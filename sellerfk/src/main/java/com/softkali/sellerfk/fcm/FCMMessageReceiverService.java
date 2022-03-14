package com.softkali.sellerfk.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.softkali.sellerfk.App;
import com.softkali.sellerfk.R;
import com.softkali.sellerfk.dashboard.DashboardActivity;

public class FCMMessageReceiverService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("abcd","onMessageReceived");
        Log.e("abcd",remoteMessage.getFrom());

        if (remoteMessage.getData()!=null){
            String title=remoteMessage.getData().get("title");
            String body=remoteMessage.getData().get("body");

            Intent intent=new Intent(this,DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent=PendingIntent.getActivities(this,0, new Intent[]{intent},PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification=new NotificationCompat.Builder(this, App.FCM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_account_box_24)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            int randomNum = 1 + (int)(Math.random() * 1000);

            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(randomNum,notification);


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
