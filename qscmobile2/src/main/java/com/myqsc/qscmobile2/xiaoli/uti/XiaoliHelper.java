package com.myqsc.qscmobile2.xiaoli.uti;

import android.content.Context;

import com.myqsc.qscmobile2.uti.Utility;

/**
 * Created by richard on 13-8-31.
 */
public class XiaoliHelper {
    private static final String PREFERENCE = "XIAOLI_DATA";
    Context mContext = null;
    public XiaoliHelper(Context context) {
        this.mContext = context;
    }

    public void clear(String result) {
        mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .remove(PREFERENCE)
                .commit();
    }

    public void set(String result) {
        mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .putString(PREFERENCE, result)
                .commit();
    }






}
