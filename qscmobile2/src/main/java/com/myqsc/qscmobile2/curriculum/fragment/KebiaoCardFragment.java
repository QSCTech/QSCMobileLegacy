package com.myqsc.qscmobile2.curriculum.fragment;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.curriculum.CurriculumActivity;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.qscmobile2.curriculum.uti.Utility;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class KebiaoCardFragment extends Fragment {
    TextView diffTextView = null;
    TextView noticeTextView = null;
    KebiaoDataHelper helper = null;
    final Utility utility = new Utility();

    TextView nameTextView = null, teacherTextView = null, timeTextView = null, placeTextView = null;

    final Handler handler = new Handler();
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            helper.getDay(Calendar.getInstance());
            handler.postDelayed(this, 1000);
        }
    };
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.card_fragment_kebiao, container, false);
		
		diffTextView = ((TextView)view.findViewById(R.id.card_fragment_kebiao_diff));
        noticeTextView = (TextView) view.findViewById(R.id.card_fragment_kebiao_notice);
        nameTextView = (TextView) view.findViewById(R.id.card_fragment_kebiao_name);
        teacherTextView = (TextView) view.findViewById(R.id.card_fragment_kebiao_teacher);
        timeTextView = (TextView) view.findViewById(R.id.card_fragment_kebiao_time);
        placeTextView = (TextView) view.findViewById(R.id.card_fragment_kebiao_place);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CurriculumActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });

        helper = new KebiaoDataHelper(getActivity());
        helper.setHandleAsyncTaskMessage(new HandleAsyncTaskMessage() {
            @Override
            public void onHandleMessage(Message message) {
                if (message.what == 0)
                    Toast.makeText(getActivity(), (CharSequence) message.obj, Toast.LENGTH_SHORT).show();
                Map<Integer, Object> map = utility.getDiffTime(Calendar.getInstance(),
                        (List<KebiaoClassData>) message.obj);
                if (map == null){
                    view.findViewById(R.id.card_fragment_kebiao_big).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.card_fragment_kebiao_small).setVisibility(View.VISIBLE);
                    view.getLayoutParams().height =
                            view.findViewById(R.id.card_fragment_kebiao_small).getLayoutParams().height;
                } else {
                    view.findViewById(R.id.card_fragment_kebiao_big).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.card_fragment_kebiao_small).setVisibility(View.INVISIBLE);
                    view.getLayoutParams().height =
                            view.findViewById(R.id.card_fragment_kebiao_big).getLayoutParams().height;
                    setTime((Integer) map.get(1), (KebiaoClassData) map.get(3));
                }
            }
        });

        handler.post(runnable);
		return view;
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
    }

    private void setTime(int diff, KebiaoClassData kebiaoClassData){
        nameTextView.setText(kebiaoClassData.name);
        teacherTextView.setText(kebiaoClassData.teacher);
        timeTextView.setText(kebiaoClassData.classString());
        placeTextView.setText(kebiaoClassData.place);
        if (diff > 0) {
            noticeTextView.setText("距离下课还有");
            diffTextView.setText(Html.fromHtml(com.myqsc.qscmobile2.uti.Utility.processDiffSecond(diff)));
        } else {
            noticeTextView.setText("距离上课还有");
            diffTextView.setText(Html.fromHtml(com.myqsc.qscmobile2.uti.Utility.processDiffSecond(-diff)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
