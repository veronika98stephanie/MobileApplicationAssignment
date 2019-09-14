package veroNstella.rmit.assignment.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.view.EventEditActivity;

public class EventAddListener implements MenuItem.OnMenuItemClickListener {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private Model model;

    public EventAddListener(Context context) {
        this.context = context;
        model = ModelImpl.getSingletonInstance(context);
    }

    // go to EventEditActivity to add new event
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent eventDetailIntent = new Intent(context, EventEditActivity.class);
        eventDetailIntent.putExtra("activity", "add");
        eventDetailIntent.setType("text/plain");

        if (eventDetailIntent.resolveActivity(context.getPackageManager()) != null) {
            model.resetTemporaryData();
            context.startActivity(eventDetailIntent);
        } else {
            Log.i(TAG, "Cannot open activity for this intent");
        }
        return false;
    }
}
