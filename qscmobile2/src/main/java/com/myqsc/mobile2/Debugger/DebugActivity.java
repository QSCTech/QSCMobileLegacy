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
    TextView textView = null;
    ScrollView scrollView = null;
    Thread thread = null;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        textView = (TextView) findViewById(R.id.debug_text);
        scrollView = (ScrollView) findViewById(R.id.debug_scroll);

        initLogs();
    }

    private void initLogs() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Process process = Runtime.getRuntime().exec("logcat");
                    final BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

                    while (true) {

                        final StringBuilder builder = new StringBuilder();
                        final Vector<String> lines = new Vector<String>();

                        final long time = System.currentTimeMillis();
                        while (true) {
                            final String line = bufferedReader.readLine();
                            if (line != null)
                                lines.add(line);

                            if (System.currentTimeMillis() - time > 1000)
                                break;
                        }

                        for (String string : lines) {
                            builder.append(string);
                        }
                        if (lines.size() == 0)
                            builder.append("NO Logs\n");

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.append(builder);
                                scrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }
}
