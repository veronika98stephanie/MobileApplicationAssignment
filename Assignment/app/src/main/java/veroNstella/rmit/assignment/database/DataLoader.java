package veroNstella.rmit.assignment.database;

import android.content.Context;
import android.os.AsyncTask;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.model.movie.Movie;

public class DataLoader extends AsyncTask<Void, Void, Void> {
    private MovieEventDB movieEventDB;
    private Model model;
    private List<Event> events = new ArrayList<>();
    private List<Movie> movies = new ArrayList<>();

    public DataLoader(Context context) {
        model = ModelImpl.getSingletonInstance(context);
        movieEventDB = new MovieEventDB(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        movieEventDB.open();
        this.movies = movieEventDB.getAllMovies();
        try {
            this.events = movieEventDB.getAllEvents(this.movies);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        movieEventDB.setEventAttendees(this.events);
        movieEventDB.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        model.setEvents(events);
        model.orderEvent();
        model.setMovies(movies);
    }
}
