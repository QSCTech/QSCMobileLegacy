package com.myqsc.qscmobile2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;

public class AboutUsFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about_us, null);
		AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.fragment_aboutus_icon), getActivity());
        ((TextView) view.findViewById(R.id.fragment_aboutus_icon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
            }
        });
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        if (motionEvent.getX() < 20)
//                            return false;
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        return true;
//                }
//                return false;
//            }
//        });
		return view;
	}
	

}
