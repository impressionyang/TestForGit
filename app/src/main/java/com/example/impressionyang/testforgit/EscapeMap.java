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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class EscapeMap extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout layout;
    ImageView imageView;
    TextView alarmText;
    Intent intent;
    public boolean alive=true;
    MyThread a=null;
    public static EscapeMap instance=null;
    Button btn_finish_map;
    Socket socket;
    Boolean iswhilealive;
    int condition;
    private long mExitTime = System.currentTimeMillis();  //为当前系统时间，单位：毫秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escape_map);
        instance=this;
        layout=(RelativeLayout) findViewById(R.id.LinearLayout);
        btn_finish_map=(Button)findViewById(R.id.btn_finish_map);
        btn_finish_map.setOnClickListener(this);
        imageView=findViewById(R.id.image_backround);
        alarmText=findViewById(R.id.alarm_text);
        alarmText.setVisibility(View.INVISIBLE);
        layout.setOnClickListener(this);

        alive=false;
        iswhilealive=true;
        intent=getIntent();
        try{
            condition=Integer.valueOf(intent.getStringExtra("condition"));
        }catch (Exception e){
            condition=666;
            e.printStackTrace();
        }
        if(condition>=0&&condition<=6){
            Toast.makeText(this,String.valueOf(condition),Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"测试输入有误，请重新输入！",Toast.LENGTH_LONG).show();
        }
        configureConditions(condition);

    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler =new Handler(){
        public void handleMessage(final Message msg){
            switch (msg.what){
                case 0:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("工厂平面图");
                    imageView.setImageResource(R.drawable.condition00);
                    break;
                case 10:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("请走绿色路线");
                    imageView.setImageResource(R.drawable.condition10);
                    break;
                case 11:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("请走绿色路线");
                    imageView.setImageResource(R.drawable.condition11);
                    break;
                case 20:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("请走绿色路线");
                    imageView.setImageResource(R.drawable.condition20);
                    break;
                case 21:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("请走绿色路线");
                    imageView.setImageResource(R.drawable.condition21);
                    break;
                case 30:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("请走绿色路线");
                    imageView.setImageResource(R.drawable.condition30);
                    break;
                case 31:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("请走绿色路线");
                    imageView.setImageResource(R.drawable.condition31);
                    break;
                case 40:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("请走绿色路线");
                    imageView.setImageResource(R.drawable.condition40);
                    break;
                case 41:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("请走绿色路线");
                    imageView.setImageResource(R.drawable.condition41);
                    break;
                case 50:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("请走绿色路线");
                    imageView.setImageResource(R.drawable.condition50);
                    break;
                case 51:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("请走绿色路线");
                    imageView.setImageResource(R.drawable.condition51);
                    break;
                case 60:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("请走绿色路线");
                    imageView.setImageResource(R.drawable.condition60);
                    break;
                case 61:
                    alarmText.setVisibility(View.VISIBLE);
                    alarmText.setText("请走绿色路线");
                    imageView.setImageResource(R.drawable.condition61);
                    break;
                case 666:
                    alarmText.setVisibility(View.VISIBLE);
                    Toast.makeText(EscapeMap.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;

                default:
                    Toast.makeText(EscapeMap.this,"error of map",Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(EscapeMap.this,Main2Activity.class));
                    break;
            }

            super.handleMessage(msg);
        }
    };

    public void configureConditions(int condition){
        switch (condition){
            case 0:
                alive=true;
                a=new MyThread("condition0",0,0);
                a.start();
                break;
            case 1:
                alive=true;
                a=new MyThread("condition1",11,10);
                a.start();
                break;
            case 2:
                alive=true;
                a=new MyThread("condition2",21,20);
                a.start();
                break;
            case 3:
                alive=true;
                a=new MyThread("condition3",31,30);
                a.start();
                break;
            case 4:
                alive=true;
                a=new MyThread("condition4",41,40);
                a.start();
                break;
            case 5:
                alive=true;
                a=new MyThread("condition5",51,50);
                a.start();
                break;
            case 6:
                alive=true;
                a=new MyThread("condition6",61,60);
                a.start();
                break;

            default:
                alarmText.setVisibility(View.INVISIBLE);
                //Toast.makeText(EscapeMap.this,"eror",Toast.LENGTH_LONG).show();
                Main2Activity.ischecklock=false;
                alive=false;
                startActivity(new Intent(EscapeMap.this,Main2Activity.class));
                EscapeMap.this.finish();
                break;
        }
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

    public class MyThread extends Thread{
        int a,b;
        public MyThread(String threadname,int a,int b){
            super(threadname);
            this.a=a;
            this.b=b;
        }

        @Override
        public void run() {
            while (alive){
                try{
                    Message message=new Message();
                    message.what=a;
                    mHandler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message1=new Message();
                    message1.what=b;
                    mHandler.sendMessage(message1);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    //check
                    WifiManager manager=(WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                    WifiInfo info=manager.getConnectionInfo();
                    String ssid=info.getSSID();
                    configureSsid(ssid);
                    if(configureSsid(ssid).equals("ESP8266")){
                        configureChange();
                    }       //if(ssid)


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public  void configureChange(){
        try {
            socket = new Socket("192.168.4.1", 8080);
            socket.setSoTimeout(10000);
            if (socket.isConnected()) {
                InputStream inputStream = socket.getInputStream();
                char[] mod = new char[7];
                mod[0]= (char) inputStream.read();
                if(mod[0]=='8'){
                    for (int i = 1; i < 7; i++) {
                        mod[i] = (char) inputStream.read();
                        String a = String.valueOf(mod[i]);
                        if (a.equals("49")) {
                            if (i != condition) {
                                iswhilealive = false;
                                Main2Activity.ischecklock = false;
                                alive = false;
                                startActivity(new Intent(EscapeMap.this,Main2Activity.class));
                                EscapeMap.this.finish();
                            }
                            break;
                        }else if(i==6){
                            alive=false;
                            startActivity(new Intent(EscapeMap.this,Main2Activity.class));
                            EscapeMap.this.finish();
                        }
                    }
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Message message2=new Message();
            message2.obj=e.toString();
            message2.what=666;
            mHandler.sendMessage(message2);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_finish_map:
                Main2Activity.ischecklock=false;
                alive=false;
                startActivity(new Intent(EscapeMap.this,Main2Activity.class));
                EscapeMap.this.finish();
                break;

            case R.id.LinearLayout:
                if(condition==0){
                    Toast.makeText(v.getContext(),"工厂平面图",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(v.getContext(),"请走绿色路线",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }



    @Override
    public void onBackPressed() {
        Main2Activity.ischecklock=false;
        alive=false;
        Intent intent=new Intent(this,Main2Activity.class);
        startActivity(intent);
        EscapeMap.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Main2Activity.ischecklock=false;
        alive=false;
        Intent intent=new Intent(this,Main2Activity.class);
        startActivity(intent);
    }
}
