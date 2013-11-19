package com.myqsc.mobile2.homework.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.homework.HomeworkActivity;

/**
 * Created by richard on 13-11-19.
 * 作业功能的卡片
 */
public class HomeworkCardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_fragment_homework, null);
        if (view == null)
            return null;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HomeworkActivity.class));
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });
        return view;
    }
}
