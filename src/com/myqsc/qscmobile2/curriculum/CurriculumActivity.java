package com.myqsc.qscmobile2.curriculum;

import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myqsc.qscmobile2.R;

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
		
		// 以下代码为测试用
		// TODO 完成正式代码
		List<CurriculumItem> list = new Vector<CurriculumItem>();
		list.add(new CurriculumItem("小计基", "撸汉权", "周日 0", "DiaoSiDao"));
		setCardList(cardList, list);
		
		list.add(0,new CurriculumItem("中计基", "撸撸", "周日 1/2", "DiaoSiDao"));
		setCardList(cardList, list);
	}
	
	private void setCardList(LinearLayout ll,List<CurriculumItem> items) {
		ll.removeAllViews();
		for (CurriculumItem item:items){
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






