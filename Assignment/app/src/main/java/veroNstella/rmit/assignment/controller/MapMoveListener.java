package veroNstella.rmit.assignment.controller;

import android.content.Context;
import android.view.View;

import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.view.MapsActivity;

public class MapMoveListener implements View.OnClickListener {

    private Event event;
    private MapsActivity mapsActivity;

    public MapMoveListener(Context context, Event event) {
        this.mapsActivity = (MapsActivity) context;
        this.event = event;
    }

    @Override
    public void onClick(View v) {
        mapsActivity.ChangeFocus(this.event);
    }
}
