package veroNstella.rmit.assignment.controller;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import java.util.Calendar;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;

public class SelectCalendarDateListener implements AdapterView.OnItemClickListener {
    private Model model;

    public SelectCalendarDateListener(Context context) {
        model = ModelImpl.getSingletonInstance(context);
    }

    // on a certain date clicked, set the events of selected date
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        model.clearEventOnSelectedDate();
        Calendar calendar = (Calendar) view.getTag();
        model.setSelectedDateEvents(calendar);
    }
}
