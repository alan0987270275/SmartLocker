package com.example.alan_lin.smart_locker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;

public class MainActivity extends Activity {



    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        AWSMobileClient.getInstance().initialize(this).execute();
        SetStartPageText();
        registerbutton();
    }






    public void registerbutton(){
        final Button R_button = (Button) findViewById(R.id.button);
        R_button.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                //((Button)R_button).setTextColor(Color.parseColor("##2F80ED"));
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , RegisisterActivity.class);
                startActivity(intent);
            }

        });
    }

    public void SetStartPageText(){
        TextView tiltename = (TextView)findViewById(R.id.textView1);
        TextView solgan = (TextView)findViewById(R.id.textView2);
        tiltename.setText("刷刷櫃");
        solgan.setText("快來試試 方便聰明的儲物櫃");
    }


}
