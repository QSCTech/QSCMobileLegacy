package com.myqsc.mobile2.exam.fragment;

import java.util.Calendar;
import java.util.List;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.exam.uti.ExamAdapter;
import com.myqsc.mobile2.exam.uti.ExamStructure;
import com.myqsc.mobile2.exam.uti.ExamDataHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;
import com.myqsc.mobile2.xiaoli.uti.XiaoliHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
		year_str = String.valueOf(year - 1) + "-" + String.valueOf(year);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_exam_all, null);

        Utility.initCheckBar(view, getActivity(), onClickListener);
		allExamListView = (ListView) view.findViewById(R.id.activity_exam_term_list);
        allExamListView.setFocusable(false);
		
		examHelper = new ExamDataHelper(getActivity());

        XiaoliHelper xiaoliHelper = new XiaoliHelper(getActivity());

        char term = xiaoliHelper.getTerm(Calendar.getInstance(), false);
        switch (term) {
            case '春':case '夏':case '秋':case '冬':
                for (int i = 0; i != term_arr.length; ++i)
                    if (term == term_arr[i]) {
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
        Utility.setCheckBarTitle(year_str + term_arr[term_int], view);
        ExamAdapter adapter = new ExamAdapter(list, getActivity());
        allExamListView.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren(allExamListView);
	}
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v.getId() == Utility.ICON_LEFT)
				--term_int;
			if (v.getId() == Utility.ICON_RIGHT)
				++term_int;
			term_int = (term_int + 4) % 4;
            LogHelper.d("这是" + term_arr[term_int] + "学期");
			updateExamData();
		}
	};
}
