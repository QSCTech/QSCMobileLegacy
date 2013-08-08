package com.myqsc.qscmobile2.uti;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.support.database.UserIDDatabaseHelper;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.support.database.table.UserIDTable;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PersonalDataHelper {
	UserIDDatabaseHelper helper = null;
	public PersonalDataHelper(Context context){
		helper = new UserIDDatabaseHelper(context); 
	}
	
	public void addUser(String uid, String pwd){
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
		values.put(UserIDTable.SELECTION, 0);
		
		LogHelper.i("Insert INTO db:" + uid);
		db.insert(UserIDTable.TABLE_NAME, null, values);
		db.close();
	}
	
	public void addUser(String uid, String pwd, boolean selection){
		if (!selection){
			addUser(uid, pwd);
			return;
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("UPDATE " + UserIDTable.TABLE_NAME 
				+ " SET "
				+ UserIDTable.SELECTION + "=1", null);
		cursor.close();
		db.close();
		addUser(uid, pwd);
		
		db = helper.getWritableDatabase();
		db.rawQuery("UPDATE " + UserIDTable.TABLE_NAME
				+ " SET "
				+ UserIDTable.SELECTION + "=1"
				+ " WHERE "
				+ UserIDTable.UID + "=?;", new String[] {uid});
		db.close();
	}
	
	public List<UserIDStructure> allUser(){
		List<UserIDStructure> userList = new ArrayList<UserIDStructure>();
		SQLiteDatabase db = helper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM " + UserIDTable.TABLE_NAME, null);
		while (cursor.moveToNext()) {
			UserIDStructure userIDStructure = new UserIDStructure();
			userIDStructure.uid = cursor.getString(cursor.getColumnIndex(UserIDTable.UID));
			userIDStructure.pwd = cursor.getString(cursor.getColumnIndex(UserIDTable.PWD));
			userIDStructure.select = cursor.getInt(cursor.getColumnIndex(UserIDTable.SELECTION));
			
			userList.add(userIDStructure);
		}
		cursor.close();
		db.close();
		return userList;
	}
}
