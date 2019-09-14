package veroNstella.rmit.assignment.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.controller.EventDeleteListener;
import veroNstella.rmit.assignment.controller.EventEditListener;
import veroNstella.rmit.assignment.model.Utility;
import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.model.event.EventImpl;
import veroNstella.rmit.assignment.model.movie.Movie;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    @SuppressLint("SimpleDateFormat")
    private DateFormat dateFormat = new SimpleDateFormat("E, dd MMMM yyyy\nhh:mm aa");
    private List<Event> events;
    private Activity context;
    private Utility utility;

    public EventAdapter(Activity context, List<Event> eventList) {
        events = eventList;
        this.context = context;
        utility = new Utility(context);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_event_list, viewGroup, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {
        eventViewHolder.txtEventTitle.setText(events.get(i).getTitle());
        eventViewHolder.txtEventStartDate.setText(dateFormat.format(events.get(i).getStartDate()));
        eventViewHolder.txtEventVenue.setText(events.get(i).getVenue());
        Movie movie = events.get(i).getMovie();
        utility.setMovieDetail(movie, eventViewHolder.txtEventMovieTitle, null,
                eventViewHolder.imgMoive);

        if (events.get(i).getAttendees() != null) {
            eventViewHolder.txtEventAttendeesNumber.setText(String.format("Number of attendees: %s",
                    Integer.toString(events.get(i).getAttendees().size())));
        } else {
            eventViewHolder.txtEventAttendeesNumber.setText(R.string.number_of_attendee_0);
        }
        eventViewHolder.imgEdit.setTag(events.get(i));
        eventViewHolder.imgDelete.setTag(events.get(i));
        eventViewHolder.imgEdit.setOnClickListener(
                new EventEditListener(context, events.indexOf(
                        (EventImpl) eventViewHolder.imgEdit.getTag())));
        eventViewHolder.imgDelete.setOnClickListener(
                new EventDeleteListener(context, events.indexOf(
                        (EventImpl) eventViewHolder.imgDelete.getTag())));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView txtEventTitle, txtEventStartDate, txtEventVenue,
                txtEventMovieTitle, txtEventAttendeesNumber;
        private ImageView imgMoive, imgEdit, imgDelete;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEventTitle = itemView.findViewById(R.id.txtEventTitle);
            txtEventStartDate = itemView.findViewById(R.id.txtEventStartDate);
            txtEventVenue = itemView.findViewById(R.id.txtEventVenue);
            txtEventMovieTitle = itemView.findViewById(R.id.txtEventMovieTitle);
            txtEventAttendeesNumber = itemView.findViewById(R.id.txtEventAttendeesNumber);
            imgEdit = itemView.findViewById(R.id.imgEventEdit);
            imgDelete = itemView.findViewById(R.id.imgEventDelete);
            imgMoive = itemView.findViewById(R.id.imgEventMovie);
        }
    }
}
