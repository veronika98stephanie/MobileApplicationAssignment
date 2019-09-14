package veroNstella.rmit.assignment.controller;

import android.app.Activity;
import android.content.DialogInterface;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.Utility;

public class ConfirmCancelNotificationListener implements DialogInterface.OnClickListener {
    private Activity activity;
    private String eventId;
    private int notificationId;
    private Model model;

    public ConfirmCancelNotificationListener(Activity activity, String eventId, int notificationId) {
        this.activity = activity;
        this.eventId = eventId;
        this.notificationId = notificationId;
        this.model = ModelImpl.getSingletonInstance(activity);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        model.removeEventByEventId(eventId);
        Utility.cancelNotification(activity, notificationId);
    }
}
