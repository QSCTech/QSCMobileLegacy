package com.myqsc.mobile2.Notice;

import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.LogHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by richard on 13-10-9.
 */
public class NoticeDetailHelper {
    LinearLayout linearLayout = null;
    public NoticeDetailHelper(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public void getEvent(final int id) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://test.myqsc.com/notice/events/" + id;

                    LogHelper.e(url);
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url);

                    httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
                    httpGet.setHeader("X-Need-Escape", "0");

                    HttpResponse response = httpClient.execute(httpGet);
                    String result = EntityUtils.toString(response.getEntity(), "UTF-8");

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("code") != 0)
                        throw new RuntimeException("活动不存在或发生其他错误");

                    final NoticeEventStructure structure =
                            new NoticeEventStructure(jsonObject.getJSONObject("data"));

                    linearLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            final LayoutInflater mInflater = LayoutInflater.from(linearLayout.getContext());
                            final RelativeLayout relativeLayout = (RelativeLayout) mInflater.inflate(R.layout.activity_notice_detail_inner, null);

                            ((TextView) relativeLayout.findViewById(R.id.notice_bar_name))
                                    .setText(structure.getEventItem("name"));
                            ((TextView) relativeLayout.findViewById(R.id.notice_bar_starttime))
                                    .setText(structure.getEventItem("start_time"));
                            ((TextView) relativeLayout.findViewById(R.id.notice_bar_stoptime))
                                    .setText(structure.getEventItem("end_time"));
                            ((TextView) relativeLayout.findViewById(R.id.notice_bar_place))
                                    .setText(structure.getEventItem("place"));
                            ((TextView) relativeLayout.findViewById(R.id.notice_bar_category))
                                    .setText(structure.getCategoryItem("name"));
                            ((TextView) relativeLayout.findViewById(R.id.notice_bar_sponsor))
                                    .setText(structure.getSponsorItem("showname"));
                            ((TextView) relativeLayout.findViewById(R.id.notice_bar_rating))
                                    .setText(structure.getEventItem("rating"));

                            ((TextView) relativeLayout.findViewById(R.id.notice_bar_content))
                                    .setText(structure.getEventItem("description"));

                            linearLayout.removeAllViews();
                            linearLayout.addView(relativeLayout);
                        }
                    });

                } catch (RuntimeException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
