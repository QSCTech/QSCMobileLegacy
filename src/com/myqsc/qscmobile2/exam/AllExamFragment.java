package com.myqsc.qscmobile2.exam;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.List;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.exam.uti.ExamAdapter;
import com.myqsc.qscmobile2.exam.uti.UpdateExamAsyncTask;
import com.myqsc.qscmobile2.login.UserSwitchActivity;
import com.myqsc.qscmobile2.support.database.structure.ExamStructure;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.AwesomeFontHelper;
import com.myqsc.qscmobile2.uti.ExamDataHelper;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.uti.PersonalDataHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "ValidFragment" })
public class AllExamFragment extends Fragment {
	Context mContext = null;
	ListView allExamListView = null;
	String term_arr[] = {"春", "夏", "秋", "冬"};
	String year_str ;
	int term_int = 0;
	ExamDataHelper examHelper = null;
	UserIDStructure structure = null;
	View view = null;
	
	public AllExamFragment(){
	}
	
	public AllExamFragment(Context context){
		mContext = context;
	}
	
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		year_str = String.valueOf(year) + "-" + String.valueOf(year + 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_all_exam, null);
		AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.fragment_all_exam_icon_left), mContext);
		AwesomeFontHelper.setFontFace((TextView) view.findViewById(R.id.fragment_all_exam_icon_right), mContext);
		
		view.findViewById(R.id.fragment_all_exam_icon_left).setOnClickListener(onClickListener);
		view.findViewById(R.id.fragment_all_exam_icon_right).setOnClickListener(onClickListener);
		
		allExamListView = (ListView) view.findViewById(R.id.activity_exam_term_list);
		
		examHelper = new ExamDataHelper(mContext);
		examHelper.setHandleAsyncTaskMessage(handleAsyncTaskMessage);

		PersonalDataHelper helper = new PersonalDataHelper(mContext);
		
		structure = helper.getCurrentUser();
		if (structure == null){
			Intent intent = new Intent();
			intent.setClass(mContext, UserSwitchActivity.class);
			startActivity(intent);
		}
		
		updateExamData();
		return view;
	}
	
	void updateExamData(){
		examHelper.getExamList(term_arr[term_int], structure.uid, structure.pwd);
		((TextView)view.findViewById(R.id.fragment_exam_all_term_textview)).setText(year_str + term_arr[term_int]); 
	}
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.fragment_all_exam_icon_left)
				--term_int;
			if (v.getId() == R.id.fragment_all_exam_icon_right)
				++term_int;
			term_int = (term_int + 4) % 4;
			LogHelper.d(term_arr[term_int]);
			updateExamData();
		}
	};
	
	HandleAsyncTaskMessage handleAsyncTaskMessage = new HandleAsyncTaskMessage() {
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
}
