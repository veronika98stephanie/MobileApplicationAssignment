package veroNstella.rmit.assignment.model.event;

import java.util.Date;
import java.util.List;

import veroNstella.rmit.assignment.model.attendee.Attendee;
import veroNstella.rmit.assignment.model.movie.Movie;

public interface Event {
    String getEventId();

    void setEventId(String eventId);

    String getTitle();

    void setTitle(String title);

    Date getStartDate();

    void setStartDate(Date startDate);

    Date getEndDate();

    void setEndDate(Date endDate);

    String getVenue();

    void setVenue(String venue);

    String getLocation();

    void setLocation(String location);

    List<Attendee> getAttendees();

    void setAttendees(List<Attendee> attendees);

    Movie getMovie();

    void setMovie(Movie movie);
}
