package com.softices.traineeapp.firebaseService;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Amit on 19/07/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private String TAG = "FirebaseMessagingService";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e(TAG, "" + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String msg = remoteMessage.getNotification().getBody();
        Log.e(TAG, "" + msg);
        Toast.makeText(this, msg+"", Toast.LENGTH_SHORT).show();
//        sendNotification(msg);
    }

//    public void sendNotification(String msg) {
//        Intent intent = new Intent(this, SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        String CHANNEL_ID = "my_channel_01";// The id of the channel.
//        CharSequence name = "Channel Name";// The user-visible name of the channel.
//        int importance = NotificationManager.IMPORTANCE_HIGH;
//        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//
//        // Create a notification and set the notification channel.
//        Notification notification = new Notification.Builder(this)
//                .setContentTitle("New Message")
//                .setContentText("You've received new messages.")
//                .setSmallIcon(R.drawable.ic_notification_24dp)
//                .setChannelId(CHANNEL_ID)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        //If API > 26.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationManager.createNotificationChannel(mChannel);
//        }
//        notificationManager.notify(0, notification);
//    }
}
