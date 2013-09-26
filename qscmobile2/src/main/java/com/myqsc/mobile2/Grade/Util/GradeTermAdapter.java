package com.myqsc.mobile2.Grade.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myqsc.mobile2.R;

import java.util.List;

/**
 * Created by richard on 13-9-26.
 */
public class GradeTermAdapter extends BaseAdapter {
    List<GradeAverageStructure> list = null;
    LayoutInflater mInflater = null;
    Context mContext = null;

    public GradeTermAdapter(Context context, List<GradeAverageStructure> list) {
        this.list = list;
        this.mContext = context;
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
        GradeAverageStructure structure = list.get(i);

        view = mInflater.inflate(R.layout.simple_listview_grade_term, null);

        ((TextView) view.findViewById(R.id.listview_grade_term_time))
                .setText(structure.time);
        ((TextView) view.findViewById(R.id.listview_grade_term_GPA))
                .setText("平均绩点：" + structure.grade);
        ((TextView) view.findViewById(R.id.listview_grade_term_credit))
                .setText("总学分：" + structure.credit);

        if ( (i & 1) == 0)
            view.setBackgroundColor(mContext.getResources().getColor(R.color.list_odd));
        else
            view.setBackgroundColor(mContext.getResources().getColor(R.color.list_even));

        return view;
    }
}
