package com.google.developer.colorvalue.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class NotificationJobService extends JobService {

    public static final int NOTIFICATION_ID = 18;

    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d("NotificationJobService", "In On start Job");

        PracticeNotification.notify(this, NOTIFICATION_ID);

        jobFinished(params, false);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }



}