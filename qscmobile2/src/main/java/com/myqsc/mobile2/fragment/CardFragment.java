package com.myqsc.mobile2.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.fragment.cardlist.FunctionStructure;
import com.myqsc.mobile2.login.uti.PersonalDataHelper;
import com.myqsc.mobile2.network.DataUpdater;
import com.myqsc.mobile2.network.UpdateHelper;
import com.myqsc.mobile2.platform.PluginDetailActivity;
import com.myqsc.mobile2.platform.update.PlatformUpdateHelper;
import com.myqsc.mobile2.platform.uti.PluginStructure;
import com.myqsc.mobile2.support.database.structure.UserIDStructure;
import com.myqsc.mobile2.uti.BroadcastHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;
import com.myqsc.mobile2.xiaoli.fragment.XiaoliCardFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CardFragment extends Fragment {

    View view = null;
	LinearLayout baseLayout = null;
	List<String> list = null;
    FragmentManager fragmentManager = null;
    UserIDStructure userIDStructure = new UserIDStructure();


    final static int FRAGMENT_MAGIC_NUM = 0XDD00;

	public CardFragment() {
		this.list = new ArrayList<String>();
	}

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);

        PersonalDataHelper personalDataHelper = new PersonalDataHelper(getActivity());
        userIDStructure = personalDataHelper.getCurrentUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                BroadcastHelper.BROADCAST_FUNCTIONLIST_CHANGED);
        getActivity().registerReceiver(receiver, intentFilter);

        PersonalDataHelper personalDataHelper = new PersonalDataHelper(getActivity());
        UserIDStructure structure = personalDataHelper.getCurrentUser();
        if (structure != null && !structure.equals(userIDStructure)) {
            //用户不相同，需要下拉刷新
            final PullToRefreshScrollView pullToRefreshScrollView = (PullToRefreshScrollView) view
                    .findViewById(R.id.card_pull_refresh_scrollview);
            pullToRefreshScrollView.setRefreshing();
        }
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.list = getListFromPreference();
		if (this.list == null)
			this.list = new ArrayList<String>();

        userIDStructure = new PersonalDataHelper(getActivity()).getCurrentUser();
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
                        LogHelper.d(message.getData().getString("key") + "更新完成");
                        String result = (String) message.obj;
                        if (result == null || message.getData().getString("key") == null || result.length() < 2)
                            return true;

                        if (result != null && getActivity() != null)
                            getActivity().getSharedPreferences(Utility.PREFERENCE, 0)
                                    .edit()
                                    .putString(message.getData().getString("key"), (String) message.obj)
                                    .commit();

                        Intent intent = new Intent(BroadcastHelper.BROADCAST_CARD_REDRAW);
                        intent.putExtra("card", message.getData().getString("key"));
                        if (getActivity() != null)
                            getActivity().sendBroadcast(intent);

                        --len[0];
                        if (len[0] == 0)
                            pullToRefreshScrollView.onRefreshComplete();
                        return false;
                    }
                });

                if (getActivity() != null) {
                    UpdateHelper helper = new UpdateHelper(getActivity());
                    helper.pullToRefresh(handler);
                }
            }
        });


		baseLayout = (LinearLayout) view
				.findViewById(R.id.fragment_card_layout);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentInflate(baseLayout, LayoutInflater.from(getActivity()), list);

        IntentFilter intentFilter = new IntentFilter(
                BroadcastHelper.BROADCAST_CARD_REDRAW);
        getActivity().registerReceiver(fragmentChangedReceiver, intentFilter);

		return view;
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(fragmentChangedReceiver);
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
            if (name == null)
                return ;
            int num = -1;
            LogHelper.e(name);
            for (int i = 0; i != list.size(); ++i) {
//                LogHelper.d(list.get(i));
                if (list.get(i).equals(name) ||
                        FragmentUtility.getCardDataStringByCardName(list.get(i)).equals(name))
                    num = i;
            }
            if (num == -1)
                return;

            for (int i = 0; i != FragmentUtility.cardString.length; ++i) {
                if (FragmentUtility.cardString[i].equals(name) ||
                        FragmentUtility.cardDataString[i].equals(name) ) {
                    //就是这个卡片啦！
                    if (FragmentUtility.cardDataString[i].equals(name))
                        name = FragmentUtility.cardString[i];

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    Fragment fragment = fragmentManager.findFragmentByTag(name);
                    if (fragment != null)
                        transaction.remove(fragment);

                    fragment = FragmentUtility.getCardFragmentByName(name, getActivity());
                    transaction.replace(num + FRAGMENT_MAGIC_NUM, fragment, name);
                    transaction.commitAllowingStateLoss();
                    break;
                }
            }
        }
    };

	private List<String> getListFromPreference() {
		if (getActivity() == null)
			return null;
		String encode = getActivity().getSharedPreferences(Utility.PREFERENCE,
				0).getString(FunctionStructure.PREFERENCE, null);

		List<String> list = new ArrayList<String>();
        LogHelper.e(encode);
        if (encode == null) {
            //没有选择时默认全选
            for (String string : FragmentUtility.cardString) {
                list.add(string);
            }
            return list;
        }

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
        tempLayout.findViewById(R.id.fragment_card).setId(FRAGMENT_MAGIC_NUM + 1010);
        baseLayout.addView(tempLayout);
        transaction.add(FRAGMENT_MAGIC_NUM + 1010, new XiaoliCardFragment());

        for(int i = 0; i != cardList.size(); ++i) {
            LinearLayout layout = (LinearLayout) inflater.inflate(
                    R.layout.fragment_card_background, null);
            layout.findViewById(R.id.fragment_card).setId(i + FRAGMENT_MAGIC_NUM);
            baseLayout.addView(layout);
            transaction.replace(i + FRAGMENT_MAGIC_NUM, fragment[i], cardList.get(i));
        }
		transaction.commitAllowingStateLoss();

        initCardList(linearLayout);
	}

    private void initCardList(LinearLayout layout) {
        final int cardIDOffset = 0X123abc;
        final Context mContext = layout.getContext();
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        WebView webView = new WebView(mContext);
        SharedPreferences preferences = layout.getContext()
                .getSharedPreferences(Utility.PREFERENCE, 0);
        String listData = preferences.getString(PlatformUpdateHelper.PLUGIN_LIST_RAW, null);
        List<PluginStructure> pluginList = PlatformUpdateHelper.parsePluginList(listData);

        for (PluginStructure structure : pluginList) {
            LogHelper.e("is selected:" + structure.isSelected(mContext));
            if (structure.isDownloaded(mContext) || structure.isSelected(mContext)) {
                LinearLayout view = (LinearLayout) mInflater.inflate(
                        R.layout.fragment_card_background, null);
                View cardView = mInflater.inflate(R.layout.plugin_card, null);
                cardView.setId(cardIDOffset + structure.id.hashCode());
                ((TextView) cardView.findViewById(R.id.card_title))
                        .setText("插件");
                ((TextView) cardView.findViewById(R.id.card_content))
                        .setText(structure.name);
                view.addView(cardView);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PluginDetailActivity.class);
                        getActivity().startActivity(intent);
                    }
                });
                layout.addView(view);
            }
        }
    }
}
