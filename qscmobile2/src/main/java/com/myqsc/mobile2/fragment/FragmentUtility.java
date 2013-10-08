package com.myqsc.mobile2.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.myqsc.mobile2.Notice.Fragment.NoticeCardFragment;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.Grade.Fragment.ChengjiCardFragment;
import com.myqsc.mobile2.curriculum.fragment.KebiaoCardEmptyFragment;
import com.myqsc.mobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.mobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.mobile2.curriculum.uti.KebiaoUtility;
import com.myqsc.mobile2.exam.fragment.ExamCardFragment;
import com.myqsc.mobile2.exam.fragment.ExamCardNoDataFragment;
import com.myqsc.mobile2.exam.uti.ExamDataHelper;
import com.myqsc.mobile2.huodong.fragment.HuodongCardFragment;
import com.myqsc.mobile2.curriculum.fragment.KebiaoCardFragment;
import com.myqsc.mobile2.network.DataUpdater;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.xiaoche.fragment.XiaocheCardFragment;

import java.util.Calendar;
import java.util.List;


public class FragmentUtility {
    public static String cardString[] = new String[]{
            "实时课表", "查考试", "查成绩", "查校车", "近期热门活动"
    };
    public static String cardDataString[] = new String[] {
            DataUpdater.JW_KEBIAO, DataUpdater.JW_KAOSHI,
            DataUpdater.JW_CHENGJI, DataUpdater.COMMON_XIAOCHE,
            DataUpdater.COMMON_NOTICE
    };

    public static int cardIcon[] = new int[]{
            R.string.icon_calendar,
            R.string.icon_copy,
            R.string.icon_trophy,
            R.string.icon_truck,
            R.string.icon_group
    };

    public static Fragment getCardFragmentByName(String name, Context context) {
        LogHelper.d(name + " fragment Inited");
        if (name.compareTo("实时课表") == 0) {
            KebiaoDataHelper helper = new KebiaoDataHelper(context);
            List<KebiaoClassData> list = helper.getDay(Calendar.getInstance());
            if (KebiaoUtility.getDiffTime(Calendar.getInstance(), list) == null)
                return new KebiaoCardEmptyFragment();
            else
                return new KebiaoCardFragment();
        }
        if (name.compareTo("查考试") == 0) {
            ExamDataHelper helper = new ExamDataHelper(context);
            if (helper.getCardExamStructure(Calendar.getInstance()) == null)
                return new ExamCardNoDataFragment();
            else
                return new ExamCardFragment();
        }
        if (name.compareTo("查成绩") == 0)
            return new ChengjiCardFragment();
        if (name.compareTo("查校车") == 0)
            return new XiaocheCardFragment();
        if (name.compareTo("近期热门活动") == 0)
            return new NoticeCardFragment();
        LogHelper.d("none fragment");
        return null;
    }
}
