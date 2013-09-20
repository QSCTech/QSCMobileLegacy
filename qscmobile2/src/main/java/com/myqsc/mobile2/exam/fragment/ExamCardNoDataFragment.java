package com.myqsc.mobile2.exam.fragment;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.exam.ExamActivity;
import com.myqsc.mobile2.exam.uti.ExamDataHelper;
import com.myqsc.mobile2.network.DataUpdater;
import com.myqsc.mobile2.uti.BroadcastHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

public class ExamCardNoDataFragment extends Fragment {

    final Handler handler = new Handler();
    final Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            final ExamDataHelper examDataHelper = new ExamDataHelper(getActivity());
            while (true){
                if (examDataHelper.getCardExamStructure(Calendar.getInstance()) != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(BroadcastHelper.BROADCAST_CARD_REDRAW);
                            intent.putExtra("card", DataUpdater.JW_KAOSHI);
                            getActivity().sendBroadcast(intent);
                        }
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_fragment_exam, null);

        ((TextView) view.findViewById(R.id.card_fragment_exam_text))
                .setText("30天以内木有考试哦~");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExamActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });
        thread.start();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        thread.interrupt();
    }
}
