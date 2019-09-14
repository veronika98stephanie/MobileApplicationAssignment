package veroNstella.rmit.assignment.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStateReceiver extends BroadcastReceiver {

    private static final String tag = "NetworkStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        ApplicationServices applicationServices = new ApplicationServices(context);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        //should check null because in airplane mode it will be null
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Log.d(tag, "Network Available");
            applicationServices.restartNotificationService();
            Intent i = new Intent(context, NotificationReceiver.class);
            context.sendBroadcast(i);
        } else {
            Log.d(tag, "Network unavailable?");
            applicationServices.stopNotificationService();
        }
    }
}
