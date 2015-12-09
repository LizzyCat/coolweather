package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	
	/*Province建表语句
	 * id:自增长编号
	 * name:省|市|县名称
	 * code:省|市|县代号
	 * level:级别（1：代表省级，2代表市级，3代表县级）
	 */
	public static final String CRETAE_AREAINFO = "create table AreaInfo ("
			+"id integer primary key autoincrement,"
			+"name text,"
			+"code text,"
			+"level integer";
	
	public CoolWeatherOpenHelper(Context context, String name,CursorFactory factory, int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CRETAE_AREAINFO);//创建AreaInfo表

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
