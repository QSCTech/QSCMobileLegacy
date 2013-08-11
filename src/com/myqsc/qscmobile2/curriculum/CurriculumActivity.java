package com.myqsc.qscmobile2.curriculum;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.R.layout;
import com.myqsc.qscmobile2.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageButton;

public class CurriculumActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curriculum);
		
		
		//ImageButton imageButton
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.curriculum, menu);
		return true;
	}

}
