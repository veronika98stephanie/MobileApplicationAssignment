package veroNstella.rmit.assignment.controller;

import android.content.Context;
import android.view.View;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;

public class AttendeeRemoveListener implements View.OnClickListener {
    private int attendeeId;
    private Model model;

    public AttendeeRemoveListener(Context context, int index) {
        this.attendeeId = index;
        this.model = ModelImpl.getSingletonInstance(context);
    }

    @Override
    public void onClick(View v) {
        model.removeAttendeeById(attendeeId);
    }
}
