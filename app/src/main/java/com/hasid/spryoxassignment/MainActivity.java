package com.hasid.spryoxassignment;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button btn_submit;

    private DownloadManager mgr=null;
    private long lastDownload=-1L;
    public AlarmManager alarmManager = null;
    public static MainActivity instance;
    String storeDir=Environment.getExternalStorageDirectory().toString();;
    String[] imagesURL = { "https://iso.500px.com/wp-content/uploads/2016/04/stock-photo-150595123-1500x1000.jpg", "https://images.unsplash.com/photo-1487035242901-d419a42d17af?ixlib=rb-1.2.1&w=1000&q=80"
            , "data:image/jpeg;base64,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"
            , "https://www.thewowstyle.com/wp-content/uploads/2015/02/Beautiful-Wallpapers-14.jpg"};
    Spinner spin;
    EditText et_min,et_sec;
    int min=0,sec=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spin = findViewById(R.id.spn_link);
        et_min =  findViewById(R.id.et_min);
        et_sec = findViewById(R.id.et_sec);
        instance=this;
        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,imagesURL);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        btn_submit=findViewById(R.id.btn_submit);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_min.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter minute",Toast.LENGTH_LONG).show();
                }else if (et_sec.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter second",Toast.LENGTH_LONG).show();
                }else {
                    Alarm a = new Alarm();
                    a.setAlarm(MainActivity.this, sec, min, spin.getSelectedItem().toString());
                    Toast.makeText(getApplicationContext(), "Download will start in " + et_min.getText().toString() + "min " + et_sec.getText().toString() + "sec", Toast.LENGTH_SHORT).show();
                    et_min.setText("");
                    et_sec.setText("");
                }
            }
        });

    }



    public void downloadManager(Context c) {
        mgr = (DownloadManager) c.getSystemService(DOWNLOAD_SERVICE);
        c.registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        c.registerReceiver(onNotificationClick,
                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }
    public void startDownload(Context context,String url) {
        downloadManager(context);
        Uri uri=Uri.parse(url);

        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .mkdirs();



        lastDownload=
                mgr.enqueue(new DownloadManager.Request(uri)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("Downloading Image")
                        .setDescription("Image")
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                getFileNameFromURL(url)));



    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
        unregisterReceiver(onNotificationClick);

    }

    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            final MediaPlayer mp = MediaPlayer.create(ctxt, R.raw.sound);
            mp.start();
        }
    };

    BroadcastReceiver onNotificationClick=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {

        }
    };

    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        }
        catch(MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }
    }

