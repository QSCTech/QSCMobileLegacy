package com.myqsc.qscmobile2.exam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoUtility;
import com.myqsc.qscmobile2.exam.uti.ExamAdapter;
import com.myqsc.qscmobile2.exam.uti.ExamDataHelper;
import com.myqsc.qscmobile2.exam.uti.ExamStructure;
import com.myqsc.qscmobile2.uti.Utility;

import java.util.Calendar;
import java.util.List;

/**
 * Created by richard on 13-9-6.
 */
public class EveryDayExamFragment extends Fragment {
    Calendar calendar = Calendar.getInstance();
    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exam_everyday, null);
        Utility.initCheckBar(view, getActivity(), onClickListener);
        initList();
        return view;
    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == Utility.ICON_LEFT)
                calendar.add(Calendar.DATE, -1);
            else
                calendar.add(Calendar.DATE, 1);
            initList();
        }
    };

    private void initList(){
        Utility.setCheckBarTitle(KebiaoUtility.processTimeTitle(calendar), view);
        ExamDataHelper examDataHelper = new ExamDataHelper(getActivity());
        List<ExamStructure> list = examDataHelper.getTodayExamList(calendar);
        ((ListView) view.findViewById(R.id.fragment_exam_everyday_list))
                .setAdapter(new ExamAdapter(list, getActivity()));
    }


}
