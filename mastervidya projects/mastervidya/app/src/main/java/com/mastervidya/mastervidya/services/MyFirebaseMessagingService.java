package com.mastervidya.mastervidya.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spanned;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mastervidya.mastervidya.R;
import com.mastervidya.mastervidya.helper.SessionHandler;
import com.mastervidya.mastervidya.ui.Homepage;

import java.net.URL;


import static android.app.Notification.DEFAULT_ALL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    SessionHandler sessionManager;
    Spanned title;
    Spanned body;
    String image;
    String path;
    Bitmap bitmap;

    public MyFirebaseMessagingService()
    {
        
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0)
        {
            sessionManager=new SessionHandler(getApplicationContext());
            if(sessionManager.isLoggedIn())
            {
                try
                {
                    title = Html.fromHtml(remoteMessage.getData().get("title"));
                    body = Html.fromHtml(remoteMessage.getData().get("body"));
                    image = remoteMessage.getData().get("image");
                    path = remoteMessage.getData().get("path");

                    if(image!=null && image.length()>0)
                    {
                        bitmap = getBitmapfromUrl(image);
                        if(bitmap!=null)
                        {
                            sendNotification(title,body,bitmap,path);
                        }
                        else
                        {
                            sendNotification(title,body,path);
                        }
                    }
                    else
                    {
                        sendNotification(title,body,path);
                    }

                }
                catch (Exception ignored)
                {

                }
            }

        }
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(imageUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        catch (Exception ignored)
        {
            return null;
        }
    }


    private void sendNotification(Spanned title, Spanned body, String path)
    {
        try {

            Intent intent;
            intent = new Intent(this, Homepage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            String channelId = "Notification";
            String channelName = "mastervidya";
            NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo1)
                    .setColor(ContextCompat.getColor(this, R.color.colorprimary))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(body))
                    .setGroupSummary(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(DEFAULT_ALL);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH);

                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);
                notificationBuilder.setChannelId(channelId);
            }

            assert notificationManager != null;
            notificationManager.notify(0, notificationBuilder.build());
        }
        catch (Exception ignored)
        {

        }
    }

    private void sendNotification(Spanned title, Spanned body, Bitmap bitmap, String path)
    {
        try {

            Intent intent;
            intent = new Intent(this, Homepage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            String channelId = "Notification";
            String channelName = "myKit";
            NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo1)
                    .setColor(ContextCompat.getColor(this, R.color.colorprimary))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(bitmap)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(body))
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(bitmap).setBigContentTitle(title).setSummaryText(body))
                    .setGroupSummary(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(DEFAULT_ALL);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH);

                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);
                notificationBuilder.setChannelId(channelId);
            }

            assert notificationManager != null;
            notificationManager.notify(0, notificationBuilder.build());
        }
        catch (Exception ignored)
        {

        }
    }

}