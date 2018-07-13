package com.google.developer.colorvalue.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.developer.colorvalue.MainActivity;
import com.google.developer.colorvalue.R;

public class PracticeNotification {

    private static final String NOTIFICATION_TAG = "Practice";
    private static final String CHANNEL_ID = "PracticeChannel";

    public static void notify(final Context context, int notificationID) {

        Log.d("NotificationJobService", "Notification Displayed");

        final Resources res = context.getResources();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)

                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_stat_practice)
                .setContentTitle(context.getString(R.string.time_to_practice))
                .setTicker(context.getString(R.string.time_to_practice))
                .setContentText(context.getString(R.string.it_is_time_to_practice))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(context, MainActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(true);
        notify(context, builder.build(), notificationID);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification, int notificationID) {
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, notificationID, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context, int notificationID) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, notificationID);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
