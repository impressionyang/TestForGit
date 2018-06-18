package com.example.impressionyang.testforgit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.impressionyang.testforgit.MainFuvtion.DataBean;
import com.example.impressionyang.testforgit.MainFuvtion.SocketService;

import java.util.Timer;

public class MainContent extends Activity implements View.OnClickListener{
    WifiManager wifiManager;
    Timer timer;
    private Handler handler;
    private TextView tv_data;
    private SharedPreferences sp;
    private boolean isRead = false;
    private Button btn,btn_refresh;
    private TextView tv,tv_t_room1,tv_t_room2,tv_f_room1,tv_f_room2;
    private long mExitTime = System.currentTimeMillis();
    private boolean isExit = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
        setTitle();
        sp = getSharedPreferences("share_wifi", Context.MODE_PRIVATE);
        isRead = sp.getBoolean("wifi_read", false);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Intent intent;
                switch (msg.what){
                    case 3:
                        DataBean dat = (DataBean) msg.obj;
                        Toast.makeText(MainContent.this, dat.getTempature() + "", Toast.LENGTH_SHORT).show();
                        if(dat.getNum() == 0){
                            tv_t_room1.setText("�¶ȣ� "+ String.valueOf(dat.getTempature()));
                            tv_f_room1.setText("�̸У�"+ String.valueOf(dat.getFog()));
                            if(dat.getTempature()>=26||dat.getFog()>=0.1){
                                disconnectSocket();
                                //intent=new Intent(MainContent.this, TestUnityActivity.class);
                                //intent.putExtra("fuction","1");
                                //startActivity(intent);
                            }
                        }
                       else if(dat.getNum() == 1){
                            tv_t_room2.setText("�¶ȣ� "+ String.valueOf(dat.getTempature()));
                            tv_f_room2.setText("�̸У�"+ String.valueOf(dat.getFog()));
                            if(dat.getTempature()>=26||dat.getFog()>=0.1){
                                disconnectSocket();
                                //intent=new Intent(MainContent.this, TestUnityActivity.class);
                                //intent.putExtra("fuction","2");
                                //startActivity(intent);
                            }
                        }

                }

            }
        };
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

    public void connectSocket(){
        Intent intent = new Intent(MainContent.this,SocketService.class);
        intent.putExtra("messenger", new Messenger(handler));
        startService(intent);
    }

    public void disconnectSocket(){
        Intent intent = new Intent(MainContent.this,SocketService.class);
        stopService(intent);
    }




    public void setTitle(){
        btn= (Button) findViewById(R.id.id_btn_title);
        btn.setBackgroundResource(R.drawable.icon_setting);
        btn.setOnClickListener(this);

        btn_refresh= (Button) findViewById(R.id.id_btn_refresh);
        btn_refresh.setBackgroundResource(R.drawable.icon_refresh);
        btn_refresh.setOnClickListener(this);

        tv= (TextView)findViewById(R.id.id_text_title);
        tv.setText("�������");
        tv.setTextColor(Color.rgb(255, 255, 255));



        tv_f_room1=findViewById(R.id.tv_fog_room1);
        tv_f_room2=findViewById(R.id.tv_fog_room_2);
        tv_t_room1=findViewById(R.id.tv_tempreture_room1);
        tv_t_room2=findViewById(R.id.tv_tempreture_room_2);

        Button room1,room2;
        room1=findViewById(R.id.btn_room_1);
        room2=findViewById(R.id.btn_room_2);
        room1.setBackgroundResource(R.drawable.icon_home);
        room2.setBackgroundResource(R.drawable.icon_home);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_btn_title:
                startActivity(new Intent(MainContent.this,MenuContainer.class));
                break;
            case R.id.id_btn_refresh:
                connectSocket();
                break;
            default:
                Toast.makeText(this,"erro", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void get_call_fuction(String call_num){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+call_num));
        startActivity(intent);
    }




    // ��дActivity��onKeyDown��������
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// ��keyCode�����˳��¼�ֵʱ
            ToQuitTheApp();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //��װToQuitTheApp����
    private void ToQuitTheApp() {
        if (isExit) {
            // ACTION_MAIN with category CATEGORY_HOME ��������Ļ
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);// ʹ�����ֹͣ���в��˳�����
        } else {
            isExit = true;
            Toast.makeText(this, "�ٰ�һ���˳�APP", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 1000);// 3�������Ϣ
        }
    }

    //����Handler��������������Ϣ
    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {//������Ϣ
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }
    };
}
