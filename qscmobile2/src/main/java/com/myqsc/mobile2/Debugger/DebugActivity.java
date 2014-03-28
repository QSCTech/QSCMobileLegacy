package com.myqsc.mobile2.Debugger;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.fragment.MySwipeExitActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by richard on 13-11-20.
 */
public class DebugActivity extends MySwipeExitActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        throw new NullPointerException("");
    }
}
