package veroNstella.rmit.assignment.model;

import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import veroNstella.rmit.assignment.exception.InvalidDateException;
import veroNstella.rmit.assignment.model.attendee.Attendee;
import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.model.movie.Movie;

public interface Model {
    void updateEvent(int eventId, String title,
                     String startDate, String endDate, String venue,
                     String location) throws ParseException;

    void resetTemporaryData();

    List<Event> getEventList();

    List<Event> getEvents(int count);

    Event getEventById(int eventId);

    void removeEventById(int eventId);

    void addEvent(String eventId, String title,
                  String startDate, String endDate, String venue,
                  String location);

    void updateEventOnSelectedDate();

    List<Event> getEventOnSelectedDate();

    void clearEventOnSelectedDate();

    List<Movie> getMovieList();

    Movie getMovieById(int movieId);

    void addMovie(int index);

    List<Attendee> getAttendeeList();

    void addAttendee(String name);

    void removeAttendeeById(int attendeeId);

    void setAttendeesList(int eventId);

    String getStartDate();

    void setStartDate(String date);

    String getEndDate();

    void setEndDate(String date);

    void setSelectedDateEvents(Calendar calendar);

    void validateLocation(String location);

    void validateDate() throws InvalidDateException, ParseException;

    void addPropertyChangeListener(PropertyChangeListener newListener);

    String createUUID();

    void orderEvent();

    void setOrder();

    void setMovies(List<Movie> movies);

    void setEvents(List<Event> events);

    void setLatitude(Double latitude);

    void setLongitude(Double longtitude);

    Double getLatitude();

    Double getLongitude();

    void removeEventByEventId(String eventId);

    Event getEventByEventId(String eventId);

    void addDismissedEvent(String eventId);

    List<String> getAllDismissedEvent();

    void addRemindAgainEvent (String eventId);

    List<String> getAllRemindAgainEvent();

    void addEventNotificationPair(String eventId, int id);

    void removeNotificationIfExist(String eventId);
}
