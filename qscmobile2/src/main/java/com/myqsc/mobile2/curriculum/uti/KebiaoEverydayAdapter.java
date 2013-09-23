package com.myqsc.mobile2.curriculum.uti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myqsc.mobile2.R;

import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoEverydayAdapter extends BaseAdapter {
    List<KebiaoClassData> list = null;
    Context mContext = null;
    LayoutInflater mInflater = null;
    boolean noData = false;

    public KebiaoEverydayAdapter(List<KebiaoClassData> list, Context context) {
        this.list = list;
        if (list.size() == 0){
            this.list.add(new KebiaoClassData());
            noData = true;
        }

        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (noData) {
            view = LayoutInflater.from(mContext).inflate(R.layout.simple_listview_noclass, null);
            view.setBackgroundColor(mContext.getResources().getColor(R.color.list_even));
            return view;
        }

        ViewHolder holder = null;
        if (view == null){
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.simple_listview_kebiao, null);

            holder.name = (TextView) view.findViewById(R.id.kebiao_list_course_name);
            holder.teacher = (TextView) view.findViewById(R.id.kebiao_list_teacher);
            holder.time = (TextView) view.findViewById(R.id.kebiao_list_time);
            holder.place = (TextView) view.findViewById(R.id.kebiao_list_place);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        KebiaoClassData structure = list.get(i);

        holder.name.setText(structure.name);
        holder.teacher.setText(structure.teacher);
        holder.time.setText(KebiaoUtility.precessTimeInfo(structure));
        holder.place.setText(structure.place);

        if ((i & 1) == 0)
            view.setBackgroundColor(mContext.getResources().getColor(R.color.list_odd));
        else
            view.setBackgroundColor(mContext.getResources().getColor(R.color.list_even));

        return view;
    }

    private class ViewHolder{
        TextView name, teacher, time, place;
    }
}
