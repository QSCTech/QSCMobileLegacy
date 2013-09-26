package com.myqsc.mobile2.Grade.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.myqsc.mobile2.Grade.Util.GradeHelper;
import com.myqsc.mobile2.Grade.Util.GradeTermAdapter;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.Utility;

/**
 * Created by richard on 13-9-26.
 */
public class GradeTermFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_term_grade, null);

        ListView listView = (ListView) view.findViewById(R.id.fragment_term_grade_listview);

        GradeHelper gradeHelper = new GradeHelper(getActivity());
        GradeTermAdapter adapter = new GradeTermAdapter(getActivity(),
                gradeHelper.getAllTermGrade());
        listView.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren(listView);
        return view;
    }

}
