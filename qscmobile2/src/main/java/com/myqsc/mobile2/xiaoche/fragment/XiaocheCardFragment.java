package com.myqsc.mobile2.xiaoche.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.xiaoche.XiaocheActivity;

/**
 * Created by richard on 13-9-7.
 */
public class XiaocheCardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_fragment_kebiao_empty, null);
        ((TextView) view.findViewById(R.id.card_fragment_kebiao_empty_text))
                .setText("想要查校车，点这里！");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), XiaocheActivity.class));
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });
        return view;
    }
}
