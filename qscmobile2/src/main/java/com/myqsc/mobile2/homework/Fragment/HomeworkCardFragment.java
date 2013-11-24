package com.myqsc.mobile2.homework.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.homework.HomeworkActivity;
import com.myqsc.mobile2.homework.HomeworkHelper;

/**
 * Created by richard on 13-11-19.
 * 作业功能的卡片
 */
public class HomeworkCardFragment extends Fragment {
    View view = null;
    HomeworkHelper helper = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.card_fragment_homework, null);
        if (view == null)
            return null;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HomeworkActivity.class));
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });


        helper = new HomeworkHelper(getActivity(), null);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        final TextView textView = (TextView) view.findViewById(R.id.card_fragment_homework_text);
        int times = helper.getMineHomeworkCount();
        if (times > 0) {
            //有作业
            textView.setText(String.format("你登记了 %d 个待完成的作业！", times));
        } else {
            textView.setText("你还没有登记过任何作业哟！\n");
        }
    }
}
