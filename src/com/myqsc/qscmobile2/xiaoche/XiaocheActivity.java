package com.myqsc.qscmobile2.xiaoche;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class XiaocheActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiaoche);
		AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.activity_xiaoche_exchange), this);
	}
	
}
