package com.myqsc.qscmobile2.exam;

import java.util.List;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.exam.uti.ExamAdapter;
import com.myqsc.qscmobile2.exam.uti.UpdateExamAsyncTask;
import com.myqsc.qscmobile2.login.UserSwitchActivity;
import com.myqsc.qscmobile2.support.database.structure.ExamStructure;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.PersonalDataHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "ValidFragment" })
public class AllExamFragment extends Fragment {
	Context mContext = null;
	
	public AllExamFragment(){
	}
	
	public AllExamFragment(Context context){
		mContext = context;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_all_exam, null);
		
		
		final ListView allExamListView = (ListView) view.findViewById(R.id.activity_exam_term_list);
		
		PersonalDataHelper helper = new PersonalDataHelper(mContext);
		UserIDStructure structure = helper.getCurrentUser();
		if (structure == null){
			Intent intent = new Intent();
			intent.setClass(mContext, UserSwitchActivity.class);
			startActivity(intent);
		}
		
		final UpdateExamAsyncTask task = new UpdateExamAsyncTask(mContext, structure.uid, structure.pwd) {
			
			@Override
			public void onHandleMessage(Message message) {
				if (message.what == 0){
					Toast.makeText(mContext, (CharSequence) message.obj, Toast.LENGTH_LONG).show();
				} else {
					ExamAdapter adapter = new ExamAdapter((List<ExamStructure>) message.obj, mContext);
					allExamListView.setAdapter(adapter);
				}
			}
		};
		
		if (android.os.Build.VERSION.SDK_INT >= 15)
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			task.execute();
		return view;
	}
	
}
