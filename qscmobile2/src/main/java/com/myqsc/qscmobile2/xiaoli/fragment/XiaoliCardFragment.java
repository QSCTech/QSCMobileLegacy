package com.myqsc.qscmobile2.xiaoli.fragment;

import com.myqsc.qscmobile2.R;

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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View view = inflater.inflate(R.layout.card_fragment_xiaoli, null);
        timeTextView = (TextView) view.findViewById(R.id.fragment_card_xiaoli_time);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                setTime();
                handler.postDelayed(this, 2000);
            }
        });

		return view;
	}

    public void setTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        timeTextView.setText(simpleDateFormat.format(calendar.getTime()));
    }

}
