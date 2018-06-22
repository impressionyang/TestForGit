package com.example.impressionyang.testforgit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EscapeMap extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout layout;
    ImageView imageView;
    TextView alarmText;
    Intent intent;
    public boolean alive=true;
    MyThread a=null;
    public static EscapeMap instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escape_map);
        instance=this;
        layout=(RelativeLayout) findViewById(R.id.LinearLayout);
        imageView=findViewById(R.id.image_backround);
        alarmText=findViewById(R.id.alarm_text);
        alarmText.setVisibility(View.INVISIBLE);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"请走红色路线",Toast.LENGTH_SHORT).show();
            }
        });

        alive=false;
        intent=getIntent();
        int condition=0;
        try{
            condition=Integer.valueOf(intent.getStringExtra("condition"));
        }catch (Exception e){
            e.printStackTrace();
        }
        configureConditions(condition);

    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler =new Handler(){
        public void handleMessage(final Message msg){
            switch (msg.what){
                case 0:
                    imageView.setImageResource(R.drawable.condition00);
                    break;
                case 10:
                    alarmText.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.condition10);
                    break;
                case 11:
                    alarmText.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.condition11);
                    break;
                case 20:
                    alarmText.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.condition20);
                    break;
                case 21:
                    alarmText.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.condition21);
                    break;
                case 30:
                    alarmText.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.condition30);
                    break;
                case 31:
                    alarmText.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.condition31);
                    break;
                case 40:
                    alarmText.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.condition40);
                    break;
                case 41:
                    alarmText.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.condition41);
                    break;
                case 50:
                    alarmText.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.condition50);
                    break;
                case 51:
                    alarmText.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.condition51);
                    break;
                case 60:
                    alarmText.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.condition60);
                    break;
                case 61:
                    alarmText.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.condition61);
                    break;
                case 666:
                    alarmText.setVisibility(View.VISIBLE);
                    Toast.makeText(EscapeMap.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;

                default:
                    Toast.makeText(EscapeMap.this,"eeror",Toast.LENGTH_LONG).show();
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
                a=new MyThread("condition0",11,10);
                a.start();
                break;
            case 2:
                alive=true;
                a=new MyThread("condition0",21,20);
                a.start();
                break;
            case 3:
                alive=true;
                a=new MyThread("condition0",31,30);
                a.start();
                break;
            case 4:
                alive=true;
                a=new MyThread("condition0",41,40);
                a.start();
                break;
            case 5:
                alive=true;
                a=new MyThread("condition0",51,50);
                a.start();
                break;
            case 6:
                alive=true;
                a=new MyThread("condition0",61,60);
                a.start();
                break;

                default:
                    Toast.makeText(EscapeMap.this,"eeror",Toast.LENGTH_LONG).show();
                    break;
        }
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
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        finish();
    }
}
