package veroNstella.rmit.assignment.controller;

import android.app.Activity;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;

public class AttendeeController {
    private Model model;

    public AttendeeController(Activity activity) {
        this.model = ModelImpl.getSingletonInstance(activity);
    }

    public void addNewAttendee(String name) {
        model.addAttendee(name);
    }
}
