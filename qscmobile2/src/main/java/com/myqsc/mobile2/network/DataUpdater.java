package com.myqsc.mobile2.network;

import android.content.Context;

import com.myqsc.mobile2.login.uti.PersonalDataHelper;
import com.myqsc.mobile2.support.database.structure.UserIDStructure;
import com.myqsc.mobile2.uti.LogHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

/**
 * Created by richard on 13-9-2.
 */
public class DataUpdater {
    private static final String HTTP_PROTOCOL = "https://m.myqsc.com/api/v2/";

//    public final static String COMMON_HASH = "share/hash";
    public final static String COMMON_TEACHER = "share/teacher";
    public final static String COMMON_XIAOCHE = "share/xiaoche";
    public final static String COMMON_XIAOLI = "share/xiaoli";
//    public final static String COMMON_SHIJIAN = "share/shijian";
//    public final static String COMMON_SHIJIAN_DETAIL = "share/notice";

    public final static String JW_VALIDATE = "jw/validate";
//    public final static String JW_HASH = "jw/hash";
//    public final static String JW_INFO = "jw/stuinfo";
    public final static String JW_KEBIAO = "jw/kebiao";
    public final static String JW_CHENGJI = "jw/chengji";
    public final static String JW_KAOSHI = "jw/kaoshi";

    public final static String HOMEWORK = "404";

    public final static String COMMON_NOTICE = "notice/events/hot";

    public final static Map<String, String> name = new HashMap<String, String>();
    static{
//        name.put(COMMON_HASH, HTTP_PROTOCOL + COMMON_HASH);
        name.put(COMMON_TEACHER, HTTP_PROTOCOL + COMMON_TEACHER);
        name.put(COMMON_XIAOCHE, HTTP_PROTOCOL + COMMON_XIAOCHE);
        name.put(COMMON_XIAOLI, HTTP_PROTOCOL + COMMON_XIAOLI);
//        name.put(COMMON_SHIJIAN, HTTP_PROTOCOL + COMMON_SHIJIAN);
//        name.put(COMMON_SHIJIAN_DETAIL, HTTP_PROTOCOL + COMMON_SHIJIAN_DETAIL);

        name.put(JW_VALIDATE, HTTP_PROTOCOL + JW_VALIDATE);
//        name.put(JW_HASH, HTTP_PROTOCOL + JW_HASH);
//        name.put(JW_INFO, HTTP_PROTOCOL + JW_INFO);
        name.put(JW_KEBIAO, HTTP_PROTOCOL + JW_KEBIAO);
        name.put(JW_CHENGJI, HTTP_PROTOCOL + JW_CHENGJI);
        name.put(JW_KAOSHI, HTTP_PROTOCOL + JW_KAOSHI);

        name.put(COMMON_NOTICE, "http://notice.myqsc.com/events/hot");
    }

    public DataUpdater() {
    }

    public static String update(String key, Context context) {
        String url = name.get(key);
        assert url != null;

        UserIDStructure structure = new PersonalDataHelper(context).getCurrentUser();
        if (structure == null)
            return null;

        try {
            return get(url + "?stuid=" + URLEncoder.encode(structure.uid, "UTF-8") + "&pwd=" +
                    URLEncoder.encode(structure.pwd, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String get(String url) {
        StringBuilder result = new StringBuilder();
        try {
            LogHelper.d("URL:" + url);
            URL address = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) address.openConnection();
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            urlConnection.setRequestProperty("X-Need-Escape", "0");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip");
            urlConnection.setConnectTimeout(15000); //15秒钟超时
            urlConnection.setReadTimeout(10000); //10s 下载超时
            urlConnection.connect();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            urlConnection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
        } catch (SSLException e){
            //ssl 证书失败
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
