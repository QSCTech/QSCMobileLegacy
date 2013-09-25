package com.myqsc.mobile2.Grade.Fragment;

import com.myqsc.mobile2.Grade.GradeActivity;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.Grade.Util.GradeAverageStructure;
import com.myqsc.mobile2.Grade.Util.GradeHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ChengjiCardFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.card_fragment_chengji, null);

        GradeHelper gradeHelper = new GradeHelper(getActivity());
        GradeAverageStructure structure = gradeHelper.getTotalAverageGrade();
        if (structure == null) {
            ((TextView) view.findViewById(R.id.card_fragment_chengji_textLeft))
                    .setText("暂时木有成绩");
            ((TextView) view.findViewById(R.id.card_fragment_chengji_textRight))
                    .setText("下拉刷新试试");
        } else {
            ((TextView) view.findViewById(R.id.card_fragment_chengji_textLeft))
                    .setText(Html.fromHtml("总学分 <font color='#007EF6'>" + structure.credit + "</font>"));
            ((TextView) view.findViewById(R.id.card_fragment_chengji_textRight))
                    .setText(Html.fromHtml("平均绩点 <font color='#007EF6'>" + structure.grade + "</font>"));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GradeActivity.class);
                getActivity().startActivity(intent);
            }
        });
		return view;
	}

}
