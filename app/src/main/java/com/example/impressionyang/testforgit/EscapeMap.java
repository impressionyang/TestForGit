package com.example.impressionyang.testforgit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EscapeMap extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escape_map);

        LinearLayout layout=(LinearLayout)findViewById(R.id.LinearLayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"red",Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onClick(View v) {

    }
}
