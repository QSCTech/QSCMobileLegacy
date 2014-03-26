package com.myqsc.mobile2.exam.uti;

import java.util.List;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.LogHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExamAdapter extends BaseAdapter {
	List<ExamStructure> data = null;
	Context mContext = null;
	LayoutInflater mInflater = null;
    boolean noData = false;
	public ExamAdapter(List<ExamStructure> data, Context context) {
		this.data = data;
        if (data.size() == 0) {
            data.add(new ExamStructure());
            noData = true;
        }
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        if (noData) {
            convertView = mInflater.inflate(R.layout.simple_listview_noclass, null);
            ((TextView) convertView.findViewById(R.id.list_no_data))
                    .setText("木有考试啦！");
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_even));
            return convertView;
        }

		ViewHolder holder = null;
		final ExamStructure structure = data.get(position);
		if (convertView == null){
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.simple_listview_exam, null);
            if (convertView == null) throw new RuntimeException("Exam Convert View Inflate Field");

			holder.name 	= (TextView) convertView.findViewById(R.id.exam_list_course_name);
			holder.time 	= (TextView) convertView.findViewById(R.id.exam_list_time);
			holder.pos 		= (TextView) convertView.findViewById(R.id.exam_list_position);
			holder.credit	= (TextView) convertView.findViewById(R.id.exam_list_credit);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

        holder.name.setText(structure.course_name);
        holder.credit.setText("学分：" + structure.credit);

		if (structure.time.length() == 0) {
			holder.time.setText("暂无考试时间");
		} else {
			holder.time.setText(structure.time);
		}

        if (structure.position.length() == 0 && structure.seat.length() == 0)
            holder.pos.setText("暂无考试地点");
        if (structure.position.length() != 0 && structure.seat.length() != 0)
            holder.pos.setText(String.format("%s 座位号：%s", structure.position, structure.seat));
        if (structure.position.length() == 0 && structure.seat.length() != 0)
            holder.pos.setText(String.format("座位号：%s", structure.seat));
        if (structure.position.length() != 0 && structure.seat.length() == 0)
            holder.pos.setText(structure.position);

		if ((position & 1) == 0)
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_odd));
		else
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_even));
		return convertView;
	}
	
	private class ViewHolder{
		TextView name, time, pos, credit;
		boolean empty = false;
	}

}
