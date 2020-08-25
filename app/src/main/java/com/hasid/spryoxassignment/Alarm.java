package com.hasid.spryoxassignment;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import static android.content.Context.ALARM_SERVICE;

public class Alarm extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        Intent myIntent = new Intent(context, MyService.class);

        myIntent.putExtra("link",intent.getExtras().getString("link"));
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);


        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, 1000*2, pendingIntent);
        context.startService(myIntent);


        // Put here YOUR code.
        Log.d("Intent",intent.getExtras().getString("link"));

        wl.release();

    }

    public void setAlarm(Context context,int sec,int min,String link)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        i.putExtra("link",link);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.set(AlarmManager.RTC_WAKEUP,  1000 * 10    , pi); // Millisec * Second * Minute
    }


}
