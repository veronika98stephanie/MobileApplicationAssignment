package veroNstella.rmit.assignment.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import veroNstella.rmit.assignment.asyncTask.FileLoader;
import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.attendee.Attendee;
import veroNstella.rmit.assignment.model.attendee.AttendeeImpl;
import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.model.event.EventImpl;
import veroNstella.rmit.assignment.model.movie.Movie;
import veroNstella.rmit.assignment.model.movie.MovieImpl;

public class MovieEventDB {
    private static final String KEY_LOCATION = "location";
    private static final String KEY_FOREIGN_MOVIE_ID = "movie_id";
    private static final String KEY_MOVIE_ID = "id";
    private static final String KEY_MOVIE_TITLE = "title";
    private static final String KEY_MOVIE_YEAR = "year";
    private static final String KEY_MOVIE_POSTER = "poster";
    private static final String KEY_ATTENDEE_ID = "id";
    private static final String KEY_ATTENDEE_NAME = "name";
    private static final String KEY_EVENT_FOREIGN_ID = "event_id";
    private static final String KEY_EVENT_ID = "id";
    private static final String KEY_EVENT_TITLE = "title";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";
    private static final String KEY_VENUE = "venue";
    private static final String DATABASE_NAME = "movie_event";
    private static final String TABLE_MOIVE = "movies";
    private static final String TABLE_EVENT = "events";
    private static final String TABLE_ATTENDEE = "attendees";
    private final String TAG = getClass().getSimpleName();
    private final int DATABASE_VERSION = 1;
    private final Context context;

    @SuppressLint("SimpleDateFormat")
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");
    @SuppressLint("SimpleDateFormat")
    private DateFormat dataBaseDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");

    private Model model;
    private SQLiteDBHelper sqLiteDBHelper;
    private SQLiteDatabase database;

    public MovieEventDB(Context context) {
        this.context = context;
        model = ModelImpl.getSingletonInstance(context);
    }

    public MovieEventDB open() throws SQLException {
        sqLiteDBHelper = new SQLiteDBHelper(this.context);
        database = sqLiteDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        sqLiteDBHelper.close();
    }


    private boolean checkDataExist(String id, String tableName, String idColumnName) {
        String movieDataExistQuery = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = \"" + id + "\"";
        Cursor cursor = database.rawQuery(movieDataExistQuery, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void createMoiveEntry(String id, String title, int year, String poster) {
        boolean dataExist = checkDataExist(id, TABLE_MOIVE, KEY_MOVIE_ID);
        if (!dataExist) {
            ContentValues values = new ContentValues();
            values.put(KEY_MOVIE_ID, id);
            values.put(KEY_MOVIE_TITLE, title);
            values.put(KEY_MOVIE_YEAR, year);
            values.put(KEY_MOVIE_POSTER, poster);
            database.insert(TABLE_MOIVE, null, values);
        }
    }

    public void createEventEntry(String id, String title, String startDate, String endDate,
                                 String venue, String location, String movieId) {
        boolean dataExist = checkDataExist(id, TABLE_EVENT, KEY_EVENT_ID);
        if (!dataExist) {
            ContentValues values = new ContentValues();
            values.put(KEY_EVENT_ID, id);
            values.put(KEY_EVENT_TITLE, title);
            values.put(KEY_START_DATE, startDate);
            values.put(KEY_END_DATE, endDate);
            values.put(KEY_VENUE, venue);
            values.put(KEY_LOCATION, location);
            values.put(KEY_FOREIGN_MOVIE_ID, movieId);
            database.insert(TABLE_EVENT, null, values);
        }
    }

    private void createAttendeeEntry(String id, String name, String eventId) {
        ContentValues values = new ContentValues();
        values.put(KEY_ATTENDEE_ID, id);
        values.put(KEY_ATTENDEE_NAME, name);
        values.put(KEY_EVENT_FOREIGN_ID, eventId);
        database.insert(TABLE_ATTENDEE, null, values);
    }

    public List<Movie> getAllMovies() {
        String[] columns = new String[]{KEY_MOVIE_ID, KEY_MOVIE_TITLE, KEY_MOVIE_YEAR, KEY_MOVIE_POSTER};
        Cursor c = database.query(TABLE_MOIVE, columns, null, null, null,
                null, null);
        List<Movie> tempMovies = new ArrayList<>();

        int rowId = c.getColumnIndex(KEY_MOVIE_ID);
        int rowTitle = c.getColumnIndex(KEY_MOVIE_TITLE);
        int rowYear = c.getColumnIndex(KEY_MOVIE_YEAR);
        int rowPoster = c.getColumnIndex(KEY_MOVIE_POSTER);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            tempMovies.add(new MovieImpl(c.getString(rowId), c.getString(rowTitle),
                    c.getInt(rowYear), c.getString(rowPoster)));
        }

        c.close();
        return tempMovies;
    }

    public List<Event> getAllEvents(List<Movie> movies) throws ParseException {
        String[] columns = new String[]{KEY_EVENT_ID, KEY_EVENT_TITLE, KEY_START_DATE,
                KEY_END_DATE, KEY_VENUE, KEY_LOCATION, KEY_FOREIGN_MOVIE_ID};
        @SuppressLint("Recycle")
        Cursor c = database.query(TABLE_EVENT, columns, null, null, null,
                null, null);
        List<Event> tempEvents = new ArrayList<>();

        int rowId = c.getColumnIndex(KEY_EVENT_ID);
        int rowTitle = c.getColumnIndex(KEY_EVENT_TITLE);
        int rowStartDate = c.getColumnIndex(KEY_START_DATE);
        int rowEndDate = c.getColumnIndex(KEY_END_DATE);
        int rowVenue = c.getColumnIndex(KEY_VENUE);
        int rowLocation = c.getColumnIndex(KEY_LOCATION);
        int rowMovie = c.getColumnIndex(KEY_FOREIGN_MOVIE_ID);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Date startDate;
            Date endDate;

            try {
                startDate = dateFormat.parse(c.getString(rowStartDate));
                endDate = dateFormat.parse(c.getString(rowEndDate));
            } catch (ParseException e) {
                startDate = dataBaseDateFormat.parse(c.getString(rowStartDate));
                endDate = dataBaseDateFormat.parse(c.getString(rowEndDate));
            }

            tempEvents.add(new EventImpl(c.getString(rowId), c.getString(rowTitle),
                    startDate, endDate, c.getString(rowVenue), c.getString(rowLocation)));

            for (Movie movie : movies) {
                if (movie.getMovieId().equals(c.getString(rowMovie))) {
                    tempEvents.get(tempEvents.size() - 1).setMovie(movie);
                }
            }

        }
        return tempEvents;
    }

    private List<Attendee> getAllAttendees(String eventId) {
        String attendeesQuery = "SELECT * FROM " + TABLE_ATTENDEE + " WHERE " + KEY_EVENT_FOREIGN_ID + " = \"" + eventId + "\";";

        @SuppressLint("Recycle")
        Cursor c = database.rawQuery(attendeesQuery, null);

        List<Attendee> tempAttendees = new ArrayList<>();

        int rowId = c.getColumnIndex(KEY_ATTENDEE_ID);
        int rowName = c.getColumnIndex(KEY_ATTENDEE_NAME);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            tempAttendees.add(new AttendeeImpl(c.getString(rowId), c.getString(rowName)));
        }
        return tempAttendees;
    }

    public void setEventAttendees(List<Event> events) {
        for (int i = 0; i < events.size(); i++) {
            List<Attendee> attendees = getAllAttendees(events.get(i).getEventId());
            events.get(i).setAttendees(attendees);
        }
    }

    public void storeAllData() {
        List<Movie> movies = model.getMovieList();
        List<Event> events = model.getEventList();

        for (Movie movie : movies) {
            this.createMoiveEntry(movie.getMovieId(), movie.getTitle(), movie.getYear(), movie.getPoster());
        }

        for (Event event : events) {
            try {
                this.createEventEntry(event.getEventId(), event.getTitle(), dateFormat.format(event.getStartDate()),
                        dateFormat.format(event.getEndDate()), event.getVenue(), event.getLocation(), event.getMovie().getMovieId());
            } catch (NullPointerException e) {
                this.createEventEntry(event.getEventId(), event.getTitle(), dateFormat.format(event.getStartDate()),
                        dateFormat.format(event.getEndDate()), event.getVenue(), event.getLocation(), null);
            }
            List<Attendee> attendees = event.getAttendees();
            if (attendees != null) {
                for (Attendee attendee : attendees) {
                    this.createAttendeeEntry(attendee.getAttendeesId(), attendee.getName(), event.getEventId());
                }
            }
        }
    }

    public void resetTable() {
        String deleteAttendeeDataQuery = "DELETE FROM " + TABLE_ATTENDEE + ";";
        String deleteEventDataQuery = "DELETE FROM " + TABLE_EVENT + ";";

        database.execSQL(deleteAttendeeDataQuery);
        database.execSQL(deleteEventDataQuery);
    }

    private class SQLiteDBHelper extends SQLiteOpenHelper {

        SQLiteDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createMovieTableQuery = "CREATE TABLE " + TABLE_MOIVE + " (" + KEY_MOVIE_ID +
                    " TEXT PRIMARY KEY, " + KEY_MOVIE_TITLE + " TEXT NOT NULL, " + KEY_MOVIE_YEAR +
                    " INT NOT NULL, " + KEY_MOVIE_POSTER + " TEXT NOT NULL);";
            db.execSQL(createMovieTableQuery);
            String createEventTableQuery = "CREATE TABLE " + TABLE_EVENT + " (" + KEY_EVENT_ID + " TEXT PRIMARY KEY, "
                    + KEY_EVENT_TITLE + " TEXT NOT NULL, " + KEY_START_DATE + " TEXT NOT NULL, " + KEY_END_DATE +
                    " TEXT NOT NULL, " + KEY_VENUE + " TEXT NOT NULL, " + KEY_LOCATION + " TEXT NOT NULL, " +
                    KEY_FOREIGN_MOVIE_ID + " TEXT, FOREIGN KEY (" + KEY_FOREIGN_MOVIE_ID + ") REFERENCES " +
                    TABLE_MOIVE + "(" + KEY_MOVIE_ID + "));";
            db.execSQL(createEventTableQuery);
            String createAttendeeTableQuery = "CREATE TABLE " + TABLE_ATTENDEE + " (" + KEY_ATTENDEE_ID +
                    " TEXT PRIMARY KEY, " + KEY_ATTENDEE_NAME + " TEXT NOT NULL, " + KEY_EVENT_FOREIGN_ID +
                    " TEXT NOT NULL, FOREIGN KEY(" + KEY_EVENT_FOREIGN_ID + ") REFERENCES " + TABLE_EVENT +
                    "(" + KEY_EVENT_ID + "));";
            db.execSQL(createAttendeeTableQuery);
            FileLoader fileLoader = new FileLoader(context);
            fileLoader.execute();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String dropAttendeeTableQuery = "DROP TABLE IF EXISTS " + TABLE_ATTENDEE + ";";
            database.execSQL(dropAttendeeTableQuery);
            String dropEventTableQuery = "DROP TABLE IF EXISTS " + TABLE_EVENT + ";";
            database.execSQL(dropEventTableQuery);
            String dropMovieTableQuery = "DROP TABLE IF EXISTS " + TABLE_MOIVE + ";";
            database.execSQL(dropMovieTableQuery);
            onCreate(db);
        }
    }
}
