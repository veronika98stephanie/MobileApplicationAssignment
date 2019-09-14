package veroNstella.rmit.assignment.model.event;

import java.util.Date;
import java.util.List;

import veroNstella.rmit.assignment.model.attendee.Attendee;
import veroNstella.rmit.assignment.model.movie.Movie;

public abstract class EventAbstract implements Event {
    private String eventId;
    private String title;
    private Date startDate;
    private Date endDate;
    private String venue;
    private String location;
    private List<Attendee> attendees;
    private Movie movie;

    EventAbstract(String eventId, String title, Date startDate, Date endDate, String venue, String location) {
        this.eventId = eventId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.venue = venue;
        this.location = location;
        this.attendees = null;
        this.movie = null;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
