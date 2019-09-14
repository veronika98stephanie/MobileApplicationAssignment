package veroNstella.rmit.assignment.controller;

import java.util.Comparator;

import veroNstella.rmit.assignment.model.event.Event;

public class EventDateComparator implements Comparator<Event> {
    private static int REVERSE_ORDER = 0;

    public EventDateComparator(int order) {
        REVERSE_ORDER = order;
    }

    @Override
    public int compare(Event o1, Event o2) {
        if (REVERSE_ORDER == 1) {
            return o1.getStartDate().compareTo(o2.getStartDate());
        } else {
            return o2.getStartDate().compareTo(o1.getStartDate());
        }
    }
}