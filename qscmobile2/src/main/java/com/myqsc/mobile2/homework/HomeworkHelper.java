package com.myqsc.mobile2.homework;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.curriculum.uti.KebiaoClassData;
import com.myqsc.mobile2.curriculum.uti.KebiaoDataHelper;
import com.myqsc.mobile2.login.uti.PersonalDataHelper;
import com.myqsc.mobile2.support.database.structure.UserIDStructure;
import com.myqsc.mobile2.uti.LogHelper;
import com.myqsc.mobile2.uti.Utility;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

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
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by richard on 13-11-19.
 */
public class HomeworkHelper {
    public static final String HOMEWORK_RAW = "HOMEWORK_RAW";
    public static final String HOMEWORK_MINE = "HOMEWORK_MINE";


    Vector<HomeworkStructure> rawVector = null;
    List<KebiaoClassData> kebiaoList = null;

    Context context = null;
    LinearLayout linearLayout = null;

    public HomeworkHelper (Context context, LinearLayout linearLayout) {
        this.context = context;
        this.linearLayout = linearLayout;

        KebiaoDataHelper kebiaoDataHelper = new KebiaoDataHelper(context);
        kebiaoList = kebiaoDataHelper.getTerm(Calendar.getInstance());
        try {
            rawVector = parseList(
                    new JSONArray(
                            context.getSharedPreferences(Utility.PREFERENCE, 0)
                                    .getString(HOMEWORK_RAW, null)
                    )

            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以同步的方式获取作业列表，并写入preference
     */
    private void syncGetHomeworkList() {
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
            LogHelper.d(httpPost.getParams().toString());

            HttpResponse httpResponse = httpClient.execute(httpPost);

            String result = EntityUtils.toString(httpResponse.getEntity());
            LogHelper.e(result);

            JSONArray homeworkArray = new JSONArray(result);
            rawVector = parseList(homeworkArray);

            Collections.sort(rawVector, new Comparator<HomeworkStructure>() {
                @Override
                public int compare(HomeworkStructure homeworkStructure, HomeworkStructure homeworkStructure2) {
                    return -(int) (homeworkStructure.assign_time - homeworkStructure2.assign_time);
                }
            });

            context.getSharedPreferences(Utility.PREFERENCE, 0)
                    .edit()
                    .putString(HOMEWORK_RAW, result)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAllHomework () {
        try {
            linearLayout.removeAllViews();
            View loadingView = LayoutInflater.from(context).inflate(R.layout.loading, null);
            linearLayout.addView(loadingView);
            ((AnimationDrawable) loadingView.findViewById(R.id.loading_image)
                    .getBackground()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    syncGetHomeworkList();
                    linearLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            //初始化作业列表
                            initViews(rawVector, false);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 初始化我的作业列表，必须在UI线程调用
     */
    public void getMineHomework () {
        if (rawVector == null) {
            return;
        }
        final SharedPreferences preferences = context.getSharedPreferences(Utility.PREFERENCE, 0);
        final Vector<HomeworkStructure> mineHomeworkVector = new Vector<HomeworkStructure>();

        for (HomeworkStructure structure : rawVector) {
            if (structure.isSelected(preferences)) {
                mineHomeworkVector.add(structure);
            }
        }

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Collections.sort(mineHomeworkVector, new Comparator<HomeworkStructure>() {
            @Override
            public int compare(HomeworkStructure homeworkStructure, HomeworkStructure homeworkStructure2) {
                try {
                    return -format.parse(homeworkStructure.due_time).compareTo(
                            format.parse(homeworkStructure2.due_time)
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        initViews(mineHomeworkVector, true);
    }

    /**
     * 根据vector初始化列表，必须在UI线程调用
     * @param vector
     * @param isMine
     */
    private void initViews (Vector<HomeworkStructure> vector, boolean isMine) {
        final LayoutInflater mInflater = LayoutInflater.from(context);
        final SharedPreferences preferences = context.getSharedPreferences(Utility.PREFERENCE, 0);

        linearLayout.removeAllViews();

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        for(HomeworkStructure structure : vector) {
            LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.homework_list_layout, null);
            changeSelectText((TextView) layout.findViewById(R.id.homework_list_selected),
                    structure.isSelected(preferences));

            if (isMine) {
                layout.findViewById(R.id.homework_list_selected)
                        .setOnClickListener(onMineHomeworkCancleListener);
            } else {
                layout.findViewById(R.id.homework_list_selected)
                        .setOnClickListener(toggleSelectStateListener);
            }

            try {
                Date structureDate = format.parse(structure.due_time);
                long diff = structureDate.getTime() - date.getTime();
                int day = (int) (diff / 1000 / 3600 / 24);
                if (day >= 0) {
                    ((TextView) layout.findViewById(R.id.homework_list_left))
                            .setText(String.format("剩余 %d天", day));
                } else {
                    ((TextView) layout.findViewById(R.id.homework_list_left))
                            .setText(String.format("逾期 %d天", -day));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            layout.findViewById(R.id.homework_list_selected)
                    .setTag(structure);

            ((TextView) layout.findViewById(R.id.homework_list_title))
                    .setText(getCourseNameByHash(structure.hash));
            ((TextView) layout.findViewById(R.id.homework_list_content))
                    .setText(structure.content);
            ((TextView) layout.findViewById(R.id.homework_list_time))
                    .setText("截止日期：" + structure.due_time);


            linearLayout.addView(layout);
        }
    }

    /**
     * 从我的作业中移除的监听器
     */
    final View.OnClickListener onMineHomeworkCancleListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences preferences = view.getContext()
                    .getSharedPreferences(Utility.PREFERENCE, 0);
            HomeworkStructure structure = (HomeworkStructure) view.getTag();
            changeSelectText((TextView) view, structure.toggleSelect(preferences));

            final LinearLayout parentView = ((LinearLayout) view.getParent().getParent().getParent());
            parentView.setVisibility(View.GONE);
        }
    };

    final View.OnClickListener toggleSelectStateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences preferences = view.getContext()
                    .getSharedPreferences(Utility.PREFERENCE, 0);
            HomeworkStructure structure = (HomeworkStructure) view.getTag();
            changeSelectText((TextView) view, structure.toggleSelect(preferences));
        }
    };

    private void changeSelectText(TextView textView, boolean select) {
        if (select) {
            textView.setText("从我的作业中移除");
        } else {
            textView.setText("添加到我的作业");
        }
    }

    TextView add_content;
    int add_year = -1, add_month = -1, add_day = -1;
    Spinner add_course;

    public void initAddViews () {
        final LayoutInflater mInflater = LayoutInflater.from(context);
        final LinearLayout addView = (LinearLayout) mInflater.inflate(R.layout.homework_view_add, null);

        add_content = (TextView) addView.findViewById(R.id.homework_add_content);

        final Spinner spinner = (Spinner) addView.findViewById(R.id.homework_add_spinner);
        add_course = spinner;

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item,
                getHomeworkName());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                ((EditText) addView.findViewById(R.id.homework_add_date))
                        .setText(String.format("%04d年 %02d月 %02d日", i, i2, i3));
                add_year = i;
                add_month = i2;
                add_day = i3;
            }
        };

        addView.findViewById(R.id.homework_add_date)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogHelper.e("homework_add view clicked");
                        Calendar calendar = Calendar.getInstance();
                        new DatePickerDialog(context, onDateSetListener,
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH))
                                .show();
                    }
                });

        addView.findViewById(R.id.homework_add_submit)
                .setOnClickListener(onSubmitHomeworkListener);

        linearLayout.removeAllViews();
        linearLayout.addView(addView);
    }

    /**
     * 点击监听器，提交作业用的
     */
    final View.OnClickListener onSubmitHomeworkListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (add_content.getText().length() < 1) {
                Toast.makeText(context, "作业内容长度不得为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (add_year < 0 || add_month < 0 || add_day < 0) {
                Toast.makeText(context, "请选择截止日期", Toast.LENGTH_SHORT).show();
                return;
            }
            linearLayout.post(new Runnable() {
                @Override
                public void run() {
                    startSubmitHomework();
                }
            });
        }
    };

    ProgressDialog alertDialog = null;
    /**
     * 开始提交一个作业
     */
    private void startSubmitHomework () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                linearLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog = new ProgressDialog(context);
                        alertDialog.setMessage("正在添加作业");
                        alertDialog.setTitle("请稍后");
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                });

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://m.myqsc.com/api/v2/homework/add");

                    PersonalDataHelper personalDataHelper = new PersonalDataHelper(context);
                    UserIDStructure userIDStructure = personalDataHelper.getCurrentUser();

                    List<NameValuePair> postParmas = new ArrayList<NameValuePair>();
                    postParmas.add(new BasicNameValuePair("stuid", userIDStructure.uid));
                    postParmas.add(new BasicNameValuePair("pwd", userIDStructure.pwd));

                    KebiaoClassData kebiaoClassData = kebiaoList.get(add_course.getSelectedItemPosition());
                    postParmas.add(new BasicNameValuePair("hash", kebiaoClassData.hash));
                    postParmas.add(new BasicNameValuePair("content", add_content.getText().toString()));

                    add_month ++;
                    postParmas.add(new BasicNameValuePair("due_time", String.format("%04d-%02d-%02d", add_year, add_month, add_day)));

                    httpPost.setEntity(new UrlEncodedFormEntity(postParmas, "UTF-8"));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));

                    final String error = jsonObject.optString("msg", null);
                    if (error != null) {
                        //有错误
                        linearLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                        });

                        return ;
                    }

                    String success = jsonObject.optString("success", null);
                    if (success == null) {
                        //有未知错误
                        linearLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "发生未知错误，可能是网络连接不稳定", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    SharedPreferences preferences = context.getSharedPreferences(Utility.PREFERENCE, 0);
                    HomeworkStructure homeworkStructure = new HomeworkStructure(success);
                    syncGetHomeworkList();
                    homeworkStructure.toggleSelect(preferences);

                    linearLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "作业添加成功", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    linearLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                        }
                    });

                }
            }
        }).start();
    }

    private Vector<String> getHomeworkName() {
        Vector<String> vector = new Vector<String>();
        for (KebiaoClassData structure : kebiaoList)
            vector.add(structure.name);
        return vector;
    }

    /**
     * 解析本地的作业列表
     * @param jsonArray
     * @return
     */
    private Vector<HomeworkStructure> parseList (JSONArray jsonArray) {
        Vector<HomeworkStructure> list = new Vector<HomeworkStructure>();

        for (int i = 0; i != jsonArray.length(); ++i) {
            try {
                boolean flag = false;
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

    /**
     * 获取我标记过了的作业的数量
     * @return
     */
    public int getMineHomeworkCount () {
        if (rawVector == null)
            return 0;

        int times = 0;
        final SharedPreferences preferences = context.getSharedPreferences(Utility.PREFERENCE, 0);
        for (HomeworkStructure structure : rawVector)
            if (structure.isSelected(preferences))
                ++times;
        return times;
    }
}
