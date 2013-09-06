package com.myqsc.qscmobile2.uti;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.myqsc.qscmobile2.R;

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

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static String processDiffSecond(int diff) {
        String result = "";
        if (diff >= 60 * 60 * 24)
            result += diff / 60 / 60 / 24 + "<font size='5px'>days</font>";
        diff %= 60 * 60 * 24;
        if (diff >= 60 * 60)
            result += diff / 60 / 60 + "<font size='5px'>h</font>";
        diff %= 60 * 60;
        if (diff >= 60)
            result += diff / 60 + "<font size='5px'>min</font>";
        result += diff % 60 + "<font size='5px'>s</font>";
        return result;
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
}