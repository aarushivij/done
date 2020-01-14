package com.example.android.done;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String channelID = "Reminder";
    public static final String channelName = "Reminder";
    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channel.setLightColor(R.color.colorPrimary);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager ()
    {
        if(mManager == null)
        {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification (String title , String message)
    {

        return new NotificationCompat.Builder(getApplicationContext() , channelID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_check_black_24dp);
    }


}