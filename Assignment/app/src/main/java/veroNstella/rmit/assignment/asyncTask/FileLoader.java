package veroNstella.rmit.assignment.asyncTask;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.database.MovieEventDB;
import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.model.movie.Movie;

public class FileLoader extends AsyncTask<Void, Void, Void> {
    private WeakReference<Context> context;
    private MovieEventDB movieEventDB;
    private Model model;
    private List<Event> events = new ArrayList<>();
    private List<Movie> movies = new ArrayList<>();

    public FileLoader(Context context) {
        this.context = new WeakReference<>(context);
        model = ModelImpl.getSingletonInstance(context);
        movieEventDB = new MovieEventDB(context);
    }

    private void loadEvents() {
        try {
            String line;
            InputStream is = context.get().getResources().openRawResource(R.raw.events);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((line = br.readLine()) != null) {
                if (line.charAt(0) != '/') {
                    String[] eventDetail = line.split("\",\"");
                    for (int i = 0; i < eventDetail.length; i++) {
                        eventDetail[i] = eventDetail[i].replace("\"", "");
                    }
                    movieEventDB.open();
                    movieEventDB.createEventEntry(eventDetail[0], eventDetail[1], eventDetail[2],
                            eventDetail[3], eventDetail[4], eventDetail[5], null);
                    movieEventDB.close();
                }
            }
            movieEventDB.open();
            this.events = movieEventDB.getAllEvents(this.movies);
            movieEventDB.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void loadMovies() {
        try {
            String line;
            InputStream is = context.get().getResources().openRawResource(R.raw.movies);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((line = br.readLine()) != null) {
                if (line.charAt(0) != '/') {
                    String[] movieDetail = line.split("\",\"");
                    for (int i = 0; i < movieDetail.length; i++) {
                        movieDetail[i] = movieDetail[i].replace("\"", "");
                        movieDetail[i] = movieDetail[i].replace(".jpg", "");
                    }
                    movieEventDB.open();
                    movieEventDB.createMoiveEntry(movieDetail[0], movieDetail[1],
                            Integer.parseInt(movieDetail[2]), movieDetail[3]);
                    movieEventDB.close();
                }
            }
            movieEventDB.open();
            this.movies = movieEventDB.getAllMovies();
            movieEventDB.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadEventAttendees() {
        try {
            movieEventDB.open();
            movieEventDB.setEventAttendees(this.events);
            movieEventDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... unused) {
        loadMovies();
        loadEvents();
        loadEventAttendees();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        model.setEvents(events);
        model.orderEvent();
        model.setMovies(movies);
    }
}
