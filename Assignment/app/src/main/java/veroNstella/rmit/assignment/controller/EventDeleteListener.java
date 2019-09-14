package veroNstella.rmit.assignment.controller;

import android.content.Context;
import android.view.View;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.event.Event;

public class EventDeleteListener implements View.OnClickListener {
    private int eventId;
    private Model model;
    private Event event;

    public EventDeleteListener(Context context, int eventId) {
        this.model = ModelImpl.getSingletonInstance(context);
        event = model.getEventById(eventId);
        this.eventId = eventId;
    }

    @Override
    public void onClick(View v) {
        model.removeEventById(eventId);
        model.removeNotificationIfExist(event.getEventId());
    }
}