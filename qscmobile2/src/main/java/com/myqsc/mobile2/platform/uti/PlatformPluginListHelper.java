package com.myqsc.mobile2.platform.uti;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.platform.update.PlatformUpdateHelper;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;

import java.util.List;

/**
 * Created by richard on 13-10-24.
 */
public class PlatformPluginListHelper {

    LinearLayout parentLayout = null;

    public void initList(final LinearLayout linearLayout) {
        this.parentLayout = linearLayout;
        final Context mContext = parentLayout.getContext();

        final SharedPreferences preferences = mContext.getSharedPreferences(Utility.PREFERENCE, 0);
        String listData = preferences.getString(PlatformUpdateHelper.PLUGIN_LIST_RAW, null);
        final List<PluginStructure> localPluginList = PlatformUpdateHelper.parsePluginList(listData);
        __initPluginList(localPluginList);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String pluginListStr = PlatformUpdateHelper.syncGetPluginList();
                final List<PluginStructure> pluginList = PlatformUpdateHelper.parsePluginList(
                    pluginListStr
                );
                preferences.edit()
                        .putString(PlatformUpdateHelper.PLUGIN_LIST_RAW, pluginListStr)
                        .commit();
                parentLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        __initPluginList(pluginList);
                    }
                });
            }
        }).start();
    }

    private void __initPluginList (List<PluginStructure> mlist) {
        LogHelper.e("Initing Plugin List");

        parentLayout.removeAllViews();

        final Context mContext = parentLayout.getContext();
        final LayoutInflater mInflater = LayoutInflater.from(mContext);

        if (mlist == null) {
            mlist = PlatformUpdateHelper.parsePluginList(
                    mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                        .getString(PlatformUpdateHelper.PLUGIN_LIST_RAW, null)
            );
        }


        for (PluginStructure structure : mlist) {
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
                layout.setOnClickListener(onStartDownloadListener);
            } else {
                layout.setOnClickListener(onStartClickListener);
                setSelect(layout, structure);
            }

            parentLayout.addView(layout);
        }
    }

    ProgressDialog progressDialog = null;
    final View.OnClickListener onStartDownloadListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final PluginStructure structure = (PluginStructure) view.getTag();

                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog = new ProgressDialog(view.getContext());
                            progressDialog.setMessage("正在同步平台文件");
                            progressDialog.setTitle("请稍后……");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                        }
                    });

                    if (!PlatformUpdateHelper.syncPlatformFile(view.getContext())) {
                        //平台文件同步失败
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(view.getContext(), "平台文件同步失败", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                        return;
                    }
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(view.getContext(), "平台文件同步完成", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });

                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog = new ProgressDialog(view.getContext());
                            progressDialog.setTitle("请稍后……");
                            progressDialog.setMessage("正在下载插件");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                        }
                    });

                    structure.downloadPlugin(view.getContext());

                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(view.getContext(), "插件下载完成", Toast.LENGTH_SHORT).show();
                            __initPluginList(null);
                        }
                    });
                }
            }).start();
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
