package com.myqsc.qscmobile2.login;

import com.myqsc.qscmobile2.AboutUsActivity;
import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.login.uti.PersonalDataHelper;
import com.umeng.fb.FeedbackAgent;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class UserSwitchFragment extends Fragment {
    List<UserIDStructure> allUserList = null;
    LinearLayout linearLayout = null;

    final int USER_MAGIC_NUM = 0XCC00;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_switch, null);

        linearLayout = (LinearLayout) view.findViewById(R.id.fragment_user_switch_main);
        initViews(inflater);

        IntentFilter intentFilter = new IntentFilter(BroadcastHelper.BROADCAST_USER_CHANGED);
        getActivity().registerReceiver(userChangedReceiver, intentFilter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(userChangedReceiver);
    }

    private void initViews(LayoutInflater inflater){
        final PersonalDataHelper personalDataHelper = new PersonalDataHelper(getActivity());
        allUserList = personalDataHelper.allUser();

        if (allUserList == null || allUserList.size() == 0) {
            Intent intent = new Intent(BroadcastHelper.BROADCAST_NEW_USER);
            getActivity().sendBroadcast(intent);
        }

        linearLayout.removeAllViews();

        if (allUserList != null) {
            for (int i = 0; i != allUserList.size(); ++i) {
                UserIDStructure structure = allUserList.get(i);
                LinearLayout temp = (LinearLayout) inflater.inflate(R.layout.simple_listview_banner, null);
                temp.setId(i + USER_MAGIC_NUM);
                temp.setOnClickListener(userOnClickListener);

                ((TextView) temp.findViewById(R.id.simple_listview_banner_icon_left))
                        .setText(R.string.icon_user);
                ((TextView) temp.findViewById(R.id.simple_listview_banner_icon_right))
                        .setText(structure.select ? R.string.icon_ok_sign : R.string.icon_circle_blank);
                ((TextView) temp.findViewById(R.id.simple_listview_banner_icon_right))
                        .setTextColor(structure.select ?
                                getActivity().getResources().getColor(R.color.blue_text)
                                : getActivity().getResources().getColor(R.color.gray_text));

                ((TextView) temp.findViewById(R.id.simple_listview_banner_text))
                        .setText(structure.uid);

                AwesomeFontHelper.setFontFace((TextView) temp.findViewById(R.id.simple_listview_banner_icon_right),
                        getActivity());
                AwesomeFontHelper.setFontFace((TextView) temp.findViewById(R.id.simple_listview_banner_icon_left),
                        getActivity());

                temp.setBackgroundColor((i & 1) == 0 ?
                        getActivity().getResources().getColor(R.color.list_odd) :
                        getActivity().getResources().getColor(R.color.list_even)
                );
                linearLayout.addView(temp);
            }
        }

        //添加新用户
        LinearLayout newUser = (LinearLayout) inflater.inflate(R.layout.simple_listview_banner, null);
        ((TextView) newUser.findViewById(R.id.simple_listview_banner_icon_left))
                .setText(R.string.icon_plus);
        AwesomeFontHelper.setFontFace((TextView) newUser.findViewById(R.id.simple_listview_banner_icon_left),
                getActivity());

        ((TextView) newUser.findViewById(R.id.simple_listview_banner_icon_right))
                .setText(R.string.icon_chevron_right);
        ((TextView) newUser.findViewById(R.id.simple_listview_banner_icon_right))
                .setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        AwesomeFontHelper.setFontFace((TextView) newUser.findViewById(R.id.simple_listview_banner_icon_right),
                getActivity());
        ((TextView) newUser.findViewById(R.id.simple_listview_banner_text))
                .setText("添加新用户");
        newUser.setId(1);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().sendBroadcast(new Intent(BroadcastHelper.BROADCAST_NEW_USER));
            }
        });
        newUser.setBackgroundColor(getActivity().getResources().getColor(R.color.list_odd));
        linearLayout.addView(newUser);

        //删除当前用户
        LinearLayout deleteUser = (LinearLayout) inflater.inflate(R.layout.simple_listview_banner, null);
        ((TextView) deleteUser.findViewById(R.id.simple_listview_banner_icon_left))
                .setText(R.string.icon_minus_sign);
        AwesomeFontHelper.setFontFace((TextView) deleteUser.findViewById(R.id.simple_listview_banner_icon_left),
                getActivity());

        ((TextView) deleteUser.findViewById(R.id.simple_listview_banner_icon_right))
                .setText(R.string.icon_chevron_right);
        ((TextView) deleteUser.findViewById(R.id.simple_listview_banner_icon_right))
                .setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        AwesomeFontHelper.setFontFace((TextView) deleteUser.findViewById(R.id.simple_listview_banner_icon_right),
                getActivity());
        ((TextView) deleteUser.findViewById(R.id.simple_listview_banner_text))
                .setText("删除当前用户");
        deleteUser.setId(2);
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("删除用户");
                builder.setMessage("确定删除用户：" +
                        ((TextView) view.findViewById(R.id.simple_listview_banner_text)).getText() +
                        "吗？");
                builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        personalDataHelper.deleteDefault();
                        initViews(LayoutInflater.from(getActivity()));
                    }
                });
                builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        deleteUser.setBackgroundColor(getActivity().getResources().getColor(R.color.list_even));
        linearLayout.addView(deleteUser);

        //关于我们
        LinearLayout aboutUs = (LinearLayout) inflater.inflate(R.layout.simple_listview_banner, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 60;
        aboutUs.setLayoutParams(params);

        ((TextView) aboutUs.findViewById(R.id.simple_listview_banner_icon_left))
                .setText(R.string.icon_info);
        AwesomeFontHelper.setFontFace((TextView) aboutUs.findViewById(R.id.simple_listview_banner_icon_left),
                getActivity());

        ((TextView) aboutUs.findViewById(R.id.simple_listview_banner_icon_right))
                .setText(R.string.icon_chevron_right);
        ((TextView) aboutUs.findViewById(R.id.simple_listview_banner_icon_right))
                .setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        AwesomeFontHelper.setFontFace((TextView) aboutUs.findViewById(R.id.simple_listview_banner_icon_right),
                getActivity());
        ((TextView) aboutUs.findViewById(R.id.simple_listview_banner_text))
                .setText("关于我们");
        aboutUs.setId(3);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), AboutUsActivity.class));
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });
        aboutUs.setBackgroundColor(getActivity().getResources().getColor(R.color.list_odd));
        linearLayout.addView(aboutUs);

        //提建议
        LinearLayout adviceUs = (LinearLayout) inflater.inflate(R.layout.simple_listview_banner, null);

        ((TextView) adviceUs.findViewById(R.id.simple_listview_banner_icon_left))
                .setText(R.string.icon_edit);
        AwesomeFontHelper.setFontFace((TextView) adviceUs.findViewById(R.id.simple_listview_banner_icon_left),
                getActivity());

        ((TextView) adviceUs.findViewById(R.id.simple_listview_banner_icon_right))
                .setText(R.string.icon_chevron_right);
        ((TextView) adviceUs.findViewById(R.id.simple_listview_banner_icon_right))
                .setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        AwesomeFontHelper.setFontFace((TextView) adviceUs.findViewById(R.id.simple_listview_banner_icon_right),
                getActivity());
        ((TextView) adviceUs.findViewById(R.id.simple_listview_banner_text))
                .setText("提建议");
        adviceUs.setId(4);
        adviceUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedbackAgent agent = new FeedbackAgent(getActivity());
                agent.startFeedbackActivity();
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });
        adviceUs.setBackgroundColor(getActivity().getResources().getColor(R.color.list_odd));
        linearLayout.addView(adviceUs);

        //去评分
        LinearLayout star = (LinearLayout) inflater.inflate(R.layout.simple_listview_banner, null);

        ((TextView) star.findViewById(R.id.simple_listview_banner_icon_left))
                .setText(R.string.icon_star);
        AwesomeFontHelper.setFontFace((TextView) star.findViewById(R.id.simple_listview_banner_icon_left),
                getActivity());

        ((TextView) star.findViewById(R.id.simple_listview_banner_icon_right))
                .setText(R.string.icon_chevron_right);
        ((TextView) star.findViewById(R.id.simple_listview_banner_icon_right))
                .setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        AwesomeFontHelper.setFontFace((TextView) star.findViewById(R.id.simple_listview_banner_icon_right),
                getActivity());
        ((TextView) star.findViewById(R.id.simple_listview_banner_text))
                .setText("去评分");
        star.setId(5);
        star.setBackgroundColor(getActivity().getResources().getColor(R.color.list_odd));
        linearLayout.addView(star);
    }

    final View.OnClickListener userOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId() - USER_MAGIC_NUM;
            PersonalDataHelper personalDataHelper = new PersonalDataHelper(getActivity());
            personalDataHelper.setDefault(allUserList.get(id).uid);
        }
    };

    final BroadcastReceiver userChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initViews(LayoutInflater.from(getActivity()));
        }
    };

}
