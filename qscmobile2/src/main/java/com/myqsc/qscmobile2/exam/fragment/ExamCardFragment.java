package com.myqsc.qscmobile2.exam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.exam.uti.ExamDataHelper;
import com.myqsc.qscmobile2.exam.uti.ExamStructure;
import com.myqsc.qscmobile2.fragment.FragmentUtility;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.Utility;

import java.util.Calendar;

/**
 * Created by richard on 13-9-6.
 */
public class ExamCardFragment extends Fragment {
    View view = null;
    final Handler handler = new Handler();
    final Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            final ExamDataHelper helper = new ExamDataHelper(getActivity());
            while (true) {
                final ExamStructure structure = helper.getCardExamStructure(Calendar.getInstance());
                if (structure == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (getActivity() != null) {
                                Intent intent = new Intent(BroadcastHelper.BROADCAST_CARD_REDRAW);
                                intent.putExtra("card", FragmentUtility.cardString[2]);
                                getActivity().sendBroadcast(intent);
                            }
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setText(structure);
                        }
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return ;
                }
            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.card_fragment_kebiao, null);
        thread.start();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        thread.interrupt();
    }

    private void setText(ExamStructure examStructure) {
        ((TextView) view.findViewById(R.id.card_fragment_kebiao_diff))
                .setText(Html.fromHtml(
                        Utility.processDiffSecond((int) ((examStructure.getStartTime().getTimeInMillis() -
                                Calendar.getInstance().getTimeInMillis()) / 1000))));
        ((TextView) view.findViewById(R.id.card_fragment_kebiao_notice))
                .setText("距离下一门考试还有：");
        ((TextView) view.findViewById(R.id.card_fragment_kebiao_name))
                .setText(examStructure.course_name);
        view.findViewById(R.id.card_fragment_kebiao_teacher)
                .setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.card_fragment_kebiao_time))
                .setText(examStructure.time.length() < 3 ?
                        "暂时没有考试时间信息" : examStructure.time);
        ((TextView) view.findViewById(R.id.card_fragment_kebiao_place))
                .setText(examStructure.position.length() < 3 ?
                        "暂时没有考试地点信息" : examStructure.position);
    }

}
