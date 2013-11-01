package com.myqsc.mobile2.platform.uti;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.platform.update.PlatformUpdateHelper;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.uti.Utility;

import java.util.List;

/**
 * Created by richard on 13-10-24.
 */
public class PlatformPluginListHelper {

    LinearLayout layout = null;

    public void initList(LinearLayout linearLayout) {
        this.layout = linearLayout;

        linearLayout.removeAllViews();

        Context mContext = linearLayout.getContext();

        LayoutInflater mInflater = LayoutInflater.from(mContext);

        SharedPreferences preferences = mContext.getSharedPreferences(Utility.PREFERENCE, 0);
        String listData = preferences.getString(PlatformUpdateHelper.PLUGIN_LIST_RAW, null);
        List<PluginStructure> pluginList = PlatformUpdateHelper.parsePluginList(listData);

        for (PluginStructure structure : pluginList) {
            LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.simple_listview_banner, null);

            layout.setTag(structure);
            ((TextView) layout.findViewById(R.id.simple_listview_banner_icon_left))
                    .setText(mContext.getText(R.string.icon_code));
            AwesomeFontHelper.setFontFace(
                    (TextView) layout.findViewById(R.id.simple_listview_banner_icon_left),
                    mContext
            );

            ((TextView) layout.findViewById(R.id.simple_listview_banner_text))
                    .setText(structure.name);

            AwesomeFontHelper.setFontFace(
                    (TextView) layout.findViewById(R.id.simple_listview_banner_icon_right),
                    mContext
            );

            if (!structure.isDownloaded(mContext)) {
                ((TextView) layout.findViewById(R.id.simple_listview_banner_icon_right))
                        .setText(mContext.getText(R.string.icon_arrow_circle_o_down));
                layout.setOnClickListener(onDownloadClickListener);
            } else {
                layout.setOnClickListener(onStartClickListener);
                setSelect(layout, structure);
            }

            linearLayout.addView(layout);
        }
    }


    final View.OnClickListener onDownloadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PluginStructure structure = (PluginStructure) view.getTag();
            final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
            progressDialog.setTitle("请稍后……");
            progressDialog.setMessage("正在下载中……");

            structure.downloadPlugin(progressDialog, view.getContext(), layout);
        }
    };

    final View.OnClickListener onStartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PluginStructure structure = (PluginStructure) view.getTag();
            structure.toggleSelected(view.getContext());
            setSelect((LinearLayout) view, structure);
        }
    };

    private void setSelect (LinearLayout layout, PluginStructure structure) {
        if (structure.isSelected(layout.getContext())) {
            ((TextView) layout.findViewById(R.id.simple_listview_banner_icon_right))
                    .setText(layout.getContext().getText(R.string.icon_ok_sign));
            ((TextView) layout.findViewById(R.id.simple_listview_banner_icon_right))
                    .setTextColor(layout.getContext().getResources().getColor(R.color.blue));
        } else {
            ((TextView) layout.findViewById(R.id.simple_listview_banner_icon_right))
                    .setText(layout.getContext().getText(R.string.icon_circle_blank));
            ((TextView) layout.findViewById(R.id.simple_listview_banner_icon_right))
                    .setTextColor(layout.getContext().getResources().getColor(R.color.gray_text));
        }
    }
}
