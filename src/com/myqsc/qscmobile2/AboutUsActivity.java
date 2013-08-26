package com.myqsc.qscmobile2;

import com.myqsc.qscmobile2.uti.AwesomeFontHelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutUsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		AwesomeFontHelper.setFontFace(((TextView) findViewById(R.id.fragment_aboutus_icon)), this);
	}
}
