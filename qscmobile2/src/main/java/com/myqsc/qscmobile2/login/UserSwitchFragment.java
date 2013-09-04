package com.myqsc.qscmobile2.login;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.login.uti.AboutListAdapter;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.login.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.uti.Utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

@SuppressLint("ValidFragment")
public class UserSwitchFragment extends Fragment {
    List<UserIDStructure> allUserList = null;
    PersonalDataHelper personalDataHelper = null;
    LinearLayout linearLayout = null;

    final int USER_MAGIC_NUM = 0XCC00;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_switch, null);
        personalDataHelper = new PersonalDataHelper(getActivity());

        PersonalDataHelper personalDataHelper = new PersonalDataHelper(getActivity());
        allUserList = personalDataHelper.allUser();

        if (allUserList == null || allUserList.size() == 0) {
            Intent intent = new Intent(BroadcastHelper.BROADCAST_NEW_USER);
            getActivity().sendBroadcast(intent);
        }

        linearLayout = (LinearLayout) view.findViewById(R.id.fragment_user_switch_main);

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

        LinearLayout newUser = (LinearLayout) inflater.inflate(R.layout.simple_listview_banner, null);
        ((TextView) newUser.findViewById(R.id.simple_listview_banner_icon_left))
                .setText(R.string.icon_plus);
        AwesomeFontHelper.setFontFace((TextView) newUser.findViewById(R.id.simple_listview_banner_icon_left),
                getActivity());

        ((TextView) newUser.findViewById(R.id.simple_listview_banner_icon_right))
                .setText(R.string.icon_chevron_right);
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
        linearLayout.addView(newUser);

        return view;
    }

    final View.OnClickListener userOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId() - USER_MAGIC_NUM;
            Intent intent = new Intent(BroadcastHelper.BROADCAST_USER_CHANGED);
            getActivity().sendBroadcast(intent);
            personalDataHelper.setDefault(allUserList.get(id).uid);
            for (int i = 0; i != linearLayout.getChildCount(); ++i) {
                View v = linearLayout.getChildAt(i);
                if ((v.getId() & USER_MAGIC_NUM) == USER_MAGIC_NUM) {
                    ((TextView) v.findViewById(R.id.simple_listview_banner_icon_right))
                            .setText(R.string.icon_circle_blank);
                    ((TextView) v.findViewById(R.id.simple_listview_banner_icon_right))
                            .setTextColor(getActivity().getResources().getColor(R.color.gray_text));
                }
            }

            ((TextView) view.findViewById(R.id.simple_listview_banner_icon_right))
                    .setText(R.string.icon_ok_sign);
            ((TextView) view.findViewById(R.id.simple_listview_banner_icon_right))
                    .setTextColor(getActivity().getResources().getColor(R.color.blue_text));
        }
    };

}
