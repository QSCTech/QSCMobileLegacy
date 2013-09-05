package com.myqsc.qscmobile2.exam.fragment;

import java.util.Calendar;
import java.util.List;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.exam.uti.ExamAdapter;
import com.myqsc.qscmobile2.support.database.structure.ExamStructure;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;
import com.myqsc.qscmobile2.uti.ExamDataHelper;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.login.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.xiaoli.uti.XiaoliHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AllExamFragment extends Fragment {
	ListView allExamListView = null;
	char term_arr[] = {'春', '夏', '秋', '冬'};
	String year_str ;
	int term_int = 0;
	ExamDataHelper examHelper = null;
	View view = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		year_str = String.valueOf(year) + "-" + String.valueOf(year + 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_all_exam, null);
		AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.fragment_all_exam_icon_left), getActivity());
		AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.fragment_all_exam_icon_right), getActivity());
		
		view.findViewById(R.id.fragment_all_exam_icon_left).setOnClickListener(onClickListener);
		view.findViewById(R.id.fragment_all_exam_icon_right).setOnClickListener(onClickListener);
		
		allExamListView = (ListView) view.findViewById(R.id.activity_exam_term_list);
		
		examHelper = new ExamDataHelper(getActivity());

        XiaoliHelper xiaoliHelper = new XiaoliHelper(getActivity());
        switch (xiaoliHelper.getTerm(Calendar.getInstance(), false)) {
            case '春':case '夏':case '秋':case '冬':
                for (int i = 0; i != term_arr.length; ++i)
                    if (xiaoliHelper.getTerm(Calendar.getInstance(), false) == term_arr[i]) {
                        term_int = i;
                    }
                break;
            case '寒':
                term_int = 0;
                break;
            case '署':
                term_int = 2;
                break;
        }

		updateExamData();
		return view;
	}
	
	void updateExamData(){
		List<ExamStructure> list = examHelper.getExamList(term_arr[term_int]);
		((TextView)view.findViewById(R.id.fragment_exam_all_term_textview))
                .setText(year_str + term_arr[term_int]);
        ExamAdapter adapter = new ExamAdapter(list, getActivity());
        allExamListView.setAdapter(adapter);
	}
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.fragment_all_exam_icon_left)
				--term_int;
			if (v.getId() == R.id.fragment_all_exam_icon_right)
				++term_int;
			term_int = (term_int + 4) % 4;
			updateExamData();
		}
	};
}
