package veroNstella.rmit.assignment.controller;

import android.app.Activity;
import android.view.MenuItem;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;

public class EventSortListener implements MenuItem.OnMenuItemClickListener {
    private Model model;

    public EventSortListener(Activity activity) {
        this.model = ModelImpl.getSingletonInstance(activity);
    }

    // set order to ascending or descending and order the event
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        model.setOrder();
        model.orderEvent();
        return false;
    }
}
