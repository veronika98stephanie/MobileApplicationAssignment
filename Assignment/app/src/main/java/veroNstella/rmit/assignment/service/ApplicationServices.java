package veroNstella.rmit.assignment.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class ApplicationServices {
    private static Intent networkService = null;
    private static Intent notificationService = null;
    private final String TAG = getClass().getSimpleName();
    private Context context;


    public ApplicationServices(Context context) {
        this.context = context;
    }

    public void createService() {
        Log.i(TAG, "onCreate: " + networkService + notificationService);
        if (networkService == null) {
            networkService = new Intent(context, NetworkReceiverService.class);
            ContextCompat.startForegroundService(context, networkService);
        }

        if (notificationService == null) {
            notificationService = new Intent(context, NotificationReceiverService.class);
            ContextCompat.startForegroundService(context, notificationService);
        }
    }

    public void restartNotificationService() {
        try {
            context.stopService(notificationService);
            ContextCompat.startForegroundService(context, notificationService);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    void stopNotificationService() {
        try {
            context.stopService(notificationService);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
