package veroNstella.rmit.assignment.controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import veroNstella.rmit.assignment.view.SettingsActivity;

public class SettingsListener implements MenuItem.OnMenuItemClickListener {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;

    public SettingsListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Intent settingsIntent = new Intent(activity, SettingsActivity.class);

        if (settingsIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(settingsIntent);
        } else {
            Log.i(TAG, "Cannot open activity for this intent");
        }

        return false;
    }
}
