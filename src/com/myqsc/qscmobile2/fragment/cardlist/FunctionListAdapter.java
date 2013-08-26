package com.myqsc.qscmobile2.fragment.cardlist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.fragment.FragmentUtility;
import com.myqsc.qscmobile2.fragment.uti.OnFragmentMessage;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.LogHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

	public FunctionListAdapter(Context context) {
		for (int i = 0; i != FragmentUtility.cardString.length; ++i) {
			FunctionStructure structure = new FunctionStructure();
			structure.cardIcon = FragmentUtility.cardIcon[i];
			structure.cardName = FragmentUtility.cardString[i];
			structure.iconRight = R.string.icon_circle_blank;
			list.add(structure);
		}
		this.mContext = context;
		inflater = LayoutInflater.from(context);

		IntentFilter intentFilter = new IntentFilter(
				BroadcastHelper.BROADCAST_FUNCTIONLIST_ONITEMCLICKED);
		mContext.registerReceiver(new receiver(), intentFilter);
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
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.simple_listview_banner,
					null);

			viewHolder.icon_left = (TextView) convertView
					.findViewById(R.id.simple_listview_banner_icon_left);
			viewHolder.icon_right = (TextView) convertView
					.findViewById(R.id.simple_listview_banner_icon_right);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.simple_listview_banner_text);

			AwesomeFontHelper.setFontFace(viewHolder.icon_left, mContext);
			AwesomeFontHelper.setFontFace(viewHolder.icon_right, mContext);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		FunctionStructure structure = list.get(position);
		viewHolder.icon_left.setText(structure.cardIcon);
		viewHolder.name.setText(structure.cardName);
		viewHolder.icon_right.setText(structure.iconRight);

		if (structure.iconRight == R.string.icon_ok_sign)
			viewHolder.icon_right.setTextColor(mContext.getResources()
					.getColor(R.color.blue_text));
		else
			viewHolder.icon_right.setTextColor(mContext.getResources()
					.getColor(R.color.gray_text));

		if ((position & 1) == 0)
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.list_odd));
		else
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.list_even));
		return convertView;
	}

	private class ViewHolder {
		TextView icon_left, icon_right, name;
	}

	private class receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// item被点击
			int position = intent.getExtras().getInt("position");
			FunctionStructure structure = list.get(position);

			if (structure.iconRight == R.string.icon_circle_blank) {
				structure.iconRight = R.string.icon_ok_sign;
			} else {
				structure.iconRight = R.string.icon_circle_blank;
			}
			notifyDataSetChanged();
			
			
			
			intent = new Intent(BroadcastHelper.BROADCAST_FUNCTIONLIST_CHANGED);
			intent.putExtra("cards", (Serializable)getCheckedCard());
			mContext.sendBroadcast(intent);
		}

	}
	
	private List<String> getCheckedCard() {
		List<String> list = new ArrayList<String>();
		for (FunctionStructure structure : this.list) {
			if (structure.iconRight == R.string.icon_ok_sign)
				list.add(structure.cardName);
		}
		return list;
	}

}
