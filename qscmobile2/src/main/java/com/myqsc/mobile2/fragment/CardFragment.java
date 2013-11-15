package com.myqsc.mobile2.fragment;

import java.util.List;
import java.util.Vector;


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
import com.myqsc.mobile2.uti.DataObserver;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.MyFragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CardFragment extends Fragment implements DataObserver {
    View view = null;
    FragmentManager fragmentManager = null;
    UserIDStructure userIDStructure = new UserIDStructure();
    Vector<FunctionStructure> functionVector = null;
    Vector<PluginStructure> pluginVector = null;


    final static int FRAGMENT_MAGIC_NUM = 0XDD00;

    @Override
    public void onPause() {
        super.onPause();
        PersonalDataHelper personalDataHelper = new PersonalDataHelper(getActivity());
        userIDStructure = personalDataHelper.getCurrentUser();
    }

    @Override
    public void onResume() {
        super.onResume();

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


        fragmentManager = getActivity().getSupportFragmentManager();

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

    final BroadcastReceiver fragmentChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra("card");
            if (name == null)
                return ;
            int num = -1;
            LogHelper.e(name);
            for (int i = 0; i != functionVector.size(); ++i) {
                if (functionVector.get(i).cardName.equals(name) ||
                        FragmentUtility.getCardDataStringByCardName(functionVector.get(i).cardName).equals(name))
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

    /**
     * 初始化各个卡片
     */
    private void fragmentInflate() {
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
		FragmentTransaction transaction = fragmentManager.beginTransaction();
        final Fragment fragment[] = new Fragment[functionVector.size()];
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.fragment_card_layout);

		for (int i = 0; i != functionVector.size(); ++i) {
            String name = functionVector.get(i).cardName;

			fragment[i] = fragmentManager.findFragmentByTag(name);
			if (fragment[i] != null)
                transaction.remove(fragment[i]);
            fragment[i] = FragmentUtility.getCardFragmentByName(name, getActivity());
		}

        linearLayout.removeAllViews();

        LinearLayout tempLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_card_background, null);
        tempLayout.findViewById(R.id.fragment_card).setId(FRAGMENT_MAGIC_NUM + 1010);
        linearLayout.addView(tempLayout);
        transaction.add(FRAGMENT_MAGIC_NUM + 1010, new XiaoliCardFragment());

        for(int i = 0; i != functionVector.size(); ++i) {
            LinearLayout layout = (LinearLayout) inflater.inflate(
                    R.layout.fragment_card_background, null);
            layout.findViewById(R.id.fragment_card).setId(i + FRAGMENT_MAGIC_NUM);
            linearLayout.addView(layout);
            transaction.replace(i + FRAGMENT_MAGIC_NUM, fragment[i], functionVector.get(i).cardName);
        }
		transaction.commitAllowingStateLoss();
	}

    private void initCardList() {
        final int cardIDOffset = 0X123abc;
        final Context mContext = getActivity();
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        final LinearLayout pluginLayout = (LinearLayout) view.findViewById(R.id.fragment_plugin_layout);
        final WebView webView = new WebView(getActivity());

        pluginLayout.removeAllViews();
        if (pluginVector == null)
            return;

        for (final PluginStructure structure : pluginVector) {
            if (structure.isDownloaded(mContext) || structure.isSelected(mContext)) {
                LinearLayout view = (LinearLayout) mInflater.inflate(
                        R.layout.fragment_card_background, null);
                View cardView = mInflater.inflate(R.layout.plugin_card, null);
                cardView.setId(cardIDOffset + structure.id.hashCode());
                ((TextView) cardView.findViewById(R.id.card_title))
                        .setText("插件");
                ((TextView) cardView.findViewById(R.id.card_content))
                        .setText(structure.name);
                ((FrameLayout) view.findViewById(R.id.fragment_card))
                        .addView(cardView);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PluginDetailActivity.class);
                        intent.putExtra("ID", structure.id);
                        getActivity().startActivity(intent);
                    }
                });
                pluginLayout.addView(view);
            }
        }
    }

    @Override
    public void update(MyFragment fragment, final int code) {
        final FunctionListFragment functionListFragment = (FunctionListFragment) fragment;
        switch (code) {
            case 0:
                //更新功能列表
                functionVector = functionListFragment.getFunctionVector();
                fragmentInflate();
                break;
            case 1:
                //更新插件列表
                pluginVector = functionListFragment.getPluginStructureVector();
                initCardList();
                break;
        }
    }
}
