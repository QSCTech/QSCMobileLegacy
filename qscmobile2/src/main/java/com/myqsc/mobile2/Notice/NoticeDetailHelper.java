package com.myqsc.mobile2.Notice;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myqsc.mobile2.Notice.Fragment.NoticeImageHelper;
import com.myqsc.mobile2.R;
import com.myqsc.mobile2.uti.LogHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by richard on 13-10-9.
 */
public class NoticeDetailHelper {
    LinearLayout linearLayout = null;
    View shareItem = null;
    View openItem = null;

    public NoticeDetailHelper(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public void setShareItem(View v) {
        shareItem = v;
    }

    public void setOpenItem(View v) {
        openItem = v;
    }

    public void getEvent(final int id, final String highlight) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://notice.myqsc.com/events/" + id;

                    LogHelper.d(url);
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
                            final View noticeParentView = mInflater.inflate(R.layout.activity_notice_detail_inner, null);

                            ((TextView) noticeParentView.findViewById(R.id.notice_bar_name))
                                    .setText(structure.getEventItem("name"));
                            ((TextView) noticeParentView.findViewById(R.id.notice_bar_starttime))
                                    .setText("从 " + structure.getEventItem("start_time") + "\n" +
                                            "至 " + structure.getEventItem("end_time"));
                            ((TextView) noticeParentView.findViewById(R.id.notice_bar_place))
                                    .setText(structure.getEventItem("place"));
                            ((TextView) noticeParentView.findViewById(R.id.notice_bar_category))
                                    .setText(structure.getCategoryItem("name"));
                            ((TextView) noticeParentView.findViewById(R.id.notice_bar_sponsor))
                                    .setText(structure.getSponsorItem("showname"));
                            ((TextView) noticeParentView.findViewById(R.id.notice_bar_rating))
                                    .setText(structure.getEventItem("rating"));

                            noticeParentView.findViewById(R.id.notice_bar_pic)
                                    .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(linearLayout.getContext(), NoticeImageActivity.class);
                                            intent.putExtra("url", structure.getCoverPic());
                                            linearLayout.getContext()
                                                    .startActivity(intent);
                                            ((Activity) linearLayout.getContext())
                                                    .overridePendingTransition(R.anim.right_push_in, 0);
                                        }
                                    });

                            if (highlight == null) {
                                ((TextView) noticeParentView.findViewById(R.id.notice_bar_content))
                                        .setText(structure.getEventItem("description"));
                            } else {
                                int pos = 0;
                                String content = structure.getEventItem("description");
                                if (content == null)
                                    return;

                                Spannable spannable = new SpannableString(content);
                                ForegroundColorSpan span = new ForegroundColorSpan(
                                        linearLayout.getResources().getColor(R.color.notice_highlight));
                                while ((pos = content.indexOf(highlight, pos)) != -1) {
                                    spannable.setSpan(span, pos, pos + highlight.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                }
                                ((TextView) noticeParentView.findViewById(R.id.notice_bar_content))
                                        .setText(spannable);
                            }

                            ((TextView) noticeParentView.findViewById(R.id.notice_bar_tag))
                                    .setText(structure.getHotTagString());

                            linearLayout.removeAllViews();
                            linearLayout.addView(noticeParentView);
                            linearLayout.postInvalidate();

                            NoticeImageHelper.initPic(structure.getCoverPic(), (android.widget.ImageView) noticeParentView.findViewById(R.id.notice_bar_pic));

                            if (shareItem != null) {
                                shareItem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_SEND);
                                        intent.setType("text/plain");
                                        intent.putExtra(Intent.EXTRA_TEXT,
                                                "我在求是潮 Notice 中发现了一个活动： " +
                                                        structure.getEventItem("name") +
                                                        " " +
                                                        "http://notice.myqsc.com/#!/event/" + id
                                                        + " "
                                        );
                                        linearLayout.getContext()
                                                .startActivity(Intent.createChooser(intent, "分享 Notice 活动"));
                                    }
                                });
                            }

                            if (openItem != null) {
                                openItem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String url = "http://notice.myqsc.com/#!/event/" + id;
                                        Intent intent = new Intent();
                                        intent.setData(Uri.parse(url));
                                        linearLayout.getContext()
                                                .startActivity(Intent.createChooser(intent, "打开 Notice 活动网页"));
                                    }
                                });
                            }

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
