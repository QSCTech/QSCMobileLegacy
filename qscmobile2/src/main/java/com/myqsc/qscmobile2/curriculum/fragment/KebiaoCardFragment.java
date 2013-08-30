package com.myqsc.qscmobile2.curriculum.fragment;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.curriculum.CurriculumActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class KebiaoCardFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.card_fragment_kebiao, null);
		
		SpannableStringBuilder text = new SpannableStringBuilder();
		text.append("21min34s");
		text.setSpan(new RelativeSizeSpan(0.5f), 2, 5, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		text.setSpan(new RelativeSizeSpan(0.5f), 7, 8, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		
		((TextView)view.findViewById(R.id.card_fragment_kebiao_time)).setText(text);
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

}
