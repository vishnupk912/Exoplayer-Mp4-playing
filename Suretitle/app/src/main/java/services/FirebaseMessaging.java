package services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.suretitle.www.DashBoard;
import com.suretitle.www.R;
import com.suretitle.www.SplashScreen;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import helper.SessionHandler;

public class FirebaseMessaging extends FirebaseMessagingService
{
    String FCM="",message,image,image_status,path="";
    String title ;
    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            FCM = remoteMessage.getData().get("FCM");
            message = remoteMessage.getData().get("body");
            title = remoteMessage.getData().get("title");
            image = remoteMessage.getData().get("image");
            image_status = remoteMessage.getData().get("image_status");
            path = remoteMessage.getData().get("path");

            System.out.println("image "+image);
            System.out.println("image_status "+image_status);

            if(FCM.equals("Success"))
            {

                if(new SessionHandler(getApplicationContext()).isLoggedIn())
                {
                    if(image_status.equals("true"))
                    {
                        sendNotification(message,title);

                    }
                    else
                    {
                        sendNotification(message,title);
                    }
                }

            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
        }
    }





    private void sendNotification(String message, String title) {
        try {
            NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this, "Notification")
                    .setSmallIcon(R.drawable.logosure)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setWhen(System.currentTimeMillis())
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setVibrate(new long[] {1000})
                    .setGroupSummary(true);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }

        catch (Exception e)
        {

        }

    }

}
