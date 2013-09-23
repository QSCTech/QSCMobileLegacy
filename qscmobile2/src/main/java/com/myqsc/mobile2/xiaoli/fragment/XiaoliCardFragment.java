package com.myqsc.mobile2.xiaoli.fragment;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.xiaoli.uti.XiaoliHelper;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class XiaoliCardFragment extends Fragment {
    TextView timeTextView = null;
    final Handler handler = new Handler();

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setTime();
            handler.postDelayed(this, 2000);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.card_fragment_xiaoli, null);
        timeTextView = (TextView) view.findViewById(R.id.fragment_card_xiaoli_time);

        XiaoliHelper helper = new XiaoliHelper(getActivity());
        ((TextView) view.findViewById(R.id.fragment_card_xiaoli_term))
                .setText(String.valueOf(helper.getTerm(
                        Calendar.getInstance(),
                        false
                )));


        ((TextView) view.findViewById(R.id.fragment_card_xiaoli_day)).setText(helper.getDayString(
                Calendar.getInstance(),
                false
        ));

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(runnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        timeTextView.setText(simpleDateFormat.format(calendar.getTime()));
    }

}
