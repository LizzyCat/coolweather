package com.coolweather.app;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.AreaInfo;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.text.TextUtils;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();

	/*
	 * 省列表
	 */
	private List<AreaInfo> provinceList;
	/*
	 * 市列表
	 */
	private List<AreaInfo> cityList;
	/*
	 * 县列表
	 */
	private List<AreaInfo> countyList;
	/*
	 * 被选中的省份
	 */
	private AreaInfo selectProvince;
	/*
	 * 被选中的城市
	 */
	private AreaInfo selectCity;
	/*
	 * 被选中的县份
	 */
	private AreaInfo selectCounty;
	/*
	 * 当前选中的级别
	 */
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.coolweather.app.R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectProvince = provinceList.get(position);
					queryProvinces();
				} else if (currentLevel == LEVEL_CITY) {
					selectCity = cityList.get(position);
					queryCities();
				}
			}
		});
		queryProvinces();
	}

	/*
	 * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器查询
	 */
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (AreaInfo province : provinceList) {
				dataList.add(province.getName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, LEVEL_PROVINCE);
		}
	}

	/*
	 * 查询选中的省份的所有的城市，优先从数据库查询，如果没有查询到再去服务器查询
	 */
	private void queryCities() {
		provinceList = coolWeatherDB.loadCities(selectProvince.getId());
		if (provinceList.size() > 0) {
			dataList.clear();
			for (AreaInfo city : cityList) {
				dataList.add(city.getName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectProvince.getName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(null, LEVEL_CITY);
		}

	}

	/*
	 * 查询选中的省份的所有的城市，优先从数据库查询，如果没有查询到再去服务器查询
	 */
	private void queryCounties() {
		provinceList = coolWeatherDB.loadCities(selectCity.getId());
		if (provinceList.size() > 0) {
			dataList.clear();
			for (AreaInfo county : countyList) {
				dataList.add(county.getName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectCity.getName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(null, LEVEL_COUNTY);
		}

	}

	/*
	 * 根据传入的代号和类型从服务器上查询省市县数据
	 */
	private void queryFromServer(final String code, final int areaLevel) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				boolean result = Utility.handleAreaResponse(coolWeatherDB,
						response, areaLevel);
				if (result) {
					// 通过runOnUiThread()方法回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if (areaLevel == LEVEL_PROVINCE) {
								queryProvinces();
							} else if (areaLevel == LEVEL_CITY) {
								queryCities();
							} else {
								queryCounties();
							}
						}
					});// runOnUiThread end
				}
			}

			@Override
			public void onError(Exception e) {
				// 通过runOnUiThread()方法回到主线程处理逻辑
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败",
								Toast.LENGTH_SHORT).show();
					}
				});// runOnUiThread end
			}
		});

	}

	private void showProgressDialog() {
		if(progressDialog == null){
			progressDialog =new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	private void closeProgressDialog() {
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	
	/*
	 * 捕获Back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出。
	 */
	@Override
	public void onBackPressed(){
		if(currentLevel ==  LEVEL_COUNTY){
			queryCities();
		}else if(currentLevel ==  LEVEL_CITY){
			queryProvinces();
		}else{
			finish();
		}
	}
}
