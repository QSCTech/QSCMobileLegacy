package com.myqsc.mobile2.uti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.myqsc.mobile2.R;

import java.util.Random;

/**
 * Created by richard on 13-9-4.
 */
public class CustomViews {
    static Random random = null;
    public static View getDividerView(Context context) {
        if (random == null)
            random = new Random();
        View view = LayoutInflater.from(context).inflate(R.layout.diveder_white, null);
        view.setId(100 + random.nextInt());
        return view;
    }
}
