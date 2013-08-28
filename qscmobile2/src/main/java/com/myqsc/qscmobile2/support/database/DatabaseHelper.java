package com.myqsc.qscmobile2.support.database;

import com.myqsc.qscmobile2.support.database.table.UserIDTable;
import com.myqsc.qscmobile2.uti.LogHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	static final String DB_NAME = "UserID.db";
	static final int DB_VERSION = 6;
	final String CREATE_USERID_TABLE = "CREATE TABLE " + UserIDTable.TABLE_NAME
			+ "("
			+ UserIDTable.UID + " text,"
			+ UserIDTable.PWD + " text,"
			+ UserIDTable.SELECTION + " integer "
			+ ");";
	final String DROP_USERID_TABLE = "DROP TABLE IF EXISTS " + UserIDTable.TABLE_NAME;
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USERID_TABLE);
		LogHelper.d("DB CREATED: onCreate()" );
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_USERID_TABLE);
		this.onCreate(db);
	}
	
	

}
