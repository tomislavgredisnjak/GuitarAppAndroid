package hr.tvz.android.MVPgredisnjak;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MojFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = " FCM Service";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        posaljiNotifikaciju(remoteMessage.getNotification().getBody());
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "To: " + remoteMessage.getTo());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void posaljiNotifikaciju(String messageBody) {
        Notification.Builder builder = new Notification.Builder(this, MainActivity.MOJ_KANAL)
                .setSmallIcon(android.R.drawable.ic_menu_save)
                .setContentTitle(messageBody)
                .setTicker("Akcija!")
                .setSubText("Sjajna prilika!")
                .setAutoCancel(true);

        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle();
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.guitars));
        bigPictureStyle.setBigContentTitle("Epiphone Les Paul!");
        bigPictureStyle.setSummaryText(messageBody);

        Intent resultIntent = new Intent(this, ShowMessage.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setStyle(bigPictureStyle);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);
    }

    @Override
    public void onNewToken(String token){
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

    }
}
