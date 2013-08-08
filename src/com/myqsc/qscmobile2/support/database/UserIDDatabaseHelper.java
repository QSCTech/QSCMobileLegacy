package com.myqsc.qscmobile2.support.database;

import com.myqsc.qscmobile2.support.database.table.UserIDTable;
import com.myqsc.qscmobile2.uti.LogHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserIDDatabaseHelper extends SQLiteOpenHelper{
	static final String DB_NAME = "UserID.db";
	static final int DB_VERSION = 4;
	final String CREATE_DB_SQL = "CREATE TABLE " + UserIDTable.TABLE_NAME
			+ "("
			+ UserIDTable.UID + " text,"
			+ UserIDTable.PWD + " text,"
			+ UserIDTable.SELECTION + " integer "
			+ ");";
	final String DROP_DB_SQL = "DROP TABLE IF EXISTS " + UserIDTable.TABLE_NAME;
	
	public UserIDDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DB_SQL);
		LogHelper.i("DB CREATED:" + CREATE_DB_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_DB_SQL);
		this.onCreate(db);
	}
	
	

}
