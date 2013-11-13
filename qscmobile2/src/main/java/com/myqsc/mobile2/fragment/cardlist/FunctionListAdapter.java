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
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FunctionListAdapter extends BaseAdapter {
	List<FunctionStructure> list = new ArrayList<FunctionStructure>();
	Context mContext = null;
	LayoutInflater inflater = null;
    SharedPreferences preferences = null;

    final String PLUGIN_ENABLE_PREFIX = "PLUGIN_ENABLE_PREFIX_";

	public FunctionListAdapter(Context context) {
		this.mContext = context;
        preferences = context.getSharedPreferences(Utility.PREFERENCE, 0);
		inflater = LayoutInflater.from(context);

		for (int i = 0; i != FragmentUtility.cardString.length; ++i) {
			FunctionStructure structure = new FunctionStructure();
			structure.cardIcon = FragmentUtility.cardIcon[i];
			structure.cardName = FragmentUtility.cardString[i];
			structure.iconRight = R.string.icon_circle_blank;
			list.add(structure);
		}

        for (FunctionStructure structure : list) {
            if (preferences.getBoolean(PLUGIN_ENABLE_PREFIX + structure.cardName, false))
                structure.iconRight = R.string.icon_ok_sign;
            //判断每个插件卡片是不是已经选中了的
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
	public View getView(int position, View bannerLayout, ViewGroup parent) {
        bannerLayout = inflater.inflate(R.layout.simple_listview_banner,
                null);

        TextView iconLeftTextView = (TextView) bannerLayout.findViewById(R.id.simple_listview_banner_icon_left);
        TextView nameTextView = (TextView) bannerLayout.findViewById(R.id.simple_listview_banner_text);
        TextView iconRightTextView = (TextView) bannerLayout.findViewById(R.id.simple_listview_banner_icon_right);

        AwesomeFontHelper.setFontFace(iconLeftTextView, mContext);
        AwesomeFontHelper.setFontFace(iconRightTextView, mContext);

		FunctionStructure structure = list.get(position);

        nameTextView.setText(structure.cardName);
        iconLeftTextView.setText(structure.cardIcon);
        iconRightTextView.setText(structure.iconRight);

		if ((position & 1) == 0)
			bannerLayout.setBackgroundColor(mContext.getResources().getColor(
					R.color.list_odd));
		else
			bannerLayout.setBackgroundColor(mContext.getResources().getColor(
					R.color.list_even));

        setIcon(structure, iconRightTextView);
        bannerLayout.setOnClickListener(itemOnClickListener);
        bannerLayout.setTag(structure);
		return bannerLayout;
	}

    private void setIcon(FunctionStructure structure, TextView iconRightTextView) {
        if (structure.iconRight == R.string.icon_ok_sign)
            iconRightTextView.setTextColor(mContext.getResources()
                    .getColor(R.color.blue_text));
        else
            iconRightTextView.setTextColor(mContext.getResources()
                    .getColor(R.color.gray_text));
    }

    /**
     * 修改一次卡片的修改状态
     * @param structure
     */
    private void resetIcon(FunctionStructure structure) {
        if (structure.iconRight == R.string.icon_ok_sign)
            structure.iconRight = R.string.icon_circle_blank;
        else
            structure.iconRight = R.string.icon_ok_sign;
    }

    /**
     * 修改一个功能开启关闭的点击监听器
     */
    final View.OnClickListener itemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final FunctionStructure structure = (FunctionStructure) view.getTag();
            resetIcon(structure);
            setIcon(structure, (TextView) view.findViewById(R.id.simple_listview_banner_icon_right));

        }
    };

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
