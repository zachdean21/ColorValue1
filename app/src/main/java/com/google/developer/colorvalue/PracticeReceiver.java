package com.google.developer.colorvalue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.developer.colorvalue.service.NotificationJobService;
import com.google.developer.colorvalue.service.PracticeNotification;

public class PracticeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        PracticeNotification.notify(context, NotificationJobService.NOTIFICATION_ID);

    }
}
