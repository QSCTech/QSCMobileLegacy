package com.myqsc.mobile2.Notice;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.myqsc.mobile2.network.DataUpdater;
import com.myqsc.mobile2.network.UpdateHelper;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 13-10-13.
 */
public class NoticeCardHelper {
    Context mContext = null;
    List<NoticeStructure> list = null;

    public NoticeCardHelper(Context context) {
        this.mContext = context;

        String data = mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .getString(DataUpdater.COMMON_NOTICE, null);
        if (data == null)
            return;

        list = new ArrayList<NoticeStructure>();
        try {
            JSONArray jsonArray = new JSONObject(data).getJSONArray("data");
            for (int i = 0; i != 3; ++i) {
                NoticeStructure structure = new NoticeStructure(jsonArray.getJSONObject(i));
                list.add(structure);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<NoticeStructure> getData() {
        return list;
    }


}
