package com.myqsc.qscmobile2.login.uti;

import java.util.List;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserSelectionAdapter extends BaseAdapter{
	Context mContext = null;
	List<UserIDStructure> data = null;
	LayoutInflater inflater = null;
	public UserSelectionAdapter(Context context, List<UserIDStructure> data){
		this.mContext = context;
		this.data = data;
		this.inflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
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
			
			convertView = inflater.inflate(R.layout.simple_listview_banner, null);
			holder.icon_left = (ImageView) convertView.findViewById(R.id.simple_listview_banner_icon_left);
			holder.icon_right = (ImageView) convertView.findViewById(R.id.simple_listview_banner_icon_right);
			holder.text = (TextView) convertView.findViewById(R.id.simple_listview_banner_text);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			
		}
		holder.text.setText(data.get(position).uid);
		return convertView;
	}
	
	private class ViewHolder{
		ImageView icon_left;
		ImageView icon_right;
		TextView text;
	}

}
