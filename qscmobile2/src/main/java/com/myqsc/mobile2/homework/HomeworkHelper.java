package com.myqsc.mobile2.homework;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.mobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.mobile2.login.uti.PersonalDataHelper;
import com.myqsc.mobile2.support.database.structure.UserIDStructure;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

/**
 * Created by richard on 13-11-19.
 */
public class HomeworkHelper {
    public static final String HOMEWORK_RAW = "HOMEWORK_RAW";

    Vector<HomeworkStructure> rawVector = new Vector<HomeworkStructure>();
    Vector<HomeworkStructure> mineVector = new Vector<HomeworkStructure>();
    List<KebiaoClassData> kebiaoList = null;

    Context context = null;
    LinearLayout linearLayout = null;

    public HomeworkHelper (Context context, LinearLayout linearLayout) {
        this.context = context;
        this.linearLayout = linearLayout;

        KebiaoDataHelper kebiaoDataHelper = new KebiaoDataHelper(context);
        kebiaoList = kebiaoDataHelper.getTerm(Calendar.getInstance());
    }

    public void getAllHomework () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://m.myqsc.com/api/v2/homework/get");

                    PersonalDataHelper personalDataHelper = new PersonalDataHelper(context);
                    UserIDStructure userIDStructure = personalDataHelper.getCurrentUser();

                    List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    postParameters.add(new BasicNameValuePair("stuid", userIDStructure.uid));
                    postParameters.add(new BasicNameValuePair("pwd", userIDStructure.pwd));

                    //获取所有课程的hash值
                    JSONArray jsonArray = new JSONArray(getCourseHashList());
                    postParameters.add(new BasicNameValuePair("hash", jsonArray.toString()));

                    UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(postParameters);
                    httpPost.setEntity(encodedFormEntity);
                    LogHelper.e(httpPost.getParams().toString());

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    String result = EntityUtils.toString(httpResponse.getEntity());
                    LogHelper.e(result);

                    JSONArray homeworkArray = new JSONArray(result);
                    rawVector = parseList(homeworkArray);

                    linearLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            //初始化作业列表
                            initViews(rawVector);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initViews (Vector<HomeworkStructure> vector) {
        final LayoutInflater mInflater = LayoutInflater.from(context);
        final SharedPreferences preferences = context.getSharedPreferences(Utility.PREFERENCE, 0);

        linearLayout.removeAllViews();

        for(HomeworkStructure structure : vector) {
            LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.homework_list_layout, null);
            if (structure.isSelected(preferences)) {
                //已选择
                ((TextView) layout.findViewById(R.id.homework_list_selected))
                        .setText("从我的作业中移除");
            } else {
                //没有选择，点击添加
                ((TextView) layout.findViewById(R.id.homework_list_selected))
                        .setText("添加到我的作业");
            }

            ((TextView) layout.findViewById(R.id.homework_list_title))
                    .setText(getCourseNameByHash(structure.hash));
            ((TextView) layout.findViewById(R.id.homework_list_content))
                    .setText(structure.content);
            ((TextView) layout.findViewById(R.id.homework_list_time))
                    .setText("截止日期：" + structure.due_time);
            linearLayout.addView(layout);


        }
    }

    private Vector<HomeworkStructure> parseList (JSONArray jsonArray) {
        Vector<HomeworkStructure> list = new Vector<HomeworkStructure>();
        for (int i = 0; i != jsonArray.length(); ++i) {
            try {
                HomeworkStructure structure = new HomeworkStructure(jsonArray.getJSONObject(i));
                list.add(structure);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 通过hash值获取课程名称
     * @param hash
     * @return
     */
    private String getCourseNameByHash(String hash) {
        for (KebiaoClassData kebiaoClassData : kebiaoList) {
            if (kebiaoClassData.hash.equals(hash))
                return kebiaoClassData.name;
        }
        return null;
    }

    /**
     * 获取所有课程的Hash值
     * @return
     */
    private List<String> getCourseHashList () {
        List<String> list = new ArrayList<String>();
        for (KebiaoClassData classData : kebiaoList) {
            list.add(classData.hash);
        }
        return list;
    }


}
