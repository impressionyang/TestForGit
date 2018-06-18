package com.example.impressionyang.testforgit.MainFuvtion;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
//233

public class SocketService extends Service {

	Socket socket;
	BufferedReader br;
	boolean isRead = false;
	private SharedPreferences sp;
	private Handler handler;
	private Messenger mMessenger;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		mMessenger = (Messenger) intent.getExtras().get("messenger");

		sp = getSharedPreferences("share_wifi", Context.MODE_PRIVATE);
		Toast.makeText(SocketService.this, "service??????", Toast.LENGTH_SHORT).show();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					Toast.makeText(SocketService.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				}else if(msg.what == 2){

					String str = (String)msg.obj;
					Toast.makeText(SocketService.this, str, Toast.LENGTH_SHORT).show();
					String[] nums = str.split("#");
					if(nums != null && nums.length >= 0){
						int len = nums.length;
						DataBean data  = new DataBean();;
						for(int i=0;i<len;i++){


							data.setTempature(Float.parseFloat(nums[1]));
							data.setFog(Float.parseFloat(nums[2]));
							data.setNum(Integer.parseInt((nums[0])));
						}
						Message dat = new Message();
						dat.what = 3;
						dat.obj = data;
						try {
							mMessenger.send(dat);
							Toast.makeText(SocketService.this, "????????", Toast.LENGTH_SHORT).show();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}

			     /*L
					for(DataBean db:list){
						if(db.getTempature() > 29.0 && db.getFog() > 0.1){
							Toast.makeText(SocketService.this, "????", Toast.LENGTH_SHORT).show();
							UnityPlayer.UnitySendMessage("routine", "startAlarm", "");
						}else{
							UnityPlayer.UnitySendMessage("routine", "cancelAlarm", "");
						}
					}
					map.clear();
					*/
				}

			}

		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					socket = new Socket("192.168.4.1", 8080);
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					isRead = true;
					Message msg = new Message();
					msg.what = 1;
					msg.obj = "wifi????????";
					handler.sendMessage(msg);
					if(isRead)
						new Thread(new SocketHandler()).start();
					sp.edit().putBoolean("wifi_read", isRead).commit();
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 1;
					msg.obj = "wifi???????";
					handler.sendMessage(msg);
					isRead = false;
					sp.edit().putBoolean("wifi_read", isRead).commit();
				}
			}
		}).start();
		return super.onStartCommand(intent,flags,startId);
	}


	private class SocketHandler implements Runnable {

		@Override
		public void run() {

			while(isRead){
				try {
					Message msg = new Message();
					msg.what = 2;
					msg.obj = br.readLine();
					handler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
					isRead = false;
					sp.edit().putBoolean("wifi_read", isRead).commit();
					closeSocket();
				}
			}
		}

	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		closeSocket();
	}

	private void closeSocket() {
		if(br != null){
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			br = null;
		}
		if(socket != null){
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			socket = null;
		}
		isRead = false;
		sp.edit().putBoolean("wifi_read", isRead).commit();
	}


}