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
import veroNstella.rmit.assignment.model.attendee.Attendee;

public class AttendeesListViewModel extends AndroidViewModel implements PropertyChangeListener {
    private MutableLiveData<List<Attendee>> attendeesLiveData;
    private Model model;

    public AttendeesListViewModel(@NonNull Application application) {
        super(application);
        model = ModelImpl.getSingletonInstance(getApplication());
        model.addPropertyChangeListener(AttendeesListViewModel.this);
    }

    public LiveData<List<Attendee>> getAttendees() {
        if (attendeesLiveData == null) {
            attendeesLiveData = new MutableLiveData<>();
            attendeesLiveData.setValue(model.getAttendeeList());
        }
        return attendeesLiveData;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("edit attendee")) {
            attendeesLiveData.setValue(model.getAttendeeList());
        }
    }
}
