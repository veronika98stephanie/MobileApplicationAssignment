package veroNstella.rmit.assignment.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.view.CreateNotification;

public class RemindAgainReceiver extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Model model = ModelImpl.getSingletonInstance(context);
        int notificationId = intent.getIntExtra("notificationId", -1);

        CreateNotification createNotification = new CreateNotification(context);
        try{
            Event event = model.getEventByEventId(intent.getStringExtra("eventId"));
            createNotification.sendNotification(event.getEventId(), event.getTitle(),
                    event.getVenue() + ", at " + event.getStartDate().toString(),
                    notificationId);
        } catch (NullPointerException e) {
            Log.i(TAG, "onReceive: Data has been deleted from the application");
        }
    }
}
