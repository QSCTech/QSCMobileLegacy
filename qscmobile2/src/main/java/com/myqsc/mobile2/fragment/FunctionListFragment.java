package com.myqsc.mobile2.fragment;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.myqsc.mobile2.ExtraFunction.ZJUWLANLogin.ZJUWLANActivity;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.fragment.cardlist.FunctionListAdapter;
import com.myqsc.mobile2.platform.update.PlatformUpdateHelper;
import com.myqsc.mobile2.platform.uti.PlatformPluginListHelper;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.uti.BroadcastHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class FunctionListFragment extends Fragment {
    BroadcastReceiver receiver = null;
    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogHelper.d("FunctionListFragment OnCreateView called");
        view = inflater.inflate(R.layout.fragment_cardlist, null);

        final FunctionListAdapter adapter = new FunctionListAdapter(getActivity());
        receiver = adapter.getBroadcastReceiver();

        ((ListView) view.findViewById(R.id.fragment_cardlist))
                .setAdapter(adapter);
        ((ListView) view.findViewById(R.id.fragment_cardlist))
                .setOnItemClickListener(onItemClickListener);
        Utility.setListViewHeightBasedOnChildren((ListView) view.findViewById(R.id.fragment_cardlist));

        initPluginList();
        initExtraList();
        return view;
    }

    private void initPluginList() {
        PlatformPluginListHelper helper = new PlatformPluginListHelper();
        helper.initList(
                (LinearLayout) view.findViewById(R.id.plugin_list_layout)
        );
    }

    private void initExtraList() {
        LinearLayout ZJUWLANLoginLayout = (LinearLayout) view.findViewById(R.id.function_extra);

        AwesomeFontHelper.setFontFace((TextView) ZJUWLANLoginLayout.findViewById(R.id.simple_listview_banner_icon_left),
                getActivity());
        ((TextView) ZJUWLANLoginLayout.findViewById(R.id.simple_listview_banner_icon_left))
                .setText(R.string.icon_signal);

        ((TextView) ZJUWLANLoginLayout.findViewById(R.id.simple_listview_banner_text))
                .setText("ZJUWLAN 自动登录");

        ZJUWLANLoginLayout.setBackgroundColor(getResources().getColor(R.color.list_odd));
        ZJUWLANLoginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ZJUWLANActivity.class));
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(receiver);
    }

    final OnItemClickListener onItemClickListener = new OnItemClickListener() {
        // TODO: Provide argument names
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            Intent intent = new Intent(BroadcastHelper.BROADCAST_FUNCTIONLIST_ONITEMCLICKED);
            intent.putExtra("position", arg2);
            getActivity().sendBroadcast(intent);
        }
    };
}
