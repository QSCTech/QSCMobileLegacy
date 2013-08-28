package com.myqsc.qscmobile2.exam.fragment;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.exam.ExamActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExamCardFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.card_fragment_exam, null);
		
		((TextView)view.findViewById(R.id.card_fragment_exam_text))
                .setText(Html.fromHtml("距离考试周还有 <font color='#007EF6'>23</font> 天，你还在准备吗？"));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExamActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });
		return view;
	}



}
