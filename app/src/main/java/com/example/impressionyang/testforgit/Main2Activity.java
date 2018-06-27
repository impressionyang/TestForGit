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
    public static boolean ischecklock=false;

    boolean alive=true;
    int counter=0,clic=0;
    Socket socket;
    TextView tv_status;
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
        tv_status=findViewById(R.id.tv_status);
        tv_show = findViewById(R.id.tv_temp);

        setListener();
        startSocket("2333");//233
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler =new Handler(){
        public void  handleMessage(final Message msg){
            switch (msg.what){
                case 1:     //设置
                    tv_show.setText(msg.obj.toString());
                    if(counter>=180){
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.exit(0);
                            }
                        }, 4000);
                    }
                    break;
                case 2: //测试
                    tv_show.setText(msg.obj.toString());
                    break;
                case 3:     //跳转
                    btn.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                    Intent intent=new Intent(Main2Activity.this,EscapeMap.class);
                    intent.putExtra("condition",msg.obj.toString());
                    ischecklock=true;
                    startActivity(intent);
                    break;
                case 4:         //状态
                    tv_status.setText(msg.obj.toString());
                    break;
                case 5:
                    Toast.makeText(Main2Activity.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void setListener(){
        tv_show = findViewById(R.id.tv_temp);
        btn = findViewById(R.id.btn_socket);
        layout=findViewById(R.id.layout);

        btn.setVisibility(View.INVISIBLE);
        editText.setVisibility(View.INVISIBLE);

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

    public void startSocket(String thredname){
        alive=true;
        a=new MyThread(thredname);
        a.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_socket:
                String input=editText.getText().toString();
                if(input.equals("")){
                    input="0";
                }
                Message message=new Message();
                message.obj=input;
                message.what=3;
                mHandler.sendMessage(message);
                break;
            case R.id.tv_temp:
                if(System.currentTimeMillis() - mExitTime < 300) {
                    clic++;
                }
                else{
                    mExitTime = System.currentTimeMillis();   //这里赋值最关键，别忘记
                }
                if(clic==3){
                    clic=0;
                    btn.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.layout:
                btn.setVisibility(View.INVISIBLE);
                editText.setVisibility(View.INVISIBLE);
                break;
        }

    }


    public class MyThread extends Thread{
        public MyThread(String threadname){
            super(threadname);
        }

        @Override
        public void run() {
            while (alive){
                wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                final String ssid = wifiInfo.getSSID();
                boolean iswificonnect=wifiManager.isWifiEnabled();
                String realssid=configureSsid(ssid);
                if(iswificonnect&&realssid.equals("ESP8266")){
                    Message messagewifi=new Message();
                    messagewifi.what=1;
                    messagewifi.obj=" wifi connected to :"+realssid;
                    mHandler.sendMessage(messagewifi);
                    try {

                        socket = new Socket("192.168.4.1", 8080);
                        socket.setSoTimeout(5000);

                        if(socket.isConnected()){
                            Message message10=new Message();
                            message10.what=4;
                            message10.obj=" socket connected to :"+realssid;
                            mHandler.sendMessage(message10);
                            counter=0;
                            InputStream inputStream = socket.getInputStream();
                            char[] mod = new char[7];

                            try{
                                Message message1=new Message();
                                Boolean aa=true;
                                mod[0] = (char) inputStream.read();
                                if(mod[0]=='8'){
                                    for (int i = 1; i < 7; i++) {
                                        mod[i] = (char) inputStream.read();
                                        String a=String.valueOf(mod[i]);
                                        if(aa&&a.equals("1")){
                                            String condition=String.valueOf(i);
                                            message1.obj=condition;
                                            message1.what=3;
                                            aa=false;       //只读一次避免干扰
                                        }
                                    }       //for
                                }


//                                Message messagetest=new Message();
//                                String line=String.valueOf(mod);
//                                messagetest.obj=line;
//                                messagetest.what=5;
//                                mHandler.sendMessage(messagetest);

                                mHandler.sendMessage(message1);
                            }catch (Exception e){
                                Message message3=new Message();
                                message3.obj=e.toString();
                                message3.what=5;
                                mHandler.sendMessage(message3);
                                e.printStackTrace();
                            }



                            Message message2=new Message();
                            message2.obj="工厂现在状态正常";
                            message2.what=1;
                            mHandler.sendMessage(message2);
                        }else {
                            Message message5=new Message();
                            message5.what=4;
                            message5.obj=" socket not connect to :"+realssid;
                            alive=true;
                            mHandler.sendMessage(message5);
                        }
                    }catch (IOException e) {
                        alive=true;
                        e.printStackTrace();
                    }
                }else {
                    if(counter==180){
                        alive=false;
                        Message message=new Message();
                        message.obj="等待超时，3秒钟后自动退出！";
                        message.what=1;
                        mHandler.sendMessage(message);
                    }else {
                        Message message=new Message();
                        message.obj="no wifi connection:"+counter+"s";
                        message.what=1;
                        mHandler.sendMessage(message);
                        counter++;
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Message message=new Message();
                    message.obj=e.toString();
                    message.what=1;
                    mHandler.sendMessage(message);
                    e.printStackTrace();
                }

            }       //while(alive)
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