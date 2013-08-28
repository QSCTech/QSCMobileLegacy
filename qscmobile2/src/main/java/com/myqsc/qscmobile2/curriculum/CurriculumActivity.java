package com.myqsc.qscmobile2.curriculum;

import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myqsc.qscmobile2.R;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;

public class CurriculumActivity extends Activity {

	LinearLayout cardList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curriculum);
		
		/*
		ImageButton imageButtonEveryday = (ImageButton) findViewById(R.id.curriculum_everyday);
		ImageButton imageButtonAllweek = (ImageButton) findViewById(R.id.curriculum_allweek);
		
		ImageButton imageButtonleft = (ImageButton)
		*/
		
		cardList = (LinearLayout) findViewById(R.id.curriculum_cards);
		
		new FetchCurriculum(new UserIDStructure("1", "1", 0)) {
			@Override
			protected void onPostExecute(Message msg) {
				if (msg.what!=0) {
					// TODO 通知错误信息
				} else {
					List<CourseData> courses = (List<CourseData>)msg.obj;
					
					int dayOfWeek = (new GregorianCalendar().get(GregorianCalendar.DAY_OF_WEEK)+5)%7+1;
					
					new ClassFilter(dayOfWeek) {
						@Override
						protected void onPostExecute(List<ClassData> classes) {
							setCardList(cardList, classes);
						}
					}.execute(courses);
				}
			}
		}.execute(-1);
		
		// 以上代码为测试用
		// TODO 完成正式代码
	}
	
	private void setCardList(LinearLayout ll,List<ClassData> items) {
		ll.removeAllViews();
		for (ClassData item:items){
			View view = LayoutInflater.from(this).inflate(R.layout.curriculum_item, null);
			TextView name = (TextView)view.findViewById(R.id.curriculum_name);
			TextView teacher = (TextView)view.findViewById(R.id.curriculum_teacher);
			TextView time = (TextView)view.findViewById(R.id.curriculum_time);
			TextView place = (TextView)view.findViewById(R.id.curriculum_place);
			name.setText(item.name);
			teacher.setText(item.teacher);
			time.setText(item.time);
			place.setText(item.place);
			ll.addView(view);
		}
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.curriculum, menu);
		return true;
	}

}






