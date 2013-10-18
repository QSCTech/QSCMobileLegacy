package com.myqsc.mobile2.Notice;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by richard on 13-10-9.
 */
public class NoticeEventStructure {
    JSONObject event, cover, category, sponsor;
    JSONArray hotTag, picture;

    public NoticeEventStructure (JSONObject jsonObject) {
        try {
            event = jsonObject.getJSONObject("event");
            cover = jsonObject.getJSONObject("cover");
            category = jsonObject.getJSONObject("category");
            sponsor = jsonObject.getJSONObject("sponsor");
            picture = jsonObject.getJSONArray("picture");
            hotTag = jsonObject.getJSONArray("hottag");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEventItem(String key) {
        try {
            return event.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCategoryItem(String key) {
        try {
            return category.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSponsorItem(String key) {
        try {
            return sponsor.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHotTagString() {
        try {
            String tag = "";
            for (int i = 0; i != hotTag.length(); ++i) {
                JSONObject object = hotTag.getJSONObject(i);
                tag += object.getString("name") + ", ";
            }
            tag = tag.substring(0, tag.length() - 2);
            return tag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
