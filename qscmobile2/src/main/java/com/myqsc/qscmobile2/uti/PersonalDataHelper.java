package com.myqsc.qscmobile2.uti;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.support.database.DatabaseHelper;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.support.database.table.UserIDTable;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PersonalDataHelper {
	DatabaseHelper helper = null;
	public PersonalDataHelper(Context context){
		helper = new DatabaseHelper(context); 
	}
	
	private void addUser(String uid, String pwd){
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + UserIDTable.TABLE_NAME 
				+ " WHERE " 
				+ UserIDTable.UID + "= ?;", new String[] {uid});
		if (cursor.getCount() != 0){
			db.delete(UserIDTable.TABLE_NAME, "uid=?", new String[] {uid});
			LogHelper.i("Delete From db:" + uid);
		}
		cursor.close();
		
		ContentValues values = new ContentValues();
		values.put(UserIDTable.UID, uid);
		values.put(UserIDTable.PWD, pwd);
		values.put(UserIDTable.SELECTION, 1);
		
		LogHelper.i("Insert INTO db:" + uid);
		db.insert(UserIDTable.TABLE_NAME, null, values);
		db.close();
	}
	
	public void addUser(String uid, String pwd, boolean selection){
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("UPDATE " + UserIDTable.TABLE_NAME 
				+ " SET "
				+ UserIDTable.SELECTION + "=0", null);
		cursor.close();
		db.close();
		addUser(uid, pwd);
		
	}
	
	public List<UserIDStructure> allUser(){
		List<UserIDStructure> userList = new ArrayList<UserIDStructure>();
		
//		for (long i = 3120000000L; i != 3120000010L; ++i)
//			addUser(String.valueOf(i), String.valueOf(i));
		SQLiteDatabase db = helper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM " + UserIDTable.TABLE_NAME, null);
		while (cursor.moveToNext()) {
			UserIDStructure userIDStructure = new UserIDStructure();
			userIDStructure.uid = cursor.getString(cursor.getColumnIndex(UserIDTable.UID));
			userIDStructure.pwd = cursor.getString(cursor.getColumnIndex(UserIDTable.PWD));
			userIDStructure.select = cursor.getInt(cursor.getColumnIndex(UserIDTable.SELECTION));
			
			userList.add(userIDStructure);
			LogHelper.i(userIDStructure.uid + " " + userIDStructure.select);
		}
		
		cursor.close();
		db.close();
		return userList;
	}
	
	public void deleteUser(String uid){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(UserIDTable.TABLE_NAME, UserIDTable.UID + "=?", new String[] {uid});
		db.close();
	}
	
	public void selectUser(String uid){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(UserIDTable.SELECTION, 0);
		db.update(UserIDTable.TABLE_NAME, values, null, null);
		values.clear();
		
		values.put(UserIDTable.SELECTION, 1);
		int len = db.update(UserIDTable.TABLE_NAME, values, UserIDTable.UID + "=?", new String[] {uid});
		LogHelper.d(String.valueOf(len));
		db.close();
	}
	
	public UserIDStructure getCurrentUser(){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(UserIDTable.TABLE_NAME, 
				new String[] {UserIDTable.UID, UserIDTable.PWD}, 
				UserIDTable.SELECTION + "=1", 
				null, null, null, null);
		if (cursor.moveToFirst()){
			UserIDStructure userIDStructure = new UserIDStructure();
			userIDStructure.uid = cursor.getString(cursor.getColumnIndex(UserIDTable.UID));
			userIDStructure.pwd = cursor.getString(cursor.getColumnIndex(UserIDTable.PWD));
			return userIDStructure;
		} else {
			return null;
		}
	}
}
