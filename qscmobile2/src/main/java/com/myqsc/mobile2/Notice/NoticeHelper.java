package com.myqsc.mobile2.Notice;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.AwesomeFontHelper;
import com.myqsc.mobile2.uti.LogHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 13-10-8.
 */
public class NoticeHelper {
    LinearLayout linearLayout = null;
    PullToRefreshScrollView scrollView = null;
    Context mContext = null;
    LayoutInflater mInflater = null;
    int pager = 1;

    public NoticeHelper(LinearLayout linearLayout, PullToRefreshScrollView scrollView, Context context){
        this.linearLayout = linearLayout;
        this.scrollView = scrollView;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void reset() {
        pager = 1;
    }

    public void getMore(final int type, final Handler handler) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://test.myqsc.com/notice/";
                switch (type) {
                    case NoticeActivity.SELECT_TINT:
                        url += "events";
                        break;
                    case NoticeActivity.SELECT_FIRE:
                        url += "events/hot";
                        break;
                }

                url += "?page=" + pager;
                ++pager;

                LogHelper.e(url);
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
                httpGet.setHeader("X-Need-Escape", "0");

                try {
                    HttpResponse response = httpClient.execute(httpGet);

                    String result = EntityUtils.toString(response.getEntity(), "UTF-8");

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (jsonObject.getInt("code") != 0 || jsonArray.length() == 0)
                        throw new RuntimeException("没有更多活动了");

                    final List<NoticeStructure> list = new ArrayList<NoticeStructure>();
                    for (int i = 0; i != jsonArray.length(); ++i) {
                        list.add(new NoticeStructure(jsonArray.getJSONObject(i)));
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            addView(list);
                            scrollView.onRefreshComplete();
                        }
                    });
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "木有更多活动啦！", Toast.LENGTH_LONG).show();
                            scrollView.onRefreshComplete();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "网络失败，请稍后重试", Toast.LENGTH_LONG).show();
                            scrollView.onRefreshComplete();
                        }
                    });
                }
            }
        });
        thread.start();
    }

    public void getSearchResult(final Handler handler, final String key) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Uri.parse("http://test.myqsc.com/notice/events")
                        .buildUpon()
                        .appendQueryParameter("page", String.valueOf(pager))
                        .appendQueryParameter("keyword", key)
                        .build().toString();
                ++pager;

                LogHelper.e(url);
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
                httpGet.setHeader("X-Need-Escape", "0");

                try {
                    HttpResponse response = httpClient.execute(httpGet);

                    String result = EntityUtils.toString(response.getEntity(), "UTF-8");

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (jsonObject.getInt("code") != 0 || jsonArray.length() == 0)
                        throw new RuntimeException("没有更多活动了");

                    final List<NoticeStructure> list = new ArrayList<NoticeStructure>();
                    for (int i = 0; i != jsonArray.length(); ++i) {
                        list.add(new NoticeStructure(jsonArray.getJSONObject(i)));
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (pager == 2) {
                                //说明这是第一次请求，就要把搜索框删掉！
                                linearLayout.removeAllViews();
                            }

                            addView(list);
                            scrollView.onRefreshComplete();
                        }
                    });
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "木有更多活动啦！", Toast.LENGTH_LONG).show();
                            scrollView.onRefreshComplete();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "网络失败，请稍后重试", Toast.LENGTH_LONG).show();
                            scrollView.onRefreshComplete();
                        }
                    });
                }
            }
        });
        thread.start();
    }

    private void addView(final List<NoticeStructure> list) {
        for (int i = 0; i != list.size(); ++i) {
            NoticeStructure structure = list.get(i);

            View view = mInflater.inflate(R.layout.notice_bar, null);
            linearLayout.addView(view);
            AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.notice_bar_icon), mContext);

            ((TextView) view.findViewById(R.id.notice_bar_name))
                    .setText(structure.getEventItem("name"));
            ((TextView) view.findViewById(R.id.notice_bar_starttime))
                    .setText(structure.getEventItem("start_time"));
            ((TextView) view.findViewById(R.id.notice_bar_stoptime))
                    .setText(structure.getEventItem("end_time"));
            ((TextView) view.findViewById(R.id.notice_bar_place))
                    .setText(structure.getEventItem("place"));

            ((TextView) view.findViewById(R.id.notice_bar_category))
                    .setText(structure.getCategoryItem("name"));

            ((TextView) view.findViewById(R.id.notice_bar_sponser))
                    .setText(structure.getSponsorItem("showname"));
        }
    }
}
