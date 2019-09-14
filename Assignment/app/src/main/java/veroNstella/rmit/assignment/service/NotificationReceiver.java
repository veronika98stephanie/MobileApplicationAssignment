package veroNstella.rmit.assignment.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import veroNstella.rmit.assignment.asyncTask.GeolocationAsyncTask;
import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.Utility;
import veroNstella.rmit.assignment.model.event.Event;

public class NotificationReceiver extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();
    protected SharedPreferences prefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int notificationPeriod = Integer.parseInt(prefs.getString("notification_period", "5"));
        Model model = ModelImpl.getSingletonInstance(context);
        List<Event> events = model.getEventList();

        Double myLatitude = model.getLatitude();
        Double myLongitude = model.getLongitude();

        List<String> dismissedEventsIds = model.getAllDismissedEvent();
        List<String> remindAgainEventsIds = model.getAllRemindAgainEvent();

        int notificationId = 0;

        for (Event event : events) {
            boolean eventHasBeenDismissed = false;
            boolean eventIsToBeReminded = false;
            for (String dismissedEventId : dismissedEventsIds) {
                if (event.getEventId().equals(dismissedEventId)) {
                    eventHasBeenDismissed = true;
                    break;
                }
            }
            for (String remindAgainEventId : remindAgainEventsIds) {
                if (event.getEventId().equals(remindAgainEventId)) {
                    eventIsToBeReminded = true;
                    break;
                }
            }
            if (!eventHasBeenDismissed && !eventIsToBeReminded) {
                String[] eventLocation = event.getLocation().split(",");
                Double eventLatitude = Double.parseDouble(eventLocation[0].trim());
                Double eventLongitude = Double.parseDouble(eventLocation[1].trim());

                String str_from = myLatitude + "," + myLongitude;
                String str_to = eventLatitude + "," + eventLongitude;

                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?" +
                        "units=imperial&mode=driving&origins=" + str_from + "&destinations=" + str_to +
                        "&key=AIzaSyBk0ddUYkQScNx9hbmUdF_ulLfUmNKjJC8";
                new GeolocationAsyncTask(context, event, notificationId).execute(url);
            } else {
                Log.i(TAG, "onReceive: event is dismissed or need to be reminded again, event id = "
                        + event.getEventId());
            }
            notificationId++;
        }
        Utility.startAlarm(notificationPeriod, context);
    }
}
