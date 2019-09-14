package veroNstella.rmit.assignment.view;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.controller.CalendarUpdateListener;
import veroNstella.rmit.assignment.controller.EventAddListener;
import veroNstella.rmit.assignment.controller.EventSortListener;
import veroNstella.rmit.assignment.controller.EventTabLayoutListener;
import veroNstella.rmit.assignment.controller.SelectCalendarDateListener;
import veroNstella.rmit.assignment.controller.ViewMapsListener;
import veroNstella.rmit.assignment.database.MovieEventDB;
import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.thread.PersistDatabase;
import veroNstella.rmit.assignment.view.adapter.CalendarAdapter;
import veroNstella.rmit.assignment.view.adapter.SelectedDateEventAdapter;
import veroNstella.rmit.assignment.view.viewmodel.SelectedDateEventViewModel;

public class CalendarActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private TextView currentMonth;
    private GridView dates;
    private Model model;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Calendar curMonth = Calendar.getInstance();
    private MovieEventDB movieEventDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        model = ModelImpl.getSingletonInstance(getApplicationContext());
        movieEventDB = new MovieEventDB(getApplicationContext());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.calendarTabLayout);
        Objects.requireNonNull(tabLayout.getTabAt(1)).select();
        tabLayout.addOnTabSelectedListener(new EventTabLayoutListener(this));

        currentMonth = (TextView) findViewById(R.id.currentMonth);
        ImageButton prevMonth = (ImageButton) findViewById(R.id.prevMonth);
        ImageButton nextMonth = (ImageButton) findViewById(R.id.nextMonth);
        dates = (GridView) findViewById(R.id.calendarView);

        updateCalendar();

        // update the calendar view to the previous month
        prevMonth.setOnClickListener(new CalendarUpdateListener(CalendarActivity.this,
                curMonth, -1));

        // update the calendar view to the next month
        nextMonth.setOnClickListener(new CalendarUpdateListener(CalendarActivity.this,
                curMonth, 1));

        // if a date cells is clicked, show the current events
        dates.setOnItemClickListener(new SelectCalendarDateListener(CalendarActivity.this));

        // get selected date events view model to be observed
        SelectedDateEventViewModel selectedDateEventViewModel = ViewModelProviders.of(this)
                .get(SelectedDateEventViewModel.class);

        selectedDateEventViewModel.getEvents().observe(this, events -> {
            myAdapter = new SelectedDateEventAdapter(CalendarActivity.this, events);
            recyclerView = findViewById(R.id.selected_date_events_recycler_view);
            layoutManager = new LinearLayoutManager(CalendarActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCalendar();
        model.clearEventOnSelectedDate();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
        new Thread(new PersistDatabase(movieEventDB)).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addition_menu, menu);

        MenuItem addEventMenu = menu.findItem(R.id.addEventMenu);
        MenuItem sortEvent = menu.findItem(R.id.sortEvent);
        MenuItem viewOnMaps = menu.findItem(R.id.viewEventsOnMap);

        addEventMenu.setOnMenuItemClickListener(new EventAddListener(CalendarActivity.this));
        sortEvent.setOnMenuItemClickListener(new EventSortListener(CalendarActivity.this));
        viewOnMaps.setOnMenuItemClickListener(new ViewMapsListener(CalendarActivity.this));

        return true;
    }

    @SuppressLint("SimpleDateFormat")
    public void updateCalendar() {
        ArrayList<Calendar> cells = new ArrayList<>();
        Calendar calendar = (Calendar) curMonth.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginning = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginning);

        int NUMBER_OF_CELLS = 42;
        while (cells.size() < NUMBER_OF_CELLS) {
            cells.add((Calendar) calendar.clone());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        dates.setAdapter(new CalendarAdapter(CalendarActivity.this, cells, curMonth));
        currentMonth.setText(new SimpleDateFormat("MMM yyyy").format(curMonth.getTime()));
        model.updateEventOnSelectedDate();
    }
}
