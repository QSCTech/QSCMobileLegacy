package com.myqsc.qscmobile2.network;

import android.content.Context;
import android.os.Build;

import com.myqsc.qscmobile2.login.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.LogHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by richard on 13-9-2.
 */
public class DataUpdater {
    private static final String HTTP_PROTOCOL = "http://m.myqsc.com/api/v2/";

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
        name.put(COMMON_HASH, HTTP_PROTOCOL + COMMON_HASH);
        name.put(COMMON_TEACHER, HTTP_PROTOCOL + COMMON_TEACHER);
        name.put(COMMON_XIAOCHE, HTTP_PROTOCOL + COMMON_XIAOCHE);
        name.put(COMMON_XIAOLI, HTTP_PROTOCOL + COMMON_XIAOLI);
//        name.put(COMMON_SHIJIAN, HTTP_PROTOCOL + COMMON_SHIJIAN);
//        name.put(COMMON_SHIJIAN_DETAIL, HTTP_PROTOCOL + COMMON_SHIJIAN_DETAIL);

        name.put(JW_VALIDATE, HTTP_PROTOCOL + JW_VALIDATE);
        name.put(JW_HASH, HTTP_PROTOCOL + JW_HASH);
        name.put(JW_INFO, HTTP_PROTOCOL + JW_INFO);
        name.put(JW_KEBIAO, HTTP_PROTOCOL + JW_KEBIAO);
        name.put(JW_CHENGJI, HTTP_PROTOCOL + JW_CHENGJI);
        name.put(JW_KAOSHI, HTTP_PROTOCOL + JW_KAOSHI);
    }

    public DataUpdater() {
    }

    public static String update(String key, Context context) {
        String url = name.get(key);
        assert url != null;

        UserIDStructure structure = new PersonalDataHelper(context).getCurrentUser();
        assert structure != null;


        try {
            return get(url + "?stuid=" + URLEncoder.encode(structure.uid, "UTF-8") + "&pwd=" +
                    URLEncoder.encode(structure.pwd, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String get(String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            //使用支持 SNI 协议的urlconnection，走https
            StringBuilder result = new StringBuilder();
            try {
                url = url.replaceFirst("http", "https");
                LogHelper.d("URL:" + url);
                URL address = new URL(url);
                URLConnection urlConnection = address.openConnection();
                urlConnection.setRequestProperty("accept", "*/*");
                urlConnection.setConnectTimeout(15000); //15秒钟超时
                urlConnection.connect();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                urlConnection.getInputStream()));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();

        } else {
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
}
