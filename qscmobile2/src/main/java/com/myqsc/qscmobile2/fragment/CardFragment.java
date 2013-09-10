package com.myqsc.qscmobile2.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.fragment.cardlist.FunctionStructure;
import com.myqsc.qscmobile2.network.DataUpdater;
import com.myqsc.qscmobile2.network.UpdateHelper;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.Utility;
import com.myqsc.qscmobile2.xiaoli.fragment.XiaoliCardFragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class CardFragment extends Fragment {
	View view = null;
	LinearLayout baseLayout = null;
	List<String> list = null;
    FragmentManager fragmentManager = null;

    final static int FRAGMENT_MAGIC_NUM = 0XDD00;

	public CardFragment() {
		this.list = new ArrayList<String>();
	}

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
        getActivity().unregisterReceiver(fragmentChangedReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                BroadcastHelper.BROADCAST_FUNCTIONLIST_CHANGED);
        getActivity().registerReceiver(receiver, intentFilter);

        IntentFilter intentFilter1 = new IntentFilter(
                BroadcastHelper.BROADCAST_CARD_REDRAW);
        getActivity().registerReceiver(fragmentChangedReceiver, intentFilter1);
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.list = getListFromPreference();
		if (this.list == null)
			this.list = new ArrayList<String>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_card, null);
        final PullToRefreshScrollView pullToRefreshScrollView = (PullToRefreshScrollView) view
                .findViewById(R.id.card_pull_refresh_scrollview);
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                final int[] len = {DataUpdater.name.size()};
                final Handler handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        String result = (String) message.obj;
                        if (result != null)
                            getActivity().getSharedPreferences(Utility.PREFERENCE, 0)
                                    .edit()
                                    .putString(message.getData().getString("key"), (String) message.obj)
                                    .commit();
                        Intent intent = new Intent(BroadcastHelper.BROADCAST_CARD_REDRAW);
                        intent.putExtra("card", message.getData().getString("key"));
                        getActivity().sendBroadcast(intent);

                        --len[0];
                        if (len[0] == 0)
                            pullToRefreshScrollView.onRefreshComplete();
                        return false;
                    }
                });
                UpdateHelper helper = new UpdateHelper(getActivity());
                helper.pullToRefresh(handler);
            }
        });


		baseLayout = (LinearLayout) view
				.findViewById(R.id.fragment_card_layout);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentInflate(baseLayout, LayoutInflater.from(getActivity()), list);
		return view;
	}

    final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			list = getListFromPreference();
			if (list == null)
				list = new ArrayList<String>();
			baseLayout.removeAllViews();
			final LayoutInflater inflater = LayoutInflater.from(getActivity());
			fragmentInflate(baseLayout, inflater, list);
		}
	};

    final BroadcastReceiver fragmentChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra("card");
            for (int i = 0; i != FragmentUtility.cardString.length; ++i) {
                if (FragmentUtility.cardString[i].compareTo(name) == 0 ||
                        FragmentUtility.cardDataString[i].compareTo(name) == 0) {
                    //就是这个卡片啦！
                    if (FragmentUtility.cardDataString[i].compareTo(name) == 0)
                        name = FragmentUtility.cardString[i];

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    Fragment fragment = fragmentManager.findFragmentByTag(name);
                    if (fragment != null)
                        transaction.remove(fragment);
                    fragment = FragmentUtility.getCardFragmentByName(name, getActivity());
                    transaction.replace(i + FRAGMENT_MAGIC_NUM, fragment, name);
                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                    transaction.commit();
                }
            }
        }
    };

	private List<String> getListFromPreference() {
		if (getActivity() == null)
			return null;
		String encode = getActivity().getSharedPreferences(Utility.PREFERENCE,
				0).getString(FunctionStructure.PREFERENCE, null);
		if (encode == null)
			return null;
		
		List<String> list = new ArrayList<String>();
		try {
			JSONArray jsonArray = new JSONArray(encode);
			for(int i = 0; i != jsonArray.length(); ++i)
				list.add(jsonArray.optString(i));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



    private void fragmentInflate(LinearLayout linearLayout,
			LayoutInflater inflater, List<String> cardList) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment fragment[] = new Fragment[cardList.size()];

		for (int i = 0; i != cardList.size(); ++i) {
            String name = cardList.get(i);
            LogHelper.d(name);

			fragment[i] = fragmentManager.findFragmentByTag(name);
			if (fragment[i] != null) {
                transaction.remove(fragment[i]);
            }
			fragment[i] = FragmentUtility.getCardFragmentByName(name, getActivity());
		}

        linearLayout.removeAllViews();

        LinearLayout tempLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_card_background, null);
        tempLayout.findViewById(R.id.fragment_card).setId(1000);
        baseLayout.addView(tempLayout);
        transaction.add(1000, new XiaoliCardFragment());

        for(int i = 0; i != cardList.size(); ++i) {
            LinearLayout layout = (LinearLayout) inflater.inflate(
                    R.layout.fragment_card_background, null);
            layout.findViewById(R.id.fragment_card).setId(i + FRAGMENT_MAGIC_NUM);
            baseLayout.addView(layout);
            transaction.replace(i + FRAGMENT_MAGIC_NUM, fragment[i], cardList.get(i));
        }
		transaction.commit();
	}
}
