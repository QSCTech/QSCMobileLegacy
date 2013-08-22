package com.myqsc.qscmobile2.uti;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.myqsc.qscmobile2.support.database.DatabaseHelper;
import com.myqsc.qscmobile2.support.database.structure.ExamStructure;
import com.myqsc.qscmobile2.support.database.table.ExamTable;

public class ExamDataHelper {
	DatabaseHelper helper = null;
	public ExamDataHelper(Context context) {
		helper = new DatabaseHelper(context);
	}
	
	public void clear(){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(ExamTable.TABLE_NAME, null, null);
		db.close();
	}
	
	public void add_exam(ExamStructure exam){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ExamTable.COURSE_NAME, exam.course_name);
		values.put(ExamTable.COURSE_NUM, exam.course_num);
		values.put(ExamTable.CREDIT, exam.credit);
		values.put(ExamTable.IS_REBUILD, exam.is_rebuild);
		values.put(ExamTable.POSITION, exam.position);
		values.put(ExamTable.SEAT, exam.seat);
		values.put(ExamTable.STU_NAME, exam.stu_name);
		values.put(ExamTable.TERM, exam.term);
		values.put(ExamTable.TIME, exam.time);
		
		db.insert(ExamTable.TABLE_NAME, null, values);
		db.close();
	}
}
