package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	
	/*Province�������
	 * id:���������
	 * name:ʡ|��|������
	 * code:ʡ|��|�ش���
	 * level:����1������ʡ����2�����м���3�����ؼ���
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
		db.execSQL(CRETAE_AREAINFO);//����AreaInfo��

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
