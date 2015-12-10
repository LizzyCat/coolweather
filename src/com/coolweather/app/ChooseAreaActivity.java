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
	 * ʡ�б�
	 */
	private List<AreaInfo> provinceList;
	/*
	 * ���б�
	 */
	private List<AreaInfo> cityList;
	/*
	 * ���б�
	 */
	private List<AreaInfo> countyList;
	/*
	 * ��ѡ�е�ʡ��
	 */
	private AreaInfo selectProvince;
	/*
	 * ��ѡ�еĳ���
	 */
	private AreaInfo selectCity;
	/*
	 * ��ѡ�е��ط�
	 */
	private AreaInfo selectCounty;
	/*
	 * ��ǰѡ�еļ���
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
	 * ��ѯȫ�����е�ʡ�����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ��������ѯ
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
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, LEVEL_PROVINCE);
		}
	}

	/*
	 * ��ѯѡ�е�ʡ�ݵ����еĳ��У����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ��������ѯ
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
	 * ��ѯѡ�е�ʡ�ݵ����еĳ��У����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ��������ѯ
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
	 * ���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯʡ��������
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
					// ͨ��runOnUiThread()�����ص����̴߳����߼�
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
				// ͨ��runOnUiThread()�����ص����̴߳����߼�
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��",
								Toast.LENGTH_SHORT).show();
					}
				});// runOnUiThread end
			}
		});

	}

	private void showProgressDialog() {
		if(progressDialog == null){
			progressDialog =new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
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
	 * ����Back���������ݵ�ǰ�ļ������жϣ���ʱӦ�÷������б�ʡ�б�����ֱ���˳���
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
