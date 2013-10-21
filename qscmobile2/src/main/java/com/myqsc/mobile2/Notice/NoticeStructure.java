package com.myqsc.mobile2.Notice;

import org.json.JSONObject;

/**
 * Created by richard on 13-10-8.
 */
public class NoticeStructure {
    JSONObject event;
    JSONObject category;
    JSONObject sponsor;

    public NoticeStructure(JSONObject jsonObject) {
        try {
            this.event = jsonObject.getJSONObject("event");
            this.category = jsonObject.getJSONObject("category");
            this.sponsor = jsonObject.getJSONObject("sponsor");
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
}
