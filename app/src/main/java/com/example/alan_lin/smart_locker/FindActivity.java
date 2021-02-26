package com.example.alan_lin.smart_locker;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.util.Date;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;



public class FindActivity extends Activity {

    MaterialCalendarView  myCalendarview;
    Button B1;
    Button B2;
    ImageView Image_add;
    TextView showtime;
    public static final String TAG = "DATE";
    String store_time="";
    String store_date="";
    int selectedBox = 0;
    private PopupWindow mPopupWindow;
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;
    DynamoDBMapper dynamoDBMapper;
    AmazonSQSClient client;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        name = getIntent().getStringExtra("name_id");
        client = new AmazonSQSClient(AWSMobileClient.getInstance().getCredentialsProvider());
        // Instantiate a AmazonDynamoDBMapperClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();
        mContext = getApplicationContext();
        mActivity = FindActivity.this;
        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.mpopwindow);
        initial();
        buttonaction();
        Calendaraction();
    }


    public void initial(){
        myCalendarview = (MaterialCalendarView) findViewById(R.id.calendarView);
        B1 = (Button) findViewById(R.id.button1);
        B2 = (Button) findViewById(R.id.button2);
        showtime = (TextView) findViewById(R.id.textView1);
        Image_add = (ImageView) findViewById(R.id.imageView);
    }


    public void buttonaction(){
        B1.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "刷刷櫃1已選擇", Toast.LENGTH_LONG).show();
                selectedBox =1;
                B1.setBackgroundResource(R.drawable.buttonclciked);
                B2.setBackgroundResource(R.drawable.button);

            }

        });
        B2.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "刷刷櫃2已選擇", Toast.LENGTH_LONG).show();
                selectedBox =2;
                B2.setBackgroundResource(R.drawable.buttonclciked);
                B1.setBackgroundResource(R.drawable.button);

            }

        });
        Image_add.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                initiatePopupWindow(v);
            }
        });
    }

    private void initiatePopupWindow(View v) {
        RelativeLayout mainLayout = (RelativeLayout)
                findViewById(R.id.findlayout);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popwindow, null);

        // create the popup window
        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        ImageButton I_close = (ImageButton) popupView.findViewById(R.id.ib_close);
        EditText time_input = (EditText) popupView.findViewById(R.id.editText);
        Button finish_button = (Button) popupView.findViewById(R.id.button3);



        // show the popup window
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched

        I_close.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if(selectedBox==1)
                    showtime.setText("1號刷刷櫃所預約日期為 "+store_date+" "+store_time);
                else
                    showtime.setText("2號刷刷櫃所預約日期為 "+store_date+" "+store_time);
            }
        });

        finish_button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if(selectedBox==1)
                    showtime.setText("1號刷刷櫃所預約日期為 "+store_date+" "+store_time);
                else
                    showtime.setText("2號刷刷櫃所預約日期為 "+store_date+" "+store_time);
                createNews("asdasd",store_date+" "+store_time,1,"123456");
            }
        });

        time_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    store_time = s.toString();
                }catch (NumberFormatException e){
                    store_time = "";
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }

        });

    }

    public void Calendaraction(){
        myCalendarview.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (selected && date != null){
                    Log.d(TAG, "date = " + date.getDate());
                    store_date = getSelectedDatesString(date.getDate());
                    Toast.makeText(getApplicationContext(),getSelectedDatesString(date.getDate()),Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"nothing",Toast.LENGTH_SHORT).show();

            }
        });

    }

    public String getSelectedDatesString(Date date) {
        if (date == null) {
            return "No Selection";
        }
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        Log.d(TAG, "date = " + dateFormat.format(date));
        return dateFormat.format(date);
    }

    public void createNews(String userid, String Time, int locker,String faceid) {
        final DBTime newsItem = new DBTime();

        newsItem.setUserId(userid);

        newsItem.setTime(Time);
        newsItem.setlocker(locker);
        newsItem.setfaceid(faceid);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(newsItem);
                // Item saved
            }
        }).start();
    }

}
