package veroNstella.rmit.assignment.controller;

import android.app.Activity;
import android.view.View;

public class EventCancelEditListener implements View.OnClickListener {
    private Activity context;

    public EventCancelEditListener(Activity activity) {
        this.context = activity;
    }

    @Override
    public void onClick(View v) {
        context.finish();
    }
}
