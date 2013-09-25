package com.myqsc.mobile2.Grade.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.LogHelper;

import java.util.List;

/**
 * Created by richard on 13-9-25.
 */
public class GradeAdapter extends BaseAdapter {
    Context mContext = null;
    List<GradeClassStructure> list = null;
    LayoutInflater mInflater = null;

    public GradeAdapter(Context context, List<GradeClassStructure> list) {
        this.mContext = context;
        this.list = list;
        LogHelper.d("Grade List: " + list.size());
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GradeClassStructure structure = list.get(i);

        view = mInflater.inflate(R.layout.simple_listview_grade, null);
        ((TextView) view.findViewById(R.id.simple_grade_name))
                .setText(structure.name);
        ((TextView) view.findViewById(R.id.simple_grade_grade))
                .setText("成绩:" + structure.grade);
        ((TextView) view.findViewById(R.id.simple_grade_credit))
                .setText("学分:" + structure.gradePoint);
        ((TextView) view.findViewById(R.id.simple_grade_gradepoint))
                .setText("绩点:" + structure.credit);

        if ((i & 1) == 0)
            view.setBackgroundColor(mContext.getResources().getColor(R.color.list_odd));
        else
            view.setBackgroundColor(mContext.getResources().getColor(R.color.list_even));
        return view;
    }
}
