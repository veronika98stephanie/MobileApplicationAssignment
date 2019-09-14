package veroNstella.rmit.assignment.controller;

import android.content.Context;
import android.view.View;

import java.util.Calendar;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.view.CalendarActivity;

public class CalendarUpdateListener implements View.OnClickListener {
    private CalendarActivity calendarActivity;
    private Model model;
    private Calendar currentCalendar;
    private int amount;

    public CalendarUpdateListener(Context context, Calendar currentCalendar, int amount) {
        this.calendarActivity = (CalendarActivity) context;
        model = ModelImpl.getSingletonInstance(context);
        this.currentCalendar = currentCalendar;
        this.amount = amount;
    }

    // notify the Calender activity to update the view
    // the calendar updated based on the clicked symbols (determined by the amount passed)
    @Override
    public void onClick(View v) {
        model.clearEventOnSelectedDate();
        currentCalendar.add(Calendar.MONTH, amount);
        calendarActivity.updateCalendar();
    }
}
