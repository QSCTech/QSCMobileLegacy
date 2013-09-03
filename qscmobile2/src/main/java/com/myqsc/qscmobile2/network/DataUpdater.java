package com.myqsc.qscmobile2.network;

import android.content.Context;

import com.myqsc.qscmobile2.login.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by richard on 13-9-2.
 */
public class DataUpdater {
    private static final String PROTOCOL = "http://m.myqsc.com/v2/";

    public final static String COMMON_HASH = "share/hash";
    public final static String COMMON_TEACHER = "share/teacher";
    public final static String COMMON_XIAOCHE = "share/xiaoche";
    public final static String COMMON_XIAOLI = "share/xiaoli";
//    public final static String COMMON_SHIJIAN = "share/shijian";
//    public final static String COMMON_SHIJIAN_DETAIL = "share/notice";

    public final static String JW_VALIDATE = "jw/validate";
    public final static String JW_HASH = "jw/hash";
    public final static String JW_INFO = "jw/stuinfo";
    public final static String JW_KEBIAO = "jw/kebiao";
    public final static String JW_CHENGJI = "jw/chengji";
    public final static String JW_KAOSHI = "jw/kaoshi";

    public final static Map<String, String> name = new HashMap<String, String>();
    static{
        name.put(COMMON_HASH, PROTOCOL + COMMON_HASH);
        name.put(COMMON_TEACHER, PROTOCOL + COMMON_TEACHER);
        name.put(COMMON_XIAOCHE, PROTOCOL + COMMON_XIAOCHE);
        name.put(COMMON_XIAOLI, PROTOCOL + COMMON_XIAOLI);
//        name.put(COMMON_SHIJIAN, PROTOCOL + COMMON_SHIJIAN);
//        name.put(COMMON_SHIJIAN_DETAIL, PROTOCOL + COMMON_SHIJIAN_DETAIL);

        name.put(JW_VALIDATE, PROTOCOL + JW_VALIDATE);
        name.put(JW_HASH, PROTOCOL + JW_HASH);
        name.put(JW_INFO, PROTOCOL + JW_INFO);
        name.put(JW_KEBIAO, PROTOCOL + JW_KEBIAO);
        name.put(JW_CHENGJI, PROTOCOL + JW_CHENGJI);
        name.put(JW_KAOSHI, PROTOCOL + JW_KAOSHI);
    }

    public DataUpdater() {
    }

    public static String update(String key, Context context) {
        String url = name.get(key);
        assert url != null;

        UserIDStructure structure = new PersonalDataHelper(context).getCurrentUser();
        assert structure != null;


        return get(url + "?stuid" + structure.uid + "&pwd" + structure.pwd);
    }

    public static String get(String url) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
