package com.myqsc.mobile2.fragment;


import com.myqsc.mobile2.ExtraFunction.ZJUWLANLogin.ZJUWLANActivity;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.fragment.cardlist.FunctionStructure;
import com.myqsc.mobile2.fragment.uti.PluginListInitCallback;
import com.myqsc.mobile2.platform.update.PlatformUpdateHelper;
import com.myqsc.mobile2.platform.uti.PlatformPluginListHelper;
import com.myqsc.mobile2.platform.uti.PluginStructure;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.MyFragment;
import com.myqsc.mobile2.uti.Utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

public class FunctionListFragment extends MyFragment{
    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogHelper.d("FunctionListFragment OnCreateView called");
        view = inflater.inflate(R.layout.fragment_cardlist, null);

        initFunctionList();
        initPluginList();
        initExtraList();
        return view;
    }

    final String PLUGIN_ENABLE_PREFIX = "PLUGIN_ENABLE_PREFIX_";
    final Vector<FunctionStructure> functionVector = new Vector<FunctionStructure>();

    private void initFunctionList() {
        LinearLayout functionListLayout = (LinearLayout) view.findViewById(R.id.fragment_card_list_layout);
        final SharedPreferences preferences = getActivity().getSharedPreferences(Utility.PREFERENCE, 0);
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());

        for (int i = 0; i != FragmentUtility.cardString.length; ++i) {
            FunctionStructure structure = new FunctionStructure();
            structure.cardIcon = FragmentUtility.cardIcon[i];
            structure.cardName = FragmentUtility.cardString[i];
            structure.iconRight = R.string.icon_circle_blank;
            functionVector.add(structure);
        }

        for (int i = 0; i != functionVector.size(); ++i) {
            FunctionStructure structure = functionVector.get(i);

            if (preferences.getBoolean(PLUGIN_ENABLE_PREFIX + structure.cardName, false))
                structure.iconRight = R.string.icon_ok_sign;
            //判断每个插件卡片是不是已经选中了的

            LinearLayout bannerLayout = (LinearLayout) mInflater.inflate(R.layout.simple_listview_banner, null);
            TextView iconLeftTextView = (TextView) bannerLayout.findViewById(R.id.simple_listview_banner_icon_left);
            TextView nameTextView = (TextView) bannerLayout.findViewById(R.id.simple_listview_banner_text);
            TextView iconRightTextView = (TextView) bannerLayout.findViewById(R.id.simple_listview_banner_icon_right);

            AwesomeFontHelper.setFontFace(iconLeftTextView, getActivity());
            AwesomeFontHelper.setFontFace(iconRightTextView, getActivity());

            nameTextView.setText(structure.cardName);
            iconLeftTextView.setText(structure.cardIcon);
            iconRightTextView.setText(structure.iconRight);

            if ((i & 1) == 0)
                bannerLayout.setBackgroundColor(getActivity().getResources().getColor(
                        R.color.list_odd));
            else
                bannerLayout.setBackgroundColor(getActivity().getResources().getColor(
                        R.color.list_even));

            setIcon(structure, iconRightTextView);
            bannerLayout.setTag(structure);
            bannerLayout.setOnClickListener(itemOnClickListener);

            functionListLayout.addView(bannerLayout);
        }

        functionListLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                noticeObserver(0);
            }
        }, 100);
    }

    final View.OnClickListener itemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final FunctionStructure structure = (FunctionStructure) view.getTag();
            changeIcon(structure);
            setIcon(structure, (TextView) view.findViewById(R.id.simple_listview_banner_icon_right));
            noticeObserver(0);   //通知所有观察者
            //每次点击仅仅修改内存中的变量值，这是为了防止由于每次使用preference操作造成严重的性能问题
        }
    };

    /**
     * 获取目前启用功能的列表
     * @return
     */
    public Vector<FunctionStructure> getFunctionVector() {
        Vector<FunctionStructure> vector = new Vector<FunctionStructure>();
        try {
            for (FunctionStructure structure : functionVector) {
                if (structure.isSelected())
                    vector.add(structure);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vector;
    }

    /**
     * 修改一次卡片的选择状态
     * @param structure
     */
    private void changeIcon(FunctionStructure structure) {
        if (structure.isSelected()) {
            structure.iconRight = R.string.icon_circle_blank;
        }
        else {
            structure.iconRight = R.string.icon_ok_sign;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveFunctionState();
    }

    /**
     * 保存目前各个卡片的开启状态
     */
    private void saveFunctionState() {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Utility.PREFERENCE, 0)
                .edit();
        for(FunctionStructure structure : functionVector) {
            editor.putBoolean(PLUGIN_ENABLE_PREFIX + structure.cardName,
                    structure.isSelected()
            );
        }
        editor.commit();
    }

    /**
     * 修改视图的icon
     */
    private void setIcon(FunctionStructure structure, TextView iconRightTextView) {
        iconRightTextView.setText(structure.iconRight);
        if (structure.iconRight == R.string.icon_ok_sign) {
            iconRightTextView.setTextColor(getActivity().getResources()
                    .getColor(R.color.blue_text));
        }
        else
            iconRightTextView.setTextColor(getActivity().getResources()
                    .getColor(R.color.gray_text));
    }

    Vector<PluginStructure> pluginStructureVector = null;
    private void initPluginList() {
        PlatformPluginListHelper helper = new PlatformPluginListHelper();
        helper.setPluginListInitCallback(pluginListInitCallback);
        helper.initList(getActivity());
    }

    /**
     * 当platformPluginListHelper 中成功下载到pluginlist后，回调，需要保证在ui线程回调
     */
    final PluginListInitCallback pluginListInitCallback = new PluginListInitCallback() {
        @Override
        public void initList(Vector<PluginStructure> pluginVector) {
            final LinearLayout pluginLayout = (LinearLayout) view.findViewById(R.id.plugin_list_layout);
            final Context mContext = getActivity();
            final LayoutInflater mInflater = LayoutInflater.from(getActivity());

            //activity after finish
            if (mContext == null)
                return ;

            if (pluginVector == null) {
                pluginVector = PlatformUpdateHelper.parsePluginList(
                                    getActivity().getSharedPreferences(Utility.PREFERENCE, 0)
                                            .getString(PlatformUpdateHelper.PLUGIN_LIST_RAW, null)
                );
            }

            pluginStructureVector = pluginVector;
            if (pluginStructureVector == null)
                return ;

            pluginLayout.removeAllViews();

            for (PluginStructure structure : pluginVector) {
                LinearLayout layout = (LinearLayout) mInflater
                        .inflate(R.layout.simple_listview_banner, null);

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
                pluginLayout.addView(layout);
            }

            pluginLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    noticeObserver(1);
                }
            }, 200);
        }
    };

    public Vector<PluginStructure> getPluginStructureVector() {
        if (pluginStructureVector == null)
            return null;
        Vector<PluginStructure> pluginVector = new Vector<PluginStructure>();

        for (PluginStructure structure : pluginStructureVector) {
            if (structure.isSelected(getActivity()))
                pluginVector.add(structure);
        }
        return pluginVector;
    }

    /**
     * 修改插件列表中插件启用状态的点击监听器
     */
    final View.OnClickListener onStartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PluginStructure structure = (PluginStructure) view.getTag();
            structure.toggleSelected(view.getContext());
            setSelect((LinearLayout) view, structure);
            noticeObserver(1);
        }
    };

    /**
     * 设置插件列表中插件的右侧图标
     * @param layout
     * @param structure
     */
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

    ProgressDialog progressDialog = null;
    /**
     * 开始下载插件列表的监听器
     */
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
                            progressDialog.setTitle("请稍候…");
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
                            progressDialog.setTitle("请稍候…");
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
                            pluginListInitCallback.initList(null);
                        }
                    });
                }
            }).start();
        }
    };

    private void initExtraList() {
        LinearLayout ZJUWLANLoginLayout = (LinearLayout) view.findViewById(R.id.function_extra);

        AwesomeFontHelper.setFontFace((TextView) ZJUWLANLoginLayout.findViewById(R.id.simple_listview_banner_icon_left),
                getActivity());
        ((TextView) ZJUWLANLoginLayout.findViewById(R.id.simple_listview_banner_icon_left))
                .setText(R.string.icon_signal);

        ((TextView) ZJUWLANLoginLayout.findViewById(R.id.simple_listview_banner_text))
                .setText("ZJUWLAN 自动登录");

        ZJUWLANLoginLayout.setBackgroundColor(getResources().getColor(R.color.list_odd));
        ZJUWLANLoginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ZJUWLANActivity.class));
                getActivity().overridePendingTransition(R.anim.right_push_in, 0);
            }
        });
    }
}
