package com.example.impressionyang.testforgit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    WifiManager wifiManager;
    boolean isExit=false;

    boolean alive=true;
    int counter=0;
    Socket socket;
    TextView tv_show;
    EditText editText;
    Button btn;
    RelativeLayout layout;
    MyThread a=null;
    private long mExitTime = System.currentTimeMillis();  //为当前系统时间，单位：毫秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.et_input);
        setListener();
        startSocket();
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
                    if(counter==5){
                        Intent intent=new Intent(Main2Activity.this,EscapeMap.class);
                        intent.putExtra("condition","0");
                        //alive=false;
                        //EscapeMap.MyThread.interrupted();
//                        if(EscapeMap.instance!=null){
//                            EscapeMap.instance.finish();
//                        }
                        startActivity(intent);
                    }else if(counter==10){
                        Intent intent=new Intent(Main2Activity.this,EscapeMap.class);
                        intent.putExtra("condition","4");
                        startActivity(intent);
                    }else if(counter==15){
                        Intent intent=new Intent(Main2Activity.this,EscapeMap.class);
                        intent.putExtra("condition","6");
                        startActivity(intent);
                    }
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

    public String configureSsid(String originssid){
        char []ssid=new char[7];
        char []origin=originssid.toCharArray();
        String realssid=null;
        for(int i=1;i<8;i++){
            ssid[i-1]=origin[i];
        }

        realssid=new String(ssid);

        return  realssid;
    }

    public void startSocket(){
        alive=true;
        a=new MyThread("233");
        a.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_socket:
                String input=editText.getText().toString();
                Intent intent=new Intent(this,EscapeMap.class);
                if(input==""){
                    input="0";
                }
                intent.putExtra("condition",input);
                alive=false;
                startActivity(intent);
                break;
            case R.id.tv_temp:
                tv_show.setText("default");
                break;
            case R.id.layout:

                break;
        }

    }


    public class MyThread extends Thread{
        public MyThread(String threadname){
            super(threadname);
        }

        @Override
        public void run() {
            boolean iswhilealive=true;

            while (alive){
                wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                final String ssid = wifiInfo.getSSID();
                boolean iswificonnect=wifiManager.isWifiEnabled();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Message message=new Message();
                    message.obj=e.toString();
                    message.what=2;
                    mHandler.sendMessage(message);
                    e.printStackTrace();
                }
                String realssid=configureSsid(ssid);
                if(iswificonnect&&realssid.equals("ESP8266")){
                    try {
                        alive=false;
                        socket = new Socket("192.168.4.1", 8080);
                        socket.setSoTimeout(10000);
                        while(iswhilealive){
                            if(socket.isConnected()){
                                counter=0;
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
                            }else {
                                alive=true;
                                iswhilealive=false;
                            }
                        }
                    }catch (IOException e) {
                        alive=true;
                        e.printStackTrace();
                    }
                }else {
                    Message message=new Message();
                    message.obj="not connect:"+counter+"s"+ssid;
                    message.what=2;
                    mHandler.sendMessage(message);
                    counter++;
            }

            }
        }
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - mExitTime < 800) {
            System.exit(0);
        }
        else{
            Toast.makeText(this,"再按返回键退出！",Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();   //这里赋值最关键，别忘记
        }
    }

}