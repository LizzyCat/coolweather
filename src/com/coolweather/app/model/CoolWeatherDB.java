package com.coolweather.app.model;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.db.CoolWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	/*
	 * ���ݿ���
	 */
	public static final String DB_NAME="cool_weather";
	
	/*
	 * ���ݿ�汾
	 */
	public static final int VERSION=1;
	
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	/*
	 * �����췽��˽�л�
	 */
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper =new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
		db=dbHelper.getWritableDatabase();
	}

	/*
	 * ��ȡCoolWeatherDB������
	 */
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB =new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	/*
	 * ��AreaInfoʵ���洢�����ݿ���
	 */
	public void saveAreaInfo(AreaInfo areaInfo){
		if(areaInfo!=null){
			ContentValues values =new ContentValues();
			values.put("name",areaInfo.getName());
			values.put("code",areaInfo.getCode());
			values.put("level",areaInfo.getLevel());
			db.insert("AreaInfo", null, values);
		}
	}
	/*
	 * ����code����ֵ�����ݿ��ж�ȡĳ��ʡ|��|�ص���Ϣ
	 */
	public AreaInfo loadProvinces(String code){
		AreaInfo areaInfo=null;
		Cursor cursor=db.query("AreaInfo", null, "code=?", new String[]{code}, null, null, null);
		if(cursor.moveToFirst()){
		    areaInfo=new AreaInfo();
			areaInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
			areaInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
			areaInfo.setCode(cursor.getString(cursor.getColumnIndex("code")));
			areaInfo.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
		}
		if(cursor!=null)
		{
			cursor.close();
		}
		return areaInfo;
	}
	
	/*
	 * �����ݿ��ж�ȡȫ�����е�ʡ����Ϣ
	 */
	public List<AreaInfo> loadProvinces(){
		List<AreaInfo> list =new ArrayList<AreaInfo>();
		Cursor cursor=db.query("AreaInfo", null, "level=1", null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				AreaInfo areaInfo=new AreaInfo();
				areaInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
				areaInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
				areaInfo.setCode(cursor.getString(cursor.getColumnIndex("code")));
				areaInfo.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
				list.add(areaInfo);
			}while(cursor.moveToNext());
		}
		if(cursor!=null)
		{
			cursor.close();
		}
		return list;
	}
	
	/*
	 * �����ݿ��ж�ȡĳʡ�����еĳ�����Ϣ
	 */
	public List<AreaInfo> loadCities(int provinceID){
		List<AreaInfo> list =new ArrayList<AreaInfo>();
		Cursor cursor=db.query("AreaInfo", null, "id=?", new String[]{String.valueOf(provinceID)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				AreaInfo areaInfo=new AreaInfo();
				areaInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
				areaInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
				areaInfo.setCode(cursor.getString(cursor.getColumnIndex("code")));
				areaInfo.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
				list.add(areaInfo);
			}while(cursor.moveToNext());
		}
		if(cursor!=null)
		{
			cursor.close();
		}
		return list;
	}
	
	/*
	 * �����ݿ��ж�ȡĳʡ�����еĳ�����Ϣ
	 */
	public List<AreaInfo> loadCounties(int cityID){
		List<AreaInfo> list =new ArrayList<AreaInfo>();
		Cursor cursor=db.query("AreaInfo", null, "id=?", new String[]{String.valueOf(cityID)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				AreaInfo areaInfo=new AreaInfo();
				areaInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
				areaInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
				areaInfo.setCode(cursor.getString(cursor.getColumnIndex("code")));
				areaInfo.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
				list.add(areaInfo);
			}while(cursor.moveToNext());
		}
		if(cursor!=null)
		{
			cursor.close();
		}
		return list;
	}
	
	
}
