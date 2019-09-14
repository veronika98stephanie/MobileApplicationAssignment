package veroNstella.rmit.assignment.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.controller.EventAddListener;
import veroNstella.rmit.assignment.controller.EventSortListener;
import veroNstella.rmit.assignment.controller.EventTabLayoutListener;
import veroNstella.rmit.assignment.controller.SettingsListener;
import veroNstella.rmit.assignment.controller.ViewMapsListener;
import veroNstella.rmit.assignment.database.MovieEventDB;
import veroNstella.rmit.assignment.service.ApplicationServices;
import veroNstella.rmit.assignment.service.LocationServiceListener;
import veroNstella.rmit.assignment.thread.PersistDatabase;
import veroNstella.rmit.assignment.view.adapter.EventAdapter;
import veroNstella.rmit.assignment.view.viewmodel.EventsListViewModel;

public class EventActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MovieEventDB movieEventDB;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Log.i(TAG, "onCreate: ");
        movieEventDB = new MovieEventDB(getApplicationContext());
        TabLayout tabLayout = findViewById(R.id.eventTabLayout);
        tabLayout.addOnTabSelectedListener(new EventTabLayoutListener(this));

        ApplicationServices applicationServices = new ApplicationServices(EventActivity.this);
        applicationServices.createService();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationServiceListener(this.getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);
        } else {
            configure();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configure();
                }
        }
    }

    @SuppressLint("MissingPermission")
    private void configure() {
        locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");

        EventsListViewModel eventViewModel = ViewModelProviders.of(this).get(EventsListViewModel.class);

        eventViewModel.getEvents().observe(this, events -> {
            myAdapter = new EventAdapter(EventActivity.this, events);
            recyclerView = findViewById(R.id.eventRecyclerView);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(EventActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
        new Thread(new PersistDatabase(movieEventDB)).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addition_menu, menu);

        MenuItem addEventMenu = menu.findItem(R.id.addEventMenu);
        MenuItem sortEvent = menu.findItem(R.id.sortEvent);
        MenuItem viewOnMaps = menu.findItem(R.id.viewEventsOnMap);
        MenuItem settings = menu.findItem(R.id.settings);

        addEventMenu.setOnMenuItemClickListener(new EventAddListener(EventActivity.this));
        sortEvent.setOnMenuItemClickListener(new EventSortListener(EventActivity.this));
        viewOnMaps.setOnMenuItemClickListener(new ViewMapsListener(EventActivity.this));
        settings.setOnMenuItemClickListener(new SettingsListener(EventActivity.this));

        return true;
    }
}
