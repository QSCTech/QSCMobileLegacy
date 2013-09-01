package com.myqsc.qscmobile2.curriculum.fragment;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.curriculum.CurriculumActivity;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.qscmobile2.curriculum.uti.Utility;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.LogHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class KebiaoCardFragment extends Fragment {
    TextView timeTextView = null;
    TextView noticeTextView = null;
    KebiaoDataHelper helper = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.card_fragment_kebiao, null);
		
		timeTextView = ((TextView)view.findViewById(R.id.card_fragment_kebiao_time));
        noticeTextView = (TextView) view.findViewById(R.id.card_fragment_kebiao_notice);

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
                int diff = Utility.getWhichClass(Calendar.getInstance(), (List<KebiaoClassData>) message.obj);
                setTime(diff);
            }
        });

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                helper.getDay(Calendar.getInstance());
                handler.postDelayed(this, 1000);
            }
        });
		return view;
	}

    private void setTime(int diff){
        if (diff > 0) {
            noticeTextView.setText("距离下课还有");
            timeTextView.setText(Html.fromHtml(com.myqsc.qscmobile2.uti.Utility.processDiffSecond(diff)));
        } else {
            noticeTextView.setText("距离上课还有");
            timeTextView.setText(Html.fromHtml(com.myqsc.qscmobile2.uti.Utility.processDiffSecond(-diff)));
        }
    }

}
