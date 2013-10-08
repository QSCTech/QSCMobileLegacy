package com.myqsc.mobile2.Notice.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myqsc.mobile2.Notice.NoticeActivity;
import com.myqsc.mobile2.R;

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
        return view;
    }
}
