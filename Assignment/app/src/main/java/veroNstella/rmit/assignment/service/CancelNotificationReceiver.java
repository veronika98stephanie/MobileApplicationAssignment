package veroNstella.rmit.assignment.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import veroNstella.rmit.assignment.view.DialogActivity;

public class CancelNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent cancelServiceIntent = new Intent(context, DialogActivity.class);
        cancelServiceIntent.putExtra("eventId", intent.getStringExtra("eventId"));
        cancelServiceIntent.putExtra("notificationId", intent.getIntExtra("notificationId", -1));
        context.startActivity(cancelServiceIntent);
    }
}
