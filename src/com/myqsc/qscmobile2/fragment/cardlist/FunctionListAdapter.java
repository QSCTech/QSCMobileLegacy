package com.myqsc.qscmobile2.fragment.cardlist;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.fragment.FragmentUtility;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FunctionListAdapter extends BaseAdapter {
	List<FunctionStructure> list = new ArrayList<FunctionStructure>();
	Context mContext = null;
	LayoutInflater inflater = null;
	Handler handler = null;
	
	public FunctionListAdapter(Context context){
		for (int i = 0; i != FragmentUtility.cardString.length; ++i){
			FunctionStructure structure = new FunctionStructure();
			structure.cardIcon = FragmentUtility.cardIcon[i];
			structure.cardName = FragmentUtility.cardString[i];
			structure.iconRight = R.string.icon_circle_blank;
			list.add(structure);
		}
		this.mContext = context;
		inflater = LayoutInflater.from(context);
		handler = new Handler(callbackFunction);
	}
	
	public Handler getHandler(){
		return handler;
	}
	
	final Handler.Callback callbackFunction = new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			int position = msg.arg1;
			TextView textView = (TextView) ((View) msg.obj).findViewById(R.id.simple_listview_banner_icon_right);
			FunctionStructure structure = list.get(position);
			if (structure.iconRight == R.string.icon_circle_blank){
				structure.iconRight = R.string.icon_ok_sign;
				textView.setText(structure.iconRight);
				textView.setTextColor(mContext.getResources().getColor(R.color.blue_text));
			} else {
				structure.iconRight = R.string.icon_circle_blank;
				textView.setText(structure.iconRight);
				textView.setTextColor(mContext.getResources().getColor(R.color.gray_text));
			}
			return true;
		}
	};
	
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
		FunctionStructure structure = list.get(position);
		viewHolder.icon_left.setText(structure.cardIcon);
		viewHolder.name.setText(structure.cardName);
		viewHolder.icon_right.setText(structure.iconRight);
		
		if (structure.iconRight == R.string.icon_ok_sign)
			viewHolder.icon_right.setTextColor(mContext.getResources().getColor(R.color.blue_text));
		else
			viewHolder.icon_right.setTextColor(mContext.getResources().getColor(R.color.gray_text));
		
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
