package com.myqsc.qscmobile2.exam.uti;

import java.util.List;
import java.util.zip.Inflater;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.support.database.structure.ExamStructure;

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
	public ExamAdapter(List<ExamStructure> data, Context context) {
		this.data = data;
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
		ViewHolder holder = null;
		if (convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.simple_listview_exam, null);
			
			holder.name = (TextView) convertView.findViewById(R.id.exam_list_course_name);
			holder.time = (TextView) convertView.findViewById(R.id.exam_list_time);
			holder.pos = (TextView) convertView.findViewById(R.id.exam_list_position);
			holder.credit = (TextView) convertView.findViewById(R.id.exam_list_credit);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ExamStructure structure = data.get(position);
		
		holder.name.setText(structure.course_name);
		holder.time.setText(structure.time);
		holder.pos.setText(structure.position + " 座位号" + structure.seat	);
		holder.credit.setText("学分:" + structure.credit);
		
		if ((position & 1) == 0)
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_odd));
		else
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_even));
		return convertView;
	}
	
	private class ViewHolder{
		TextView name, time, pos, credit;
	}

}
