package com.example.alan_lin.smart_locker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sqs.AmazonSQSClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.amazonaws.services.sqs.model.*;


public class RegisisterActivity extends Activity {

    private final String KEY = "<Your KEY>";
    private final String SECRET = "<Your SECRET KEY>";
    private static final String TAG = "error";
    public String name;
    EditText name_ET;
    Button R_button;
    ImageView finsih_button;
    private static final int CAMERA_REQUEST = 1888;
    private String mCurrentPhototPath;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_POSTEFIX = ".jpg";
    private static final String IMAGE_DIRECTORY_NAME = "mycamera";
    File f;
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;
    AmazonSQSClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_regisister);
        // callback method to call credentialsProvider method.
        AWSMobileClient.getInstance().initialize(this).execute();
        credentials = new BasicAWSCredentials(KEY, SECRET);
        s3Client = new AmazonS3Client(credentials);
        client = new AmazonSQSClient(AWSMobileClient.getInstance().getCredentialsProvider());
        EdittextStor();
        finishbutton();
        camera();
    }

    public void takePhoto(View view){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        f = null;
        try{
            f = setUpPhtotFile();
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(f));
            startActivityForResult(takePhotoIntent,CAMERA_REQUEST);
        } catch (IOException e){
            e.printStackTrace();
            f = null;
            mCurrentPhototPath = null;
        }
    }

    private File setUpPhtotFile() throws IOException{
        File f = createImageFile();

        mCurrentPhototPath = f.getAbsolutePath();
        return  f;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            BitmapFactory.Options mOptions = new BitmapFactory.Options();
            mOptions.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhototPath, mOptions);
            Bitmap show = scaleDown(bitmap,256,true);
            finsih_button.setImageBitmap(show);

        } else {
            Toast.makeText(this,"Image capture filaed",Toast.LENGTH_SHORT);
        }
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


    private File createImageFile () throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFilename = JPEG_FILE_PREFIX + timeStamp + "_" ;
        File album = getAlbumDir();
        System.out.println(imageFilename+"   "+album);
        File imageFile = File.createTempFile(imageFilename,JPEG_FILE_POSTEFIX,album);
        return imageFile;
    }

    private File getAlbumDir(){
        File albumDir = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/");
        if(albumDir == null){
            Toast.makeText(this,"failed to create albumDir",Toast.LENGTH_SHORT);
        }
        return albumDir;
    }

    public void addPhotoToGallery (View view){
        if(mCurrentPhototPath != null){

            Intent MediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhototPath);
            Uri contentUri = Uri.fromFile(f);
            MediaScanIntent.setData(contentUri);
            this.sendBroadcast(MediaScanIntent);
            System.out.println("~~~"+mCurrentPhototPath);
            mCurrentPhototPath = null;
        }
    }

    public void camera(){
        finsih_button = (ImageView) findViewById(R.id.imageView3);
        finsih_button.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                takePhoto(v);
            }

        });
    }

    public void EdittextStor(){
        name_ET = (EditText)findViewById(R.id.editText);
    }

    public void finishbutton(){
        name_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    name = s.toString();
                }catch (NumberFormatException e){
                    name = "";
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        R_button = (Button) findViewById(R.id.button);
        R_button.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), name, Toast.LENGTH_LONG).show();
                addPhotoToGallery(v);
                //aws S3 upload
                uploadFile(name+".jpg",f);
                trigger(name+".jpg",name);
                sendmessage();
                Intent intent = new Intent();
                intent.setClass(RegisisterActivity.this , menuActivity.class);
                intent.putExtra("name_id", name);
                startActivity(intent);
            }

        });
    }



    public void trigger(String filename, String nickname){
        System.out.println("Hello world~~~~~~~~~~~~~~~~~~~~~`");


        // Create an instance of CognitoCachingCredentialsProvider
        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                this.getApplicationContext(), "us-east-1:4a579142-d4c1-4d8b-99cc-840f8ab669e2", Regions.US_EAST_1);

        // Create LambdaInvokerFactory, to be used to instantiate the Lambda proxy.
        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.US_EAST_1, cognitoProvider);

        // Create the Lambda proxy object with a default Json data binder.
        // You can provide your own data binder by implementing
        // LambdaDataBinder.
        final MyInterface myInterface = factory.build(MyInterface.class);

        RequestClass request = new RequestClass("myfristaws-userfiles-mobilehub-1927978180", filename, nickname);
        // The Lambda function invocation results in a network call.
        // Make sure it is not called from the main thread.
        new AsyncTask<RequestClass, Void, ResponseClass>() {
            @Override
            protected ResponseClass doInBackground(RequestClass... params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    return myInterface.AndroidBackendLambdaFunction(params[0]);
                } catch (LambdaFunctionException lfe) {
                    Log.e("Tag", "Failed to invoke echo", lfe);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ResponseClass result) {
                if (result == null) {
                    return;
                }

                // Do a toast
                Toast.makeText(RegisisterActivity.this, result.getGreetings()+",,"+result.getMap(), Toast.LENGTH_LONG).show();
            }
        }.execute(request);
    }


    private void uploadFile(String fileName,File file) {
        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

        // "jsaS3" will be the folder that contains the file
        TransferObserver uploadObserver =
                transferUtility.upload("Photo/input/" + fileName, file);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed download.
                    trigger(name+".jpg",name);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

        // If your upload does not trigger the onStateChanged method inside your
        // TransferListener, you can directly check the transfer state as shown here.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }
    }

    public void sendmessage(){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    String url="https://sqs.us-east-1.amazonaws.com/886766962141/3tierqueue";
                    client.sendMessage(new SendMessageRequest(url,
                            "{\"user\":\""+name+"\"}"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }
}
