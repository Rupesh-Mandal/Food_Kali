package com.softkali.sellerkf.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.softkali.sellerkf.App;
import com.softkali.sellerkf.R;
import com.softkali.sellerkf.dashboard.DashboardActivity;

public class FCMMessageReceiverService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("abcd","onMessageReceived");
        Log.e("abcd",remoteMessage.getFrom());

        if (remoteMessage.getData()!=null){
            String title=remoteMessage.getData().get("title");
            String body=remoteMessage.getData().get("body");

            Notification notification=new NotificationCompat.Builder(this, App.FCM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_account_box_24)
                    .setContentTitle(title)
                    .setContentText(body)
                    .build();

            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1332,notification);


        }


    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.e("abcd","onDeletedMessages");

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("abcd","onNewToken");

    }
}
