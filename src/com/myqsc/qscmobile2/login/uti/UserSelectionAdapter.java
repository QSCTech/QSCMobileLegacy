package com.myqsc.qscmobile2.login.uti;

import java.util.List;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.PersonalDataHelper;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
		LogHelper.i("User Count:" + data.size());
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
			
			holder.select = data.get(position).select;
			holder.position = position;
			
			convertView.setTag(holder);
			
			
		} else {
			holder = (ViewHolder) convertView.getTag();
			
		}
		LogHelper.i("Init item:" + position + " in position:" + holder.position);
		
		convertView.setBackgroundResource((position&1)==0 ? R.drawable.user_switch_background1 : R.drawable.user_switch_background2);
		
		if (position == 0)
			data.get(position).select = 1;
		holder.text.setText(data.get(position).uid);
		
		
		holder.icon_right.setImageResource(holder.select==0 ? R.drawable.user_switch_icon_circle : R.drawable.user_switch_icon_right);
		
		convertView.setOnLongClickListener(onLongClickListener);
		holder.icon_right.setOnClickListener(onClickListener);
		return convertView;
	}
	
	private class ViewHolder{
		ImageView icon_left;
		ImageView icon_right;
		TextView text;
		int cancle = 0;
		int select = 0;
		int position = 0;
	}
	
	OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			v = (View) v.getParent();
			final ViewHolder viewHolder = (ViewHolder) v.getTag();
			LogHelper.i("Delete Item: " + viewHolder.position + " Cancle:" + viewHolder.cancle);
			
			if (viewHolder.cancle == 1){
				//删除这个账户
				final String uid = viewHolder.text.getText().toString();
				PersonalDataHelper helper = new PersonalDataHelper(mContext);
				helper.deleteUser(uid);

				data.remove(viewHolder.position);
				notifyDataSetChanged();
			}
		}
	};
	
	OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			
			final Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(200);
			
			final ViewHolder viewHolder = (ViewHolder) v.getTag();
			
			LogHelper.d("Item: " + viewHolder.position + "Long clicked"	);
			if (viewHolder.cancle == 0){
				if (viewHolder.select == 0){
					viewHolder.cancle = 1;
					viewHolder.icon_right.setImageResource(R.drawable.user_switch_icon_wrong);
				} else {
					Toast.makeText(mContext, "不能试图删除一个默认账户哦~", Toast.LENGTH_SHORT).show();
				}
			} else {
				viewHolder.cancle = 0;
				viewHolder.icon_right.setImageResource(R.drawable.user_switch_icon_circle);
			}
			
			return false;
		}
	};
}
