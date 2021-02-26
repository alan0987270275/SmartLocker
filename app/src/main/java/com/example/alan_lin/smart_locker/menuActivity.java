package com.example.alan_lin.smart_locker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class menuActivity extends Activity {

    ImageView find_B;
    ImageView add_B;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        find_Bhit();
        add_Bhit();
    }

    public void find_Bhit(){
        find_B = (ImageView) findViewById(R.id.imageView1);
        find_B.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(menuActivity.this , FindActivity.class);
                startActivity(intent);
            }

        });
    }

    public void add_Bhit(){
        add_B = (ImageView) findViewById(R.id.imageView2);
        add_B.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(menuActivity.this , AdduserActivity.class);
                startActivity(intent);
            }

        });
    }
}
