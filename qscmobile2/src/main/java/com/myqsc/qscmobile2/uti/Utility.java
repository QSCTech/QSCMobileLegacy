package com.myqsc.qscmobile2.uti;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
	public final static String PREFERENCE = "QSCMobile";
    public final static int WEEK_BOTH = 0;
    public final static int WEEK_ODD = 1;
    public final static int WEEK_EVEN = 2;

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

    public static String processDiffSecond(int diff){
        String result = "";
        if (diff >= 60 * 60)
            result += diff / 60 / 60 + "<font size='7sp'>h</font>";
        diff %= 60 * 60;
        if (diff >= 60)
            result += diff / 60 + "<font size='7sp'>min</font>";
        result += diff % 60 + "<font size='7sp'>s</font>";
        return result;
    }
}