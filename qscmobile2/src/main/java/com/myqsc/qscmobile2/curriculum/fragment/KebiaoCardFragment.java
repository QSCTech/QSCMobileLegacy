package com.myqsc.qscmobile2.curriculum.fragment;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.curriculum.CurriculumActivity;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoUtility;
import com.myqsc.qscmobile2.uti.BroadcastHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class KebiaoCardFragment extends Fragment {
    TextView diffTextView = null;
    TextView noticeTextView = null;
    KebiaoDataHelper helper = null;

    TextView nameTextView = null, teacherTextView = null, timeTextView = null, placeTextView = null;

    final Handler handler = new Handler();
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 1000);
            List<KebiaoClassData> list = helper.getDay(Calendar.getInstance());
            Map<Integer, Object> map = KebiaoUtility.getDiffTime(
                    Calendar.getInstance(),
                    list
            );
            if (map == null) {
                Intent intent = new Intent(BroadcastHelper.BROADCAST_CARD_REDRAW);
                intent.putExtra("card", "实时课表");
                if (getActivity() != null)
                    getActivity().sendBroadcast(intent);
                return;
                //今天没有课，发送广播请求上层重载卡片
            }

            int diffTime = (Integer) map.get(1);
            KebiaoClassData kebiao = (KebiaoClassData) map.get(3);
            setTime(diffTime, kebiao);
        }
    };
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.card_fragment_kebiao, container, false);
		
		diffTextView = (TextView) view.findViewById(R.id.card_fragment_kebiao_diff);
        noticeTextView = (TextView) view.findViewById(R.id.card_fragment_kebiao_notice);
        nameTextView = (TextView) view.findViewById(R.id.card_fragment_kebiao_name);
        teacherTextView = (TextView) view.findViewById(R.id.card_fragment_kebiao_teacher);
        timeTextView = (TextView) view.findViewById(R.id.card_fragment_kebiao_time);
        placeTextView = (TextView) view.findViewById(R.id.card_fragment_kebiao_place);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CurriculumActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });

        helper = new KebiaoDataHelper(getActivity());

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
