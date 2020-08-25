package com.hasid.spryoxassignment;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;


public class MyService extends Service {

    @Override
    public void onCreate() {

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        MainActivity.instance.startDownload(this,intent.getStringExtra("link"));

    }
    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }




}
