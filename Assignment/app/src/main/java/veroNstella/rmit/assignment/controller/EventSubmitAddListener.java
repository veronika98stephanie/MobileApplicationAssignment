package veroNstella.rmit.assignment.controller;

import android.app.Activity;
import android.text.Editable;
import android.view.View;

import java.text.ParseException;

import veroNstella.rmit.assignment.exception.InvalidDateException;
import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.view.EventEditActivity;

public class EventSubmitAddListener implements View.OnClickListener {
    private EventEditActivity context;
    private String eventId;
    private Editable title, venue, location;
    private Model model;

    public EventSubmitAddListener(Activity activity, String eventId, Editable title, Editable venue,
                                  Editable location) {
        this.context = (EventEditActivity) activity;
        this.eventId = eventId;
        this.title = title;
        this.venue = venue;
        this.location = location;
        model = ModelImpl.getSingletonInstance(activity);
    }

    @Override
    public void onClick(View v) {
        try {
            model.validateDate();
            model.validateLocation(location.toString());
            model.addEvent(eventId, title.toString(), model.getStartDate(),
                    model.getEndDate(), venue.toString(), location.toString());
            context.makeToast("Event added");
            model.orderEvent();
            context.finish();
        } catch (NumberFormatException e) {
            context.makeToast("False location format, failed update");
        } catch (ArrayIndexOutOfBoundsException e) {
            context.makeToast("False location format, failed update");
        } catch (NullPointerException e) {
            context.makeToast("Fill out the event detail");
        } catch (InvalidDateException e) {
            context.makeToast(e.getMessage());
        } catch (ParseException e) {
            context.makeToast("Invalid date format");
        }
    }
}
