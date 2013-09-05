package com.myqsc.qscmobile2.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.chengji.fragment.ChengjiCardFragment;
import com.myqsc.qscmobile2.curriculum.fragment.KebiaoCardEmptyFragment;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.qscmobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.qscmobile2.curriculum.uti.Utility;
import com.myqsc.qscmobile2.exam.fragment.ExamCardFragment;
import com.myqsc.qscmobile2.huodong.fragment.HuodongCardFragment;
import com.myqsc.qscmobile2.curriculum.fragment.KebiaoCardFragment;
import com.myqsc.qscmobile2.uti.LogHelper;

import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class FragmentUtility {
	public static String cardString[] = new String[] {
		"近期热门活动", "实时课表", "查考试", "查成绩"
	};
	
	public static int cardIcon[] = new int[] {
		R.string.icon_group,
		R.string.icon_calendar,
		R.string.icon_copy,
		R.string.icon_trophy
	};
	
	public static Fragment getCardFragmentByName(String name, Context context){
		if (name.compareTo(cardString[0]) == 0)
			return new HuodongCardFragment();
		if (name.compareTo(cardString[1]) == 0) {
            KebiaoDataHelper helper = new KebiaoDataHelper(context);
            List<KebiaoClassData> list = helper.getDay(Calendar.getInstance());
            if (Utility.getDiffTime(Calendar.getInstance(), list) == null)
                return new KebiaoCardEmptyFragment();
            else
                return new KebiaoCardFragment();
        }
		if (name.compareTo(cardString[2]) == 0)
			return new ExamCardFragment();
		if (name.compareTo(cardString[3]) == 0)
			return new ChengjiCardFragment();
		LogHelper.d("none fragment");
		return null;
	}
}
