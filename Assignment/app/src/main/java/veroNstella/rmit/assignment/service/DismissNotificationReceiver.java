package veroNstella.rmit.assignment.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.Utility;

public class DismissNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Model model = ModelImpl.getSingletonInstance(context);
        model.addDismissedEvent(intent.getStringExtra("eventId"));
        Utility.cancelNotification(context, intent.getIntExtra("notificationId", -1));
    }
}
