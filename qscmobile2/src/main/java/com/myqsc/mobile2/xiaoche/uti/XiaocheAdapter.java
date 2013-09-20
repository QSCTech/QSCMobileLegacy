package com.myqsc.mobile2.xiaoche.uti;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.uti.LogHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class XiaocheAdapter extends BaseAdapter {
	List<XiaocheStructure> list = null;
	Context mContext = null;
	LayoutInflater mInflater = null;
	List<Integer> whichView = null;
	List<View> views = null;
	ListView listView = null;
	
	public static final int VIEW_BIG = 0, VIEW_SMALL = 1;
	
	
	public XiaocheAdapter(List<XiaocheStructure> list, Context context, ListView listView){
		this.list = list;
		this.whichView = new ArrayList<Integer>();
		this.views = new ArrayList<View>();
		for(int i = 0; i != list.size(); ++i){
			whichView.add(new Integer(VIEW_SMALL));
			views.add(null);
		}
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.listView = listView;
		listView.setOnItemClickListener(onItemClickListener);
	}
	
	@Override
	public int getCount() {
		return list.size();
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
		XiaocheStructure structure = list.get(position);
		
		if (whichView.get(position) == VIEW_BIG){
			if (views.get(position) == null){
				convertView = mInflater.inflate(R.layout.simple_listview_bus_big, null);;
				
				((TextView)convertView.findViewById(R.id.listview_bus_big_busnum)).setText(structure.bus_num);
				((TextView)convertView.findViewById(R.id.listview_bus_big_pos)).setText(structure.position);
				((TextView)convertView.findViewById(R.id.listview_bus_big_starttime)).setText(structure.startTime);
				((TextView)convertView.findViewById(R.id.listview_bus_big_stoptime)).setText(structure.stopTime);
				((TextView)convertView.findViewById(R.id.listview_bus_big_time)).setText(structure.runTime);
				AwesomeFontHelper.setFontFace((TextView) convertView.findViewById(R.id.listview_bus_big_icon_arrow), mContext);
				views.set(position, convertView);
			} else {
				convertView = views.get(position);
			}
				
		} else {
			if (views.get(position) == null){
				convertView = mInflater.inflate(R.layout.simple_listview_bus_small, null);
				
				((TextView) convertView.findViewById(R.id.listview_bus_small_left)).setText(structure.startTime);
				((TextView) convertView.findViewById(R.id.listview_bus_small_right)).setText(structure.bus_num);
				AwesomeFontHelper.setFontFace((TextView)convertView.findViewById(R.id.listview_bus_small_arrow), mContext);
				views.set(position, convertView);
				
			} else {
				convertView = views.get(position);
			}
		}
		
		if ((position & 1) == 0){
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_odd));
		} else {
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_even));
		}
		
		return convertView;
	}
	
	OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			int position = arg2;
			LogHelper.d(String.valueOf(position));
			views.set(position, null);
			if (whichView.get(position) == 0)
				whichView.set(position, Integer.valueOf(1));
			else
				whichView.set(position, Integer.valueOf(0));
			notifyDataSetChanged();
		}
	};
}
