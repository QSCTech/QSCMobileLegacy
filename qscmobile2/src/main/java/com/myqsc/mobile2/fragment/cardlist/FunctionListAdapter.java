package com.myqsc.mobile2.fragment.cardlist;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.fragment.FragmentUtility;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.uti.BroadcastHelper;
import com.myqsc.mobile2.uti.Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
		this.mContext = context;
		inflater = LayoutInflater.from(context);

		IntentFilter intentFilter = new IntentFilter(
				BroadcastHelper.BROADCAST_FUNCTIONLIST_ONITEMCLICKED);
		mContext.registerReceiver(functionItemClickReceiver, intentFilter);
		
		for (int i = 0; i != FragmentUtility.cardString.length; ++i) {
			FunctionStructure structure = new FunctionStructure();
			structure.cardIcon = FragmentUtility.cardIcon[i];
			structure.cardName = FragmentUtility.cardString[i];
			structure.iconRight = R.string.icon_circle_blank;
			list.add(structure);
		}

        List<String> selectedCard = getSelectedCard();
        if (selectedCard == null)
            return;
		for (String string : selectedCard) {
			for (FunctionStructure structure : list) {
				if (string.compareTo(structure.cardName) == 0)
					structure.iconRight = R.string.icon_ok_sign;
			}
		}
	}

    public BroadcastReceiver getBroadcastReceiver(){
        return functionItemClickReceiver;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    private List<String> getSelectedCard() {
		String encode = mContext.getSharedPreferences(Utility.PREFERENCE, 0).getString(FunctionStructure.PREFERENCE, null);
		if (encode == null)
			return null;
		
		try {
			JSONArray jsonArray = new JSONArray(encode);
			List<String> list = new ArrayList<String>();
			for(int i = 0; i != jsonArray.length(); ++i)
				list.add(jsonArray.optString(i));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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

    final BroadcastReceiver functionItemClickReceiver = new FunctionItemClickReceiver();
	private class FunctionItemClickReceiver extends BroadcastReceiver {

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

			JSONArray jsonArray = new JSONArray();
			for (String string : getCheckedCard()) {
				jsonArray.put(string);
			}
			String encode = jsonArray.toString();
			mContext.getSharedPreferences(Utility.PREFERENCE, 0).edit()
					.putString(FunctionStructure.PREFERENCE, encode).commit();

			intent = new Intent(BroadcastHelper.BROADCAST_FUNCTIONLIST_CHANGED);
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
