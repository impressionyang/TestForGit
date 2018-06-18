package com.example.impressionyang.testforgit.MainFuvtion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.impressionyang.testforgit.MainContent;
import com.example.impressionyang.testforgit.R;

//233

public class   MainActivity extends Activity{

	
	private WifiManager wifiManager;
	private SharedPreferences sp;
	private boolean isRead;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		sp = getSharedPreferences("share_wifi", Context.MODE_PRIVATE);
		if(!checkWifiConnect() || !isRead){
			startActivity("");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void startActivity(String msg){
		Intent intent = new Intent(MainActivity.this,MainContent.class);
		startActivity(intent);
	}
	public void finishActivity(String msg){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//mUnityPlayer.quit();
			}
		});
		super.onBackPressed();
		this.finish();
	}
	
	public boolean checkWifiConnect(){
		boolean isconnect = wifiManager.isWifiEnabled();
		if(isconnect == false){
			return false;
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ssid = wifiInfo.getSSID();
		if(!"YANZHENYU".equals(ssid))
			return false;
		return true;	
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent intent = new Intent(MainActivity.this,SocketService.class);
		stopService(intent);
		sp.edit().putBoolean("wifi_read", false).commit();
	}

	public void set_layout(LinearLayout layout){
		//View scene =mUnityPlayer.getView();
		//layout.addView(scene);
		//UnityPlayer.UnitySendMessage("routine", "cancelAlarm", "");
	}
}
