package veroNstella.rmit.assignment.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import veroNstella.rmit.assignment.model.Utility;

public class NotificationReceiverService extends Service {
    private SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utility.cancelAlarm(this);
        int notificationPeriod = Integer.parseInt(prefs.getString("notification_period", "5"));
        Utility.startAlarm(notificationPeriod, this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
