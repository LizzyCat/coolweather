package com.coolweather.app.util;

import android.text.TextUtils;

import com.coolweather.app.model.AreaInfo;
import com.coolweather.app.model.CoolWeatherDB;

public class Utility {
	/*
	 * �����ʹ�����������ص�ʡ|��|������
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
					//���������������ݴ洢��AreaInfo��
					coolWeatherDB.saveAreaInfo(areaInfo);
				}
				return true;
			}
		}
		return false;
	}


}