package veroNstella.rmit.assignment.model.event;

import java.util.Date;

public class EventImpl extends EventAbstract {
    public EventImpl(String eventId, String title, Date startDate, Date endDate, String venue, String location) {
        super(eventId, title, startDate, endDate, venue, location);
    }
}
