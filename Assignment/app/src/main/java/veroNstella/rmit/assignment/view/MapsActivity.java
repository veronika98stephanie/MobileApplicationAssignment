package veroNstella.rmit.assignment.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.view.adapter.EventListAdapter;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Event> eventList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Model model = ModelImpl.getSingletonInstance(this);
        if(model.getEventList().size() >= 3) {
            eventList = model.getEvents(3);
        }else {
            eventList = model.getEvents(model.getEventList().size());
        }
        RecyclerView eventListDisplay = findViewById(R.id.eventList);
        EventListAdapter eventListAdapter = new EventListAdapter(this, eventList);
        eventListDisplay.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MapsActivity.this);
        eventListDisplay.setLayoutManager(layoutManager);
        eventListDisplay.setAdapter(eventListAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng lastKnownCoordinates = new LatLng(-37.815018, 144.946014);

        for (Event event : this.eventList) {
            MarkerOptions markerOptions = generateStandardMarker(event);
            mMap.addMarker(markerOptions);
            lastKnownCoordinates = markerOptions.getPosition();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownCoordinates, 15));

    }

    private LatLng convertLocationString(String location) {
        String[] rawCoordinates = location.split(",");
        int LATITUDE_KEY = 0;
        int LONGITUDE_KEY = 1;
        return new LatLng(Double.parseDouble(rawCoordinates[LATITUDE_KEY].trim()),
                Double.parseDouble(rawCoordinates[LONGITUDE_KEY].trim()));
    }

    private MarkerOptions generateStandardMarker(Event event) {
        LatLng coordinates = convertLocationString(event.getLocation());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coordinates);
        markerOptions.title(event.getTitle());
        return markerOptions;
    }

    public void ChangeFocus(Event event) {
        LatLng coordinates = this.convertLocationString(event.getLocation());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
    }
}
