package com.coolweather.app.util;

import android.text.TextUtils;

import com.coolweather.app.model.AreaInfo;
import com.coolweather.app.model.CoolWeatherDB;

public class Utility {
	/*
	 * 解析和处理服务器返回的省|市|县数据
	 */
	public synchronized static boolean handleAreaResponse(CoolWeatherDB coolWeatherDB, String response, int areaLevel){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length > 0){
				for(String p : allProvinces){
					String[] array = p.split("\\|");
					AreaInfo areaInfo = new AreaInfo();
					areaInfo.setCode(array[0]);
					areaInfo.setName(array[1]);
					areaInfo.setLevel(areaLevel);
					//将解析出来的数据存储到AreaInfo表
					coolWeatherDB.saveAreaInfo(areaInfo);
				}
				return true;
			}
		}
		return false;
	}


}