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
	 * 数据库名
	 */
	public static final String DB_NAME="cool_weather";
	
	/*
	 * 数据库版本
	 */
	public static final int VERSION=1;
	
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	/*
	 * 将构造方法私有化
	 */
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper =new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
		db=dbHelper.getWritableDatabase();
	}

	/*
	 * 获取CoolWeatherDB的新例
	 */
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB =new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	/*
	 * 将AreaInfo实例存储到数据库中
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
	 * 根据code代号值从数据库中读取某个省|市|县的信息
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
	 * 从数据库中读取全国所有的省份信息
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
	 * 从数据库中读取某省下所有的城市信息
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
	 * 从数据库中读取某省下所有的城市信息
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
