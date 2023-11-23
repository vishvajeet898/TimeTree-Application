package com.example.timetreeapplication.broadcastReceiver;

import static android.provider.Settings.System.getString;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.codegama.timetreeapplication.R;
import com.example.timetreeapplication.activity.AlarmActivity;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    String title, desc, date, time;

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Alarm just rang...", Toast.LENGTH_SHORT).show();

        title = intent.getStringExtra("TITLE");
        desc = intent.getStringExtra("DESC");
        date = intent.getStringExtra("DATE");
        time = intent.getStringExtra("TIME");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            // Set the alarm here.
            Toast.makeText(context, "Alarm just rang...", Toast.LENGTH_SHORT).show();
        }


        Intent i = new Intent(context, AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "123")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Name")
                .setContentText("Name")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(123, notification.build());
        Toast.makeText(context, "Broadcast receiver called", Toast.LENGTH_SHORT).show();





  /*      NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(200, notification.build());
        */

        Toast.makeText(context, "Broadcast receiver called", Toast.LENGTH_SHORT).show();
/*
        Intent i = new Intent(context, AlarmActivity.class);
        i.putExtra("TITLE", title);
        i.putExtra("DESC", desc);
        i.putExtra("DATE", date);
        i.putExtra("TIME", time);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,PendingIntent.FLAG_MUTABLE);
        notification.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSs

        context.startActivity(i);*/
    }


    }

