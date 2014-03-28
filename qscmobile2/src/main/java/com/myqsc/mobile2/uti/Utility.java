package com.myqsc.mobile2.uti;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.myqsc.mobile2.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utility {
    public final static String PREFERENCE = "QSCMobile";
    public final static int WEEK_BOTH = 0;
    public final static int WEEK_ODD = 1;
    public final static int WEEK_EVEN = 2;

    public final static int ICON_LEFT = R.id.bar_icon_left;
    public final static int ICON_RIGHT = R.id.bar_icon_right;
    public final static int ICON_TITLE = R.id.bar_title;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        totalHeight += 20;

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static SpannableStringBuilder processDiffSecond(int diff) {
        final float relativeTextSize = 0.6f;

        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (diff >= 60 * 60 * 24){
            int day = diff / 60 / 60 / 24;
            builder.append(String.valueOf(day));
            int start = builder.length();
            builder.append("day ");
            builder.setSpan(new RelativeSizeSpan(relativeTextSize), start, builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        diff %= 60 * 60 * 24;
        if (diff >= 60 * 60) {
            int hour = diff / 60 / 60;
            builder.append(String.valueOf(hour));
            int start = builder.length();
            builder.append("hr ");
            builder.setSpan(new RelativeSizeSpan(relativeTextSize), start, builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        diff %= 60 * 60;
        if (diff >= 60) {
            int minute = diff / 60;
            builder.append(String.valueOf(minute));
            int start = builder.length();
            builder.append("min ");
            builder.setSpan(new RelativeSizeSpan(relativeTextSize), start, builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        int second = diff % 60;
        builder.append(String.valueOf(second));
        int start = builder.length();
        builder.append("s ");
        builder.setSpan(new RelativeSizeSpan(relativeTextSize), start, builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static void initCheckBar(View view, Context context, View.OnClickListener onClickListener) {
        if (view != null)
            __initCHeckBar(view.findViewById(ICON_LEFT),
                    view.findViewById(ICON_RIGHT),
                    context,
                    onClickListener);
        else
            __initCHeckBar(((Activity)context).findViewById(ICON_LEFT),
                    ((Activity)context).findViewById(ICON_RIGHT),
                    context,
                    onClickListener);
    }

    public static void setCheckBarTitle(String string, View view) {
        ((TextView)view.findViewById(ICON_TITLE)).setText(string);
    }

    public static void setCheckBarTitle(String string, Activity activity) {
        ((TextView)activity.findViewById(ICON_TITLE)).setText(string);
    }

    private static void __initCHeckBar(View left, View right, Context context,
                                       View.OnClickListener onClickListener) {
        left.setOnClickListener(onClickListener);
        right.setOnClickListener(onClickListener);

        AwesomeFontHelper.setFontFace((TextView) left, context);
        AwesomeFontHelper.setFontFace((TextView) right, context);
    }

    /**
     * 下载一个文件
     * @param url 文件网络地址
     * @param file 文件
     * @param force 是否强制覆盖
     * @throws IOException
     */
    public static void downloadFile (String url, File file, boolean force) throws IOException {
        if (force)
            file.delete();
        // 强制模式，强制覆盖文件

        if (file.exists())
            return;
        // 如果文件存在则返回

        file.getParentFile().mkdirs();

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        byte [] data = EntityUtils.toByteArray(httpClient.execute(httpGet).getEntity());
        // 以比特方式写入文件，防止string带来的编码问题

        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(data);
        fileOutputStream.close();
    }
}