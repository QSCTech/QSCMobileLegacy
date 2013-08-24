package com.myqsc.qscmobile2.fragment.cardlist;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CardlistAdapter extends BaseAdapter {
	List<CardStructure> list = new ArrayList<CardStructure>();
	Context mContext = null;
	LayoutInflater inflater = null;
	public CardlistAdapter(Context context){
		for(int i = 0; i != 30; ++i){
			CardStructure structure = new CardStructure();
			structure.cardIcon = R.string.icon_lock;
			structure.cardName = "卡片 " + i;
			structure.iconRight = R.string.icon_circle_blank;
			list.add(structure);
		}
		this.mContext = context;
		inflater = LayoutInflater.from(context);
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
		ViewHolder viewHolder = null;
		if (convertView == null){
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.simple_listview_banner, null);
			
			viewHolder.icon_left = (TextView) convertView.findViewById(R.id.simple_listview_banner_icon_left);
			viewHolder.icon_right = (TextView) convertView.findViewById(R.id.simple_listview_banner_icon_right);
			viewHolder.name = (TextView) convertView.findViewById(R.id.simple_listview_banner_text);
			
			AwesomeFontHelper.setFontFace(viewHolder.icon_left, mContext);
			AwesomeFontHelper.setFontFace(viewHolder.icon_right, mContext);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CardStructure structure = list.get(position);
		viewHolder.icon_left.setText(structure.cardIcon);
		viewHolder.name.setText(structure.cardName);
		viewHolder.icon_right.setText(structure.iconRight);
		
		if ((position & 1) == 0)
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_odd));
		else
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_even));
		return convertView;
	}
	
	private class ViewHolder{
		TextView icon_left, icon_right, name;
	}

}
