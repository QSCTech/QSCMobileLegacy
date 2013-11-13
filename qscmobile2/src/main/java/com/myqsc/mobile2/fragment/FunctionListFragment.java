package com.myqsc.mobile2.fragment;


import com.myqsc.mobile2.ExtraFunction.ZJUWLANLogin.ZJUWLANActivity;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.fragment.cardlist.FunctionStructure;
import com.myqsc.mobile2.platform.uti.PlatformPluginListHelper;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.MyFragment;
import com.myqsc.mobile2.uti.Utility;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;

public class FunctionListFragment extends MyFragment{
    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogHelper.d("FunctionListFragment OnCreateView called");
        view = inflater.inflate(R.layout.fragment_cardlist, null);

        initFunctionList();
        initPluginList();
        initExtraList();
        return view;
    }

    final String PLUGIN_ENABLE_PREFIX = "PLUGIN_ENABLE_PREFIX_";
    final Vector<FunctionStructure> functionVector = new Vector<FunctionStructure>();

    private void initFunctionList() {
        LinearLayout functionListLayout = (LinearLayout) view.findViewById(R.id.fragment_card_list_layout);
        final SharedPreferences preferences = getActivity().getSharedPreferences(Utility.PREFERENCE, 0);
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());

        for (int i = 0; i != FragmentUtility.cardString.length; ++i) {
            FunctionStructure structure = new FunctionStructure();
            structure.cardIcon = FragmentUtility.cardIcon[i];
            structure.cardName = FragmentUtility.cardString[i];
            structure.iconRight = R.string.icon_circle_blank;
            functionVector.add(structure);
        }

        for (int i = 0; i != functionVector.size(); ++i) {
            FunctionStructure structure = functionVector.get(i);

            if (preferences.getBoolean(PLUGIN_ENABLE_PREFIX + structure.cardName, false))
                structure.iconRight = R.string.icon_ok_sign;
            //判断每个插件卡片是不是已经选中了的

            LinearLayout bannerLayout = (LinearLayout) mInflater.inflate(R.layout.simple_listview_banner, null);
            TextView iconLeftTextView = (TextView) bannerLayout.findViewById(R.id.simple_listview_banner_icon_left);
            TextView nameTextView = (TextView) bannerLayout.findViewById(R.id.simple_listview_banner_text);
            TextView iconRightTextView = (TextView) bannerLayout.findViewById(R.id.simple_listview_banner_icon_right);

            AwesomeFontHelper.setFontFace(iconLeftTextView, getActivity());
            AwesomeFontHelper.setFontFace(iconRightTextView, getActivity());

            nameTextView.setText(structure.cardName);
            iconLeftTextView.setText(structure.cardIcon);
            iconRightTextView.setText(structure.iconRight);

            if ((i & 1) == 0)
                bannerLayout.setBackgroundColor(getActivity().getResources().getColor(
                        R.color.list_odd));
            else
                bannerLayout.setBackgroundColor(getActivity().getResources().getColor(
                        R.color.list_even));

            setIcon(structure, iconRightTextView);
            bannerLayout.setTag(structure);
            bannerLayout.setOnClickListener(itemOnClickListener);

            functionListLayout.addView(bannerLayout);
        }
        noticeObserver(0);
    }

    final View.OnClickListener itemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final FunctionStructure structure = (FunctionStructure) view.getTag();
            changeIcon(structure);
            setIcon(structure, (TextView) view.findViewById(R.id.simple_listview_banner_icon_right));
            noticeObserver(0);   //通知所有观察者
            //每次点击仅仅修改内存中的变量值，这是为了防止由于每次使用preference操作造成严重的性能问题
        }
    };

    /**
     * 获取目前启用功能的列表
     * @return
     */
    public Vector<FunctionStructure> getFunctionVector() {
        Vector<FunctionStructure> vector = new Vector<FunctionStructure>();

        try {
            SharedPreferences preferences = getActivity().getSharedPreferences(Utility.PREFERENCE, 0);
            for (FunctionStructure structure : functionVector) {
                if (structure.isSelected())
                    vector.add(structure);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vector;
    }

    /**
     * 修改一次卡片的选择状态
     * @param structure
     */
    private void changeIcon(FunctionStructure structure) {
        if (structure.isSelected()) {
            structure.iconRight = R.string.icon_circle_blank;
        }
        else {
            structure.iconRight = R.string.icon_ok_sign;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveFunctionState();
    }

    /**
     * 保存目前各个卡片的开启状态
     */
    private void saveFunctionState() {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Utility.PREFERENCE, 0)
                .edit();
        for(FunctionStructure structure : functionVector) {
            editor.putBoolean(PLUGIN_ENABLE_PREFIX + structure.cardName,
                    structure.isSelected()
            );
        }
        editor.commit();
    }

    /**
     * 修改视图的icon
     */
    private void setIcon(FunctionStructure structure, TextView iconRightTextView) {
        iconRightTextView.setText(structure.iconRight);
        if (structure.iconRight == R.string.icon_ok_sign) {
            iconRightTextView.setTextColor(getActivity().getResources()
                    .getColor(R.color.blue_text));
        }
        else
            iconRightTextView.setTextColor(getActivity().getResources()
                    .getColor(R.color.gray_text));
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
}
