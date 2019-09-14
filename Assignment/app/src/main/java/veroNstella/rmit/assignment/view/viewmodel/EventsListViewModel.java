package veroNstella.rmit.assignment.view.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.event.Event;

public class EventsListViewModel extends AndroidViewModel implements PropertyChangeListener {
    private Model model;
    private MutableLiveData<List<Event>> eventsLiveData;

    public EventsListViewModel(@NonNull Application application) {
        super(application);
        model = ModelImpl.getSingletonInstance(getApplication());
        model.addPropertyChangeListener(EventsListViewModel.this);
    }

    public LiveData<List<Event>> getEvents() {
        if (eventsLiveData == null) {
            eventsLiveData = new MutableLiveData<>();
            eventsLiveData.setValue(model.getEventList());
        }
        return eventsLiveData;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("remove event")) {
            eventsLiveData.setValue(model.getEventList());
        }
        if (evt.getPropertyName().equals("update event")) {
            eventsLiveData.setValue(model.getEventList());
        }
    }
}
