package com.kamil.permissions;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

//http://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en
public class MainActivity extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("text", "Elena");
        editor.putString("name", "Elena");
        editor.putInt("idName", 12);
        editor.commit();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
            int idName = prefs.getInt("idName", 0); //0 is the default value.

            Toast.makeText(MainActivity.this,name + " " + idName,Toast.LENGTH_LONG).show();
        }


        String[] perms = {"android.permission.RECORD_AUDIO", "android.permission.CAMERA"};

        int permsRequestCode = 200;
//shouldShowRequestPermissionRationale what case?

            if(ContextCompat.checkSelfPermission(this,perms[0])==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this,"GRANTED",Toast.LENGTH_LONG).show();
                ////do it here
            }
            else {
                Toast.makeText(MainActivity.this, "no GRANTED", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,perms, permsRequestCode);
            }



        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://www.google.com", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean audioAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;

                boolean cameraAccepted = grantResults[1]== PackageManager.PERMISSION_GRANTED;

                if(audioAccepted) {
                    //do it here
                    Toast.makeText(MainActivity.this, "tak", Toast.LENGTH_LONG).show();
                    int frequency = 8000;
                    int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
                    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
                    try {
                        // Create a new AudioRecord object to record the audio.
                        int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);

                        AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                                frequency, channelConfiguration,
                                audioEncoding, bufferSize);
                    } catch (Throwable t) {
                        Log.e("AudioRecord", "Recording Failed");
                    }
                }
                else
                    Toast.makeText(MainActivity.this,"nie",Toast.LENGTH_LONG).show();

                break;

        }

    }
}
