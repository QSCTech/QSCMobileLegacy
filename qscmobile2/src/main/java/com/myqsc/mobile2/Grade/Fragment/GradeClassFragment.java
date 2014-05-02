package com.myqsc.mobile2.Grade.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.myqsc.mobile2.Grade.Util.GradeAdapter;
import com.myqsc.mobile2.Grade.Util.GradeHelper;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.Utility;

import java.util.List;

/**
 * Created by richard on 13-9-25.
 */
public class GradeClassFragment extends Fragment {
    List<String> termString = null;
    int num = 0;
    GradeHelper gradeHelper = null;
    ListView listView = null;
    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class_grade, null);
        listView = (ListView) view.findViewById(R.id.activity_grade_list);

        gradeHelper = new GradeHelper(getActivity());
        termString = gradeHelper.getAllTermString();

        Utility.initCheckBar(view, getActivity(), onClickListener);

        initGradeList();
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case Utility.ICON_LEFT:
                    --num;
                    if (num < 0)
                        num += termString.size();
                    break;
                case Utility.ICON_RIGHT:
                    ++num;
                    if (num == termString.size())
                        num = 0;
                    break;
                default:
                    break;
            }
            initGradeList();
        }
    };

    void initGradeList() {
        if (termString.size() == 0) {
            Utility.setCheckBarTitle("暂无学期", view);
            return;
        }

        GradeAdapter adapter = new GradeAdapter(getActivity(),
                gradeHelper.getTermGrade(termString.get(num)));
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Utility.setListViewHeightBasedOnChildren(listView);
        Utility.setCheckBarTitle(termString.get(num), view);
    }


}
