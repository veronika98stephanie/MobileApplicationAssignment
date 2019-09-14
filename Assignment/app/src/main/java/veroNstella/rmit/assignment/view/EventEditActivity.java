package veroNstella.rmit.assignment.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.controller.AttendeeAddListener;
import veroNstella.rmit.assignment.controller.AttendeeController;
import veroNstella.rmit.assignment.controller.EventCancelEditListener;
import veroNstella.rmit.assignment.controller.EventEditMovieListener;
import veroNstella.rmit.assignment.controller.EventSubmitAddListener;
import veroNstella.rmit.assignment.controller.EventSubmitEditListener;
import veroNstella.rmit.assignment.controller.SelectDateListener;
import veroNstella.rmit.assignment.controller.SelectTimeListener;
import veroNstella.rmit.assignment.database.MovieEventDB;
import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.Utility;
import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.model.movie.Movie;
import veroNstella.rmit.assignment.thread.PersistDatabase;
import veroNstella.rmit.assignment.view.adapter.AttendeeAdapter;
import veroNstella.rmit.assignment.view.viewmodel.AttendeesListViewModel;

public class EventEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final String TAG = getClass().getSimpleName();

    // activity request code
    private final int EDIT_MOVIE = 1;
    private final int PICK_CONTACT = 2;

    private MovieEventDB movieEventDB;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Model model;
    private TextView startDate;
    private TextView endDate;
    private ImageView movieImage;
    private TextView movieTitle;
    private TextView year;
    private AttendeeController attendeeController = new AttendeeController(this);
    private Utility utility = new Utility(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        movieEventDB = new MovieEventDB(getApplicationContext());

        model = ModelImpl.getSingletonInstance(getApplicationContext());

        AttendeesListViewModel attendeesViewModel = ViewModelProviders.of(this)
                .get(AttendeesListViewModel.class);

        attendeesViewModel.getAttendees().observe(this, attendees -> {
            myAdapter = new AttendeeAdapter(EventEditActivity.this, attendees);
            recyclerView = findViewById(R.id.recyclerViewEditAttendees);
            layoutManager = new GridLayoutManager(
                    EventEditActivity.this, 3,
                    GridLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);
        });

        EditText title = findViewById(R.id.etTitle);
        startDate = findViewById(R.id.etStartDate);
        endDate = findViewById(R.id.etEndDate);
        EditText venue = findViewById(R.id.etVenue);
        EditText location = findViewById(R.id.etLocation);
        movieImage = findViewById(R.id.imgEditMovie);
        movieTitle = findViewById(R.id.txtEditMovieTitle);
        year = findViewById(R.id.txtEditMovieYear);
        Button btnEditMovie = findViewById(R.id.btnEventEditMovie);
        Button btnAddAttendees = findViewById(R.id.btnAddEventAttendee);
        Button btnSubmit = findViewById(R.id.btnEditSubmit);
        Button btnCancel = findViewById(R.id.btnEditCancel);
        ImageView selectStartDate = findViewById(R.id.selectStartDate);
        ImageView selectEndDate = findViewById(R.id.selectEndDate);
        ImageView selectStartTime = findViewById(R.id.selectStartTime);
        ImageView selectEndTime = findViewById(R.id.selectEndTime);

        Intent intent = getIntent();
        String activityName = (String) Objects.requireNonNull(intent.getExtras()).get("activity");

        // function to show filled out form to be edited if activity name is edit
        if (activityName != null && activityName.equals("edit")) {
            int eventId = (Integer) intent.getExtras().get(Intent.EXTRA_TEXT);
            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");

            try {
                Event event = model.getEventById(eventId);
                if (event != null) {
                    title.setText(event.getTitle());
                    startDate.setText(dateFormat.format(event.getStartDate()));
                    model.setStartDate(startDate.getEditableText().toString());
                    endDate.setText(dateFormat.format(event.getEndDate()));
                    model.setEndDate(endDate.getEditableText().toString());
                    venue.setText(event.getVenue());
                    location.setText(event.getLocation());
                }
                Movie movie = null;
                if (event != null) {
                    movie = event.getMovie();
                }
                utility.setMovieDetail(movie, movieTitle, year, movieImage);
            } catch (Exception e) {
                Toast.makeText(this, "Cannot parse the id into integer",
                        Toast.LENGTH_SHORT).show();
            }
            btnSubmit.setOnClickListener(new EventSubmitEditListener(
                    EventEditActivity.this, eventId, title.getText(),
                    venue.getText(), location.getText()));
        }

        // function to show blank form to be filled if activity name is add
        if (activityName != null && activityName.equals("add")) {
            btnSubmit.setOnClickListener(new EventSubmitAddListener(
                    EventEditActivity.this, model.createUUID(), title.getText(),
                    venue.getText(), location.getText()));
        }

        btnEditMovie.setOnClickListener(new EventEditMovieListener(
                EventEditActivity.this, EDIT_MOVIE));

        btnAddAttendees.setOnClickListener(new AttendeeAddListener(
                EventEditActivity.this, PICK_CONTACT));

        btnCancel.setOnClickListener(new EventCancelEditListener(
                EventEditActivity.this));

        String reqStartDate = "select start date";
        selectStartDate.setOnClickListener(new SelectDateListener(
                EventEditActivity.this, reqStartDate));

        String reqEndDate = "select end date";
        selectEndDate.setOnClickListener(new SelectDateListener(
                EventEditActivity.this, reqEndDate));

        String reqStartTime = "select start time";
        selectStartTime.setOnClickListener(new SelectTimeListener(
                EventEditActivity.this, reqStartTime));

        String reqEndTime = "select end time";
        selectEndTime.setOnClickListener(new SelectTimeListener(
                EventEditActivity.this, reqEndTime));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
        new Thread(new PersistDatabase(movieEventDB)).start();
    }

    @SuppressLint("Recycle")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_MOVIE) {
            if (resultCode == RESULT_OK) {
                int index = 0;
                if (data != null) {
                    index = Integer.parseInt(data.getStringExtra("movie"));
                }

                model.addMovie(index);

                utility.setMovieDetail(model.getMovieById(index), movieTitle, year, movieImage);

            } else {
                Toast.makeText(this, "No movie selected", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PICK_CONTACT) {

            if (resultCode == Activity.RESULT_OK) {
                Uri contactUri = null;
                String name = "";
                int idx;
                Cursor cursor = null;
                if (data != null) {
                    contactUri = data.getData();
                }
                if (contactUri != null) {
                    cursor = getContentResolver().query(
                            contactUri, null, null, null, null);
                }
                if (cursor != null && cursor.moveToFirst()) {
                    idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    name = cursor.getString(idx);
                }

                attendeeController.addNewAttendee(name);
                Toast.makeText(this, "attendee selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No attendee selected", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        month += 1;

        if (fragmentManager.findFragmentByTag("select start date") != null) {
            String newDate;
            if (startDate.getText() != null && startDate.getText().length() > 0) {
                String[] dateAndTime = startDate.getText().toString().split(" ", 2);
                if (dateAndTime.length == 2) {
                    newDate = String.format("%02d/%02d/%d %s", dayOfMonth, month, year, dateAndTime[1]);
                } else {
                    newDate = String.format("%02d/%02d/%d %s", dayOfMonth, month, year, dateAndTime[0]);
                }
            } else {
                newDate = String.format("%02d/%02d/%02d ", dayOfMonth, month, year);
            }
            startDate.setText(newDate);
            model.setStartDate(startDate.getEditableText().toString());
        }

        if (fragmentManager.findFragmentByTag("select end date") != null) {
            String newDate;
            if (endDate.getText() != null && endDate.getText().length() > 0) {
                String[] dateAndTime = endDate.getText().toString().split(" ", 2);
                if (dateAndTime.length == 2) {
                    newDate = String.format("%02d/%02d/%d %s", dayOfMonth, month, year, dateAndTime[1]);
                } else {
                    newDate = String.format("%02d/%02d/%d %s", dayOfMonth, month, year, dateAndTime[0]);
                }
            } else {
                newDate = String.format("%02d/%02d/%02d ", dayOfMonth, month, year);
            }
            endDate.setText(newDate);
            model.setEndDate(endDate.getEditableText().toString());
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        String am_pm = (hourOfDay < 12) ? "AM" : "PM";

        if (fragmentManager.findFragmentByTag("select start time") != null) {
            String newTime;
            if (startDate.getText() != null && startDate.getText().length() > 0) {
                String[] dateAndTime = startDate.getText().toString().split(" ", 2);
                newTime = String.format("%s %02d:%02d %s", dateAndTime[0], hourOfDay, minute, am_pm);
            } else {
                newTime = String.format(" %02d:%02d %s", hourOfDay, minute, am_pm);
            }

            startDate.setText(newTime);
            model.setStartDate(startDate.getEditableText().toString());
        }

        if (fragmentManager.findFragmentByTag("select end time") != null) {
            String newTime;
            if (endDate.getText() != null && endDate.getText().length() > 0) {
                String[] dateAndTime = endDate.getText().toString().split(" ", 2);
                newTime = String.format("%s %02d:%02d %s", dateAndTime[0], hourOfDay, minute, am_pm);
            } else {
                newTime = String.format(" %02d:%02d %s", hourOfDay, minute, am_pm);
            }
            endDate.setText(newTime);
            model.setEndDate(endDate.getEditableText().toString());
        }
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
