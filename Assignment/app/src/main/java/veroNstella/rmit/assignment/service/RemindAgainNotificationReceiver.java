package veroNstella.rmit.assignment.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.concurrent.TimeUnit;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.Utility;

public class RemindAgainNotificationReceiver extends BroadcastReceiver {
    private Context context;
    private int notificationId;
    private String eventId;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Model model = ModelImpl.getSingletonInstance(context);
        model.addRemindAgainEvent(intent.getStringExtra("eventId"));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int remindAgain = Integer.parseInt(prefs.getString("remind_again", "5"));
        this.notificationId = intent.getIntExtra("notificationId", -1);
        this.eventId = intent.getStringExtra("eventId");
        startAlarm(remindAgain);
        Utility.cancelNotification(context, notificationId);
    }

    private void startAlarm(int minutes) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, RemindAgainReceiver.class);
        intent.putExtra("eventId", eventId);
        intent.putExtra("notificationId", notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent,
                PendingIntent.FLAG_ONE_SHOT);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                    TimeUnit.MINUTES.toMillis(minutes), pendingIntent);
        }
    }
}
