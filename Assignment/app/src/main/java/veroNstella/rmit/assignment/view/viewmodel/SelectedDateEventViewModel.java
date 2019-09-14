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

public class SelectedDateEventViewModel extends AndroidViewModel implements PropertyChangeListener {
    private Model model;
    private MutableLiveData<List<Event>> selectedDateEventLiveData;

    public SelectedDateEventViewModel(@NonNull Application application) {
        super(application);
        model = ModelImpl.getSingletonInstance(getApplication());
        model.addPropertyChangeListener(SelectedDateEventViewModel.this);
    }

    public LiveData<List<Event>> getEvents() {
        if (selectedDateEventLiveData == null) {
            selectedDateEventLiveData = new MutableLiveData<>();
            selectedDateEventLiveData.setValue(model.getEventOnSelectedDate());
        }
        return selectedDateEventLiveData;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("selected date event")) {
            selectedDateEventLiveData.setValue(model.getEventOnSelectedDate());
        }
    }
}
