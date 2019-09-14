package veroNstella.rmit.assignment.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
import veroNstella.rmit.assignment.controller.MapMoveListener;
import veroNstella.rmit.assignment.model.Utility;
import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.model.movie.Movie;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {
    private List<Event> eventList;
    @SuppressLint("SimpleDateFormat")
    private DateFormat dateFormat = new SimpleDateFormat("E, dd MMMM yyyy\nhh:mm aa");
    private Activity context;
    private Utility utility;

    public EventListAdapter(Activity context, List<Event> eventList) {
        this.eventList = eventList;
        this.context = context;
        this.utility = new Utility(context);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_event_list, viewGroup, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {
        eventViewHolder.txtEventTitle.setText(this.eventList.get(i).getTitle());
        eventViewHolder.txtEventStartDate.setText(dateFormat.format(this.eventList.get(i).getStartDate()));
        eventViewHolder.txtEventVenue.setText(this.eventList.get(i).getVenue());
        if (this.eventList.get(i).getMovie() != null) {
            eventViewHolder.txtEventMovieTitle.setText(this.eventList.get(i).getMovie().getTitle());
        } else {
            eventViewHolder.txtEventMovieTitle.setText(R.string.n_a);
        }
        if (this.eventList.get(i).getAttendees() != null) {
            eventViewHolder.txtEventAttendeesNumber.setText(String.format("Number of attendees: %s",
                    Integer.toString(this.eventList.get(i).getAttendees().size())));
        } else {
            eventViewHolder.txtEventAttendeesNumber.setText(R.string.number_of_attendee_0);
        }
        Movie movie = this.eventList.get(i).getMovie();
        utility.setMovieDetail(movie, eventViewHolder.txtEventMovieTitle, null,
                eventViewHolder.imgMoive);
        eventViewHolder.eventCard.setOnClickListener(new MapMoveListener(context, this.eventList.get(i)));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        private TextView txtEventTitle, txtEventStartDate, txtEventVenue,
                txtEventMovieTitle, txtEventAttendeesNumber;
        private ImageView imgMoive;
        private CardView eventCard;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEventTitle = itemView.findViewById(R.id.txtEventTitle);
            txtEventStartDate = itemView.findViewById(R.id.txtEventStartDate);
            txtEventVenue = itemView.findViewById(R.id.txtEventVenue);
            txtEventMovieTitle = itemView.findViewById(R.id.txtEventMovieTitle);
            txtEventAttendeesNumber = itemView.findViewById(R.id.txtEventAttendeesNumber);
            imgMoive = itemView.findViewById(R.id.imgEventMovie);
            eventCard = itemView.findViewById(R.id.eventCard);
        }
    }
}
