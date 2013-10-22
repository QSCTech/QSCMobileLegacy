package com.myqsc.mobile2.Notice.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myqsc.mobile2.Notice.NoticeActivity;
import com.myqsc.mobile2.Notice.NoticeCardHelper;
import com.myqsc.mobile2.Notice.NoticeStructure;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.AwesomeFontHelper;

import java.util.List;

/**
 * Created by richard on 13-10-8.
 */
public class NoticeCardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_fragment_notice, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });

        AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.notice_card_star1), getActivity());
        AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.notice_card_star2), getActivity());
        AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.notice_card_star3), getActivity());

        NoticeCardHelper helper = new NoticeCardHelper(getActivity());

        List<NoticeStructure> list = helper.getData();

        if (list != null && list.size() >= 3) {
            try {
                NoticeStructure structure[] = {
                        list.get(0), list.get(1), list.get(2)
                };

                ((TextView) view.findViewById(R.id.notice_card_name1))
                        .setText(structure[0].getEventItem("name"));
                ((TextView) view.findViewById(R.id.notice_card_name2))
                        .setText(structure[1].getEventItem("name"));
                ((TextView) view.findViewById(R.id.notice_card_name3))
                        .setText(structure[2].getEventItem("name"));

                ((TextView) view.findViewById(R.id.notice_card_rating1))
                        .setText(structure[0].getEventItem("rating"));
                ((TextView) view.findViewById(R.id.notice_card_rating2))
                        .setText(structure[1].getEventItem("rating"));
                ((TextView) view.findViewById(R.id.notice_card_rating3))
                        .setText(structure[2].getEventItem("rating"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return view;
    }
}
