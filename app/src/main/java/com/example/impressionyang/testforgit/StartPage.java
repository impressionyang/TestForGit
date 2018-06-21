package com.example.impressionyang.testforgit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class StartPage extends Activity {

    private long time = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start_page);

        Handler handler = new Handler();


        handler.postDelayed(new Runnable() {
            /*
                postDelayed

                public final boolean postDelayed(Runnable r,long delayMillis)

                Causes the Runnable r to be added to the message queue, to be run after the specified amount of time elapses.
                The runnable will be run on the thread to which this handler is attached.
            */
            @Override
            public void run() {
                startActivity(new Intent(StartPage.this, com.example.impressionyang.testforgit.Main2Activity.class));
                StartPage.this.finish();
                finish();
            }
        }, time);
    }

}



