package com.hasid.spryoxassignment;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_submit;

    private DownloadManager mgr=null;
    private long lastDownload=-1L;
    public AlarmManager alarmManager = null;
    public static MainActivity instance;
    String storeDir=Environment.getExternalStorageDirectory().toString();;
    ArrayList<String> imagesURL = new ArrayList<>();
    Spinner spin;
    EditText et_min,et_sec;
    int min=0,sec=0;
    DatabaseHandler db;
    TextView tv_addmore;
    ArrayAdapter<String> aa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spin = findViewById(R.id.spn_link);
        et_min =  findViewById(R.id.et_min);
        et_sec = findViewById(R.id.et_sec);
        tv_addmore = findViewById(R.id.tv_addmore);
        instance=this;
        db=new DatabaseHandler(this);
        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        btn_submit=findViewById(R.id.btn_submit);
        tv_addmore.setPaintFlags(tv_addmore.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        List<Link> link=db.getAllLink();
        for (int i=0;i<link.size();i++){
            imagesURL.add(db.getAllLink().get(i)._link);
        }
         aa = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, imagesURL);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        tv_addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     final AlertDialog dialogBuilder = new AlertDialog.Builder(MainActivity.this).create();
                             LayoutInflater inflater = getLayoutInflater();
                             View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                             final EditText editText = (EditText) dialogView.findViewById(R.id.edt_link);
                             Button buttonSubmit = (Button) dialogView.findViewById(R.id.buttonSubmit);
                             Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

                             buttonCancel.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     dialogBuilder.dismiss();
                                 }
                             });
                             buttonSubmit.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     // DO SOMETHINGS
                                     db.addLink(new Link(editText.getText().toString().trim()));
                                     aa.notifyDataSetChanged();
                                     dialogBuilder.dismiss();
                                 }
                             });

                             dialogBuilder.setView(dialogView);
                             dialogBuilder.show();

            }
        });
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

