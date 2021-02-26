package com.example.alan_lin.smart_locker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AdduserActivity extends Activity {

    ImageView getphoto;
    ImageView settime;
    private PopupWindow mPopupWindow;
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;
    String store_time="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);
        initial();
        getphotoaction();
        settimeaction();
    }

    public void initial(){
        getphoto = (ImageView) findViewById(R.id.imageView3);
        settime = (ImageView) findViewById(R.id.imageView);
        mContext = getApplicationContext();
        mActivity = AdduserActivity.this;
        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.mpopwindow);
    }

    public void getphotoaction(){
        getphoto.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);

            }

        });
    }

    public void settimeaction(){
        settime.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                initiatePopupWindow(v);
            }

        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                getphoto.setImageBitmap(scaleDown(selectedImage,256,true));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }


    private void initiatePopupWindow(View v) {
        RelativeLayout mainLayout = (RelativeLayout)
                findViewById(R.id.adduser);

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
        TextView word = (TextView) popupView.findViewById(R.id.tv);
        word.setText("輸入好友領取時間");


        // show the popup window
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched

        I_close.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        finish_button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Toast.makeText(getApplicationContext(),"以新增好友領取時間: "+store_time,Toast.LENGTH_SHORT);
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

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
