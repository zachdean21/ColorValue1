package com.google.developer.colorvalue;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.google.developer.colorvalue.service.NotificationJobService;

public class SettingsActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    private static final int JOB_ID = 88;

    private static final long ONE_DAY_MILLISECONDS = 1 * 24 * 60 * 60 * 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        final String notifyKey = getString(R.string.pref_key_notification);
        if (key.equals(notifyKey)) {
            boolean on = sharedPreferences.getBoolean(notifyKey, false);
            JobScheduler jobScheduler =
                    (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            // TODO implement JobScheduler for notification {@link ScheduledJobService}

            // Create Intent for the PracticeReceiver
            Intent intent = new Intent(this, PracticeReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if(on) {
                /*ComponentName componentName = new ComponentName(this, NotificationJobService.class);
                JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                        .setPeriodic(ONE_DAY_MILLISECONDS)
                        .setPersisted(true)
                        .build();
                jobScheduler.schedule(jobInfo);*/

                // Use Alarm Manager
                long currentTimeInMilliseconds = SystemClock.elapsedRealtime();
                long firstAlarmTime = currentTimeInMilliseconds + ONE_DAY_MILLISECONDS;
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstAlarmTime, ONE_DAY_MILLISECONDS, pendingIntent);

            } else {
                //jobScheduler.cancel(JOB_ID);

                alarmManager.cancel(pendingIntent);
            }
        }
    }

    /**
     * loading setting resource
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preference);
        }

    }
}