package com.myqsc.qscmobile2.curriculum.uti;

import android.content.Context;

import com.myqsc.qscmobile2.curriculum.ClassData;
import com.myqsc.qscmobile2.uti.Utility;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoDataHelper {
    Context mContext = null;

    public KebiaoDataHelper(Context context){
        this.mContext = context;
    }

    public void clear(){
        mContext
                .getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .remove(ClassData.PREFERENCE)
                .commit();
    }

    public void set(String result){
        clear();
        mContext
                .getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .putString(ClassData.PREFERENCE, result)
                .commit();
    }

    public void getByDay(){
        String result = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(ClassData.PREFERENCE, null);
        if (result == null){

        }
    }


}
