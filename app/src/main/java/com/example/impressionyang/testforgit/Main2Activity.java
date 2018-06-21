package com.example.impressionyang.testforgit;

import android.annotation.SuppressLint;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    WifiManager wifiManager;

    int clip = 0,counter=0;
    Socket socket;
    TextView tv_show;
    Button btn;
    RelativeLayout layout;
    MyThread a=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListener();
        a=new MyThread("233","ssid");
        a.start();
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler =new Handler(){
        public void  handleMessage(final Message msg){
            switch (msg.what){
                case 1:
                    tv_show.setText(msg.obj.toString());
                    break;

                case 2:
                    tv_show.setText(msg.obj.toString());
                    break;

            }
            super.handleMessage(msg);
        }
    };

    public void setListener(){
        tv_show = findViewById(R.id.tv_temp);
        btn = findViewById(R.id.btn_socket);
        layout=findViewById(R.id.layout);

        layout.setOnClickListener(this);
        btn.setOnClickListener(this);
        tv_show.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_socket:
                wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                final String ssid = wifiInfo.getSSID();
                if (clip == 0 && ssid.equals("ESP8266")) {
                    a=new MyThread("233",ssid);
                    a.start();
                    a.interrupt();
                    clip=1;
                } else {
                    a.interrupt();
                    a=new MyThread("233",ssid);
                    a.start();
                    a.interrupt();
                }
                break;
            case R.id.tv_temp:
                tv_show.setText("default");
                break;
            case R.id.layout:
                a.interrupt();
                a=new MyThread("233","ssid");
                a.start();
                a.interrupt();
                break;
        }

    }


    public class MyThread extends Thread{
        String ssid;
        public MyThread(String threadname,String ssid){
            super(threadname);
            this.ssid=ssid;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(ssid.equals("ESP8266")){
                try {
                    socket = new Socket("192.168.4.1", 8080);
                    socket.setSoTimeout(10000);
                    InputStream inputStream = socket.getInputStream();
                    byte[] mod = new byte[8];
                    for (int i = 0; i < 8; i++) {
                        mod[i] = (byte) inputStream.read();
                    }
                    String line = new String(mod);
                    Message message=new Message();
                    message.obj=line;
                    message.what=1;
                    mHandler.sendMessage(message);
//                    if (line == null) {
//                        Toast.makeText(Main2Activity.this, "null:", Toast.LENGTH_LONG).show();
//                    } else {
//                        tv_show.setText(line);
//                        Toast.makeText(Main2Activity.this, "contens:" + line, Toast.LENGTH_LONG).show();
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
//                TimerTask task=new TimerTask() {
//                    @Override
//                    public void run() {
//                        Message message=new Message();
//                        message.obj=counter+":"+ssid.toString();
//                        message.what=1;
//                        mHandler.sendMessage(message);
//                    }
//                };
//
//
//                try{
//                    for(int i=0;i<5;i++){
//                        new Timer(true).schedule(task,1000,1000);
//                        counter++;
//                    }
//                }catch (Exception e){
//                    Message message=new Message();
//                    message.obj=e.toString();
//                    message.what=1;
//                    mHandler.sendMessage(message);
//                    e.printStackTrace();
//                }
                Message message=new Message();
                message.obj=counter+":"+ssid.toString();
                message.what=2;
                mHandler.sendMessage(message);
                counter++;

            }
        }
    }





    @Override
    protected void onDestroy() {
        /**
         * 防止线程不能停止
         */
        if (null != a) {
            a.interrupt();
        }
        super.onDestroy();
    }

}