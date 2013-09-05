package com.myqsc.qscmobile2.curriculum.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.curriculum.CurriculumActivity;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.qscmobile2.curriculum.uti.Utility;
import com.myqsc.qscmobile2.fragment.FragmentUtility;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.LogHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by richard on 13-9-5.
 */
public class KebiaoCardEmptyFragment extends Fragment {
    Handler handler = new Handler();
    final Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            KebiaoDataHelper helper = new KebiaoDataHelper(getActivity());
            List<KebiaoClassData> list = helper.getDay(Calendar.getInstance());
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                if (Utility.getDiffTime(Calendar.getInstance(), list) != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(BroadcastHelper.BROADCAST_CARD_REDRAW);
                            intent.putExtra("card", FragmentUtility.cardString[1]);
                            if (getActivity() != null)
                                getActivity().sendBroadcast(intent);
                        }
                    });
                }
            }
            LogHelper.d("thread interrupted");
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_fragment_kebiao_empty, null);
        thread.start();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CurriculumActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        thread.interrupt();
    }
}
