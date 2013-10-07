package com.myqsc.mobile2.platform.uti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.platform.update.PlatformUpdateHelper;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 13-9-23.
 */
public class PluginListAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mInflater = null;
    List<PluginStructure> list = null;

    public PluginListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        String pluginString = context.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(PlatformUpdateHelper.PLUGIN_LIST_PRE, null);
        LogHelper.d(pluginString);
        list = new ArrayList<PluginStructure>();
        if (pluginString == null)
            return;

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(pluginString);
            for (int i = 0; i != jsonArray.length(); ++i) {
                PluginStructure structure = new PluginStructure(jsonArray.getJSONObject(i));
                list.add(structure);
                LogHelper.d("plugin :" + structure.name + "inited");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.simple_listview_banner, null);

            holder.iconLeft = (TextView) view.findViewById(R.id.simple_listview_banner_icon_left);
            AwesomeFontHelper.setFontFace(holder.iconLeft, mContext);
            holder.iconLeft.setText(R.string.icon_code);
            holder.iconRight = (TextView) view.findViewById(R.id.simple_listview_banner_icon_right);
            AwesomeFontHelper.setFontFace(holder.iconRight, mContext);
            holder.name = (TextView) view.findViewById(R.id.simple_listview_banner_text);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(list.get(i).name);

        return view;
    }

    private class ViewHolder{
        TextView iconLeft, iconRight, name;
    }
}
