package veroNstella.rmit.assignment.controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.util.List;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.view.EventEditActivity;

public class EventEditListener implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private int eventId;
    private Activity context;
    private Model model;
    private List<Event> eventList;

    public EventEditListener(Activity activity, int eventId) {
        this.context = activity;
        this.model = ModelImpl.getSingletonInstance(activity);
        eventList = model.getEventList();
        this.eventId = eventId;
    }

    public EventEditListener(Activity context, Event event) {
        this.context = context;
        this.model = ModelImpl.getSingletonInstance(context);
        eventList = model.getEventList();
        eventId = eventList.indexOf(event);
    }

    @Override
    public void onClick(View v) {
        Intent eventDetailIntent = new Intent(context, EventEditActivity.class);
        eventDetailIntent.putExtra(Intent.EXTRA_TEXT, eventId);
        eventDetailIntent.putExtra("activity", "edit");
        eventDetailIntent.setType("text/plain");

        if (eventDetailIntent.resolveActivity(context.getPackageManager()) != null) {
            model.resetTemporaryData();
            model.setAttendeesList(eventId);
            context.startActivity(eventDetailIntent);
        } else {
            Log.i(TAG, "Cannot open activity for this intent");
        }
    }
}
