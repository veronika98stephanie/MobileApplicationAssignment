package veroNstella.rmit.assignment.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.Utility;

public class CancelCancelNotificationListener implements DialogInterface.OnClickListener {
    private Activity activity;
    private String eventId;
    private int notificationId;
    private int remindAgainDuration;
    private Model model;

    public CancelCancelNotificationListener(Activity activity, String eventId, int notificationId) {
        this.activity = activity;
        this.eventId = eventId;
        this.notificationId = notificationId;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        this.remindAgainDuration = Integer.parseInt(sharedPreferences.getString("remind_again", "5"));
        this.model = ModelImpl.getSingletonInstance(activity);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        model.addRemindAgainEvent(eventId);
        Utility.startRemindAgainAlarm(activity, remindAgainDuration, notificationId, eventId);
        Utility.cancelNotification(activity, notificationId);
    }
}
