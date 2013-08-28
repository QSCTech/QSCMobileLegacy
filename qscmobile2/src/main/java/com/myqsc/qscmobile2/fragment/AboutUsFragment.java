package com.myqsc.qscmobile2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class AboutUsFragment extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about_us);
        AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.fragment_aboutus_icon), this);
    }
}
