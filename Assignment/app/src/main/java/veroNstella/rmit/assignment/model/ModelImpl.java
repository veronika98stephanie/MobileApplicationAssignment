package veroNstella.rmit.assignment.model;

import android.annotation.SuppressLint;
import android.content.Context;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import veroNstella.rmit.assignment.controller.EventDateComparator;
import veroNstella.rmit.assignment.database.DataLoader;
import veroNstella.rmit.assignment.exception.InvalidDateException;
import veroNstella.rmit.assignment.model.attendee.Attendee;
import veroNstella.rmit.assignment.model.attendee.AttendeeImpl;
import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.model.event.EventImpl;
import veroNstella.rmit.assignment.model.movie.Movie;

public class ModelImpl implements Model {

    private static int order = 1;

    private static Context applicationContext;

    private final String TAG = getClass().getName();

    @SuppressLint("SimpleDateFormat")
    private DateFormat eventListDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm aa");

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private List<Event> events = new ArrayList<>();

    private List<Event> eventsOnSelectedDate = new ArrayList<>();

    private List<Movie> movies = new ArrayList<>();

    private Movie temporarySelectedMovie = null;

    private List<Attendee> temporaryAttendees = new ArrayList<>();

    private String temporaryStartDate;

    private String temporaryEndDate;

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private Double latitude = null;

    private Double longitude = null;

    private ArrayList<String> dismissedEventsIds = new ArrayList<>();

    private ArrayList<String> remindAgainEventsIds = new ArrayList<>();

    private Map<String, Integer> eventIdAndNotification = new HashMap<>();

    private ModelImpl() {
    }

    public static Model getSingletonInstance(Context appContext) {
        if (applicationContext == null) {
            applicationContext = appContext;
            DataLoader dataLoader = new DataLoader(appContext);
            dataLoader.execute();
        }
        return LazyHolder.INSTANCE;
    }

    public void updateEvent(int eventId, String title,
                            String startDate, String endDate, String venue,
                            String location) throws ParseException {
        Event old_Event = new EventImpl(this.events.get(eventId).getEventId(),
                this.events.get(eventId).getTitle(), this.events.get(eventId).getStartDate(),
                this.events.get(eventId).getEndDate(), this.events.get(eventId).getVenue(),
                this.events.get(eventId).getLocation());
        Event event = this.events.get(eventId);
        event.setTitle(title);
        event.setStartDate(eventListDateFormat.parse(startDate));
        event.setEndDate(eventListDateFormat.parse(endDate));
        event.setVenue(venue);
        event.setLocation(location);

        if (temporarySelectedMovie != null) {
            event.setMovie(temporarySelectedMovie);
        }

        event.setAttendees(new ArrayList<>(temporaryAttendees));
        pcs.firePropertyChange("update event", old_Event, event);

    }

    @Override
    public void resetTemporaryData() {
        this.temporarySelectedMovie = null;
        this.temporaryAttendees.removeAll(temporaryAttendees);
        this.temporaryStartDate = null;
        this.temporaryEndDate = null;
    }

    @Override
    public List<Event> getEventList() {
        return events;
    }

    @Override
    public List<Event> getEvents(int count) {
        List<Event> temporary = this.events;
        Date currentDate = new Date();
        temporary.sort(new EventDateComparator(1));
        temporary.removeIf(e -> e.getEndDate().compareTo(currentDate) < 0);
        return temporary.subList(0,count);
    }

    @Override
    public Event getEventById(int eventId) {
        return events.get(eventId);
    }

    @Override
    public void removeEventById(int eventId) {
        pcs.firePropertyChange("remove event", events, this.events.remove(eventId));
    }

    @Override
    public void addEvent(String eventId, String title, String startDate, String endDate,
                         String venue, String location) {
        Date parsedStartDate;
        Date parsedEndDate;
        Event event = null;

        try {
            parsedStartDate = eventListDateFormat.parse(startDate);
            parsedEndDate = eventListDateFormat.parse(endDate);
            event = new EventImpl(eventId, title, parsedStartDate, parsedEndDate, venue, location);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (temporarySelectedMovie != null) {
            if (event != null) {
                event.setMovie(temporarySelectedMovie);
            }
        }

        if (event != null) {
            event.setAttendees(new ArrayList<>(temporaryAttendees));
        }
        this.events.add(event);
        pcs.firePropertyChange("update event", false, event);
    }

    @Override
    public void updateEventOnSelectedDate() {
        pcs.firePropertyChange("selected date event", false
                , eventsOnSelectedDate);
    }

    @Override
    public List<Event> getEventOnSelectedDate() {
        return this.eventsOnSelectedDate;
    }

    @Override
    public void clearEventOnSelectedDate() {
        pcs.firePropertyChange("selected date event",
                eventsOnSelectedDate, this.eventsOnSelectedDate.removeAll(eventsOnSelectedDate));
    }

    @Override
    public List<Movie> getMovieList() {
        return movies;
    }

    @Override
    public Movie getMovieById(int movieId) {
        return movies.get(movieId);
    }

    @Override
    public void addMovie(int index) {
        this.temporarySelectedMovie = movies.get(index);
    }

    @Override
    public List<Attendee> getAttendeeList() {
        return temporaryAttendees;
    }

    @Override
    public void addAttendee(String name) {
        pcs.firePropertyChange(
                "edit attendee", this.temporaryAttendees,
                this.temporaryAttendees.add(new AttendeeImpl(createUUID(), name)));
    }

    @Override
    public void removeAttendeeById(int attendeeId) {
        pcs.firePropertyChange(
                "edit attendee", this.temporaryAttendees,
                this.temporaryAttendees.remove(attendeeId));
    }

    @Override
    public void setAttendeesList(int eventId) {
        if (events.get(eventId).getAttendees() != null) {
            List<Attendee> newAttendees = events.get(eventId).getAttendees();
            for (int i = 0; i < newAttendees.size(); i++) {
                temporaryAttendees = new ArrayList<>(newAttendees);
            }
            pcs.firePropertyChange("edit attendee", null, temporaryAttendees);
        }
    }

    @Override
    public String getStartDate() {
        return this.temporaryStartDate;
    }

    @Override
    public void setStartDate(String startDate) {
        this.temporaryStartDate = startDate;
    }

    @Override
    public String getEndDate() {
        return this.temporaryEndDate;
    }

    @Override
    public void setEndDate(String editable) {
        this.temporaryEndDate = editable;
    }

    @Override
    public void setSelectedDateEvents(Calendar calendar) {
        @SuppressLint("DefaultLocale")
        String formattedDate = String.format("%02d/%02d/%04d", calendar.get(Calendar.DATE),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        for (Event event : events) {
            if (sdf.format(event.getStartDate()).equals(formattedDate)) {
                pcs.firePropertyChange("selected date event",
                        eventsOnSelectedDate, this.eventsOnSelectedDate.add(event));
            }
        }
    }

    @Override
    public void validateLocation(String location) {
        String[] locations;
        locations = location.split(",");
        Double.parseDouble(locations[0].trim());
        Double.parseDouble(locations[1].trim());
    }

    @Override
    public void validateDate() throws InvalidDateException, ParseException {
        Date checkedStartDate = eventListDateFormat.parse(temporaryStartDate);
        Date checkedEndDate = eventListDateFormat.parse(temporaryEndDate);
        String now = eventListDateFormat.format(Calendar.getInstance().getTime());
        Date dNow = eventListDateFormat.parse(now);

        if (checkedStartDate.compareTo(dNow) <= 0) {
            throw new InvalidDateException("Start date must be later then today");
        }

        if (checkedEndDate.compareTo(checkedStartDate) <= 0) {
            throw new InvalidDateException("End date not valid");
        }

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener newListener) {
        pcs.addPropertyChangeListener(newListener);
    }

    @Override
    public String createUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void orderEvent() {
        Collections.sort(events, new EventDateComparator(order));
        Collections.sort(eventsOnSelectedDate, new EventDateComparator(order));
        pcs.firePropertyChange("update event",
                false, events);
        pcs.firePropertyChange("selected date event",
                false, eventsOnSelectedDate);
    }

    @Override
    public void setOrder() {
        if (order == 1) {
            order = 0;
        } else {
            order = 1;
        }
    }

    @Override
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public void setEvents(List<Event> events) {
        this.events = events;
        pcs.firePropertyChange("update event",
                false, events);
    }

    @Override
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public void setLongitude(Double longtitude) {
        this.longitude = longtitude;
    }

    @Override
    public Double getLatitude() {
        return this.latitude;
    }

    @Override
    public Double getLongitude() {
        return this.longitude;
    }

    @Override
    public void removeEventByEventId(String eventId) {
        for(int i = 0; i < events.size(); i++) {
            if(events.get(i).getEventId().equals(eventId)){
                pcs.firePropertyChange("remove event", events,
                        this.events.remove(events.get(i)));
                break;
            }
        }
    }

    @Override
    public Event getEventByEventId(String eventId) {
        for(int i = 0; i < events.size(); i++) {
            if(events.get(i).getEventId().equals(eventId)){
                return events.get(i);
            }
        }
        return null;
    }

    @Override
    public void addDismissedEvent(String eventId) {
        this.dismissedEventsIds.add(eventId);
    }

    @Override
    public List<String> getAllDismissedEvent() {
        return this.dismissedEventsIds;
    }

    @Override
    public void addRemindAgainEvent(String eventId) {
        this.remindAgainEventsIds.add(eventId);
    }

    @Override
    public List<String> getAllRemindAgainEvent() {
        return this.remindAgainEventsIds;
    }

    @Override
    public void addEventNotificationPair(String eventId, int id) {
        eventIdAndNotification.put(eventId, id);
    }

    @Override
    public void removeNotificationIfExist(String eventId) {
        if(eventIdAndNotification.containsKey(eventId)){
            Utility.cancelNotification(applicationContext, eventIdAndNotification.get(eventId));
        }
    }

    private static class LazyHolder {
        static final Model INSTANCE = new ModelImpl();
    }


}
