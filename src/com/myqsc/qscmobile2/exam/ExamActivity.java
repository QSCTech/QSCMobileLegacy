package com.myqsc.qscmobile2.exam;

import java.util.List;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.exam.uti.ExamAdapter;
import com.myqsc.qscmobile2.exam.uti.UpdateExamAsyncTask;
import com.myqsc.qscmobile2.login.UserSwitchActivity;
import com.myqsc.qscmobile2.support.database.structure.ExamStructure;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;
import com.myqsc.qscmobile2.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.uti.Utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ExamActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam);
		AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.exam_icon_left), this);
		AwesomeFontHelper.setFontFace((TextView) findViewById(R.id.exam_icon_right), this);
		final ListView allExamListView = (ListView) findViewById(R.id.activity_exam_term_list);
		
		PersonalDataHelper helper = new PersonalDataHelper(this);
		UserIDStructure structure = helper.getCurrentUser();
		if (structure == null){
			Intent intent = new Intent();
			intent.setClass(getBaseContext(), UserSwitchActivity.class);
			startActivity(intent);
			finish();
		}
		
		
		
		final UpdateExamAsyncTask task = new UpdateExamAsyncTask(this, structure.uid, structure.pwd) {
			
			@Override
			public void onHandleMessage(Message message) {
				if (message.what == 0){
					Toast.makeText(getBaseContext(), (CharSequence) message.obj, Toast.LENGTH_LONG).show();
				} else {
					ExamAdapter adapter = new ExamAdapter((List<ExamStructure>) message.obj, getBaseContext());
					allExamListView.setAdapter(adapter);
				}
			}
		};
		
		if (android.os.Build.VERSION.SDK_INT >= 15)
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			task.execute();
	}

}
