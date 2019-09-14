package veroNstella.rmit.assignment.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.controller.EventEditListener;
import veroNstella.rmit.assignment.model.event.Event;

public class SelectedDateEventAdapter extends RecyclerView.Adapter<SelectedDateEventAdapter.SelectedDateEventViewHolder> {
    @SuppressLint("SimpleDateFormat")
    private DateFormat dateStartFormat = new SimpleDateFormat("E, dd MMMM yyyy\nhh:mm aa");
    @SuppressLint("SimpleDateFormat")
    private DateFormat dateEndFormat = new SimpleDateFormat("hh:mm aa");
    private List<Event> events;
    private Activity context;

    public SelectedDateEventAdapter(Activity context, List<Event> eventList) {
        events = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public SelectedDateEventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_selected_date_event, viewGroup, false);
        return new SelectedDateEventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedDateEventViewHolder viewHolder, int i) {

        String date = "";
        date += dateStartFormat.format(events.get(i).getStartDate());
        date += " - ";
        date += dateEndFormat.format(events.get(i).getEndDate());

        viewHolder.txtMonthlyEventTitle.setText(events.get(i).getTitle());
        viewHolder.txtMonthlyEventDate.setText(date);
        viewHolder.txtMonthlyEventVenue.setText(events.get(i).getVenue());
        viewHolder.itemView.setOnClickListener(
                new EventEditListener(context, events.get(i)));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class SelectedDateEventViewHolder extends RecyclerView.ViewHolder {
        private TextView txtMonthlyEventTitle, txtMonthlyEventDate, txtMonthlyEventVenue;
        private View itemView;

        SelectedDateEventViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMonthlyEventTitle = itemView.findViewById(R.id.txtMontlyEventTitle);
            txtMonthlyEventDate = itemView.findViewById(R.id.txtMonthlyEventTime);
            txtMonthlyEventVenue = itemView.findViewById(R.id.txtMonthlyEventVenue);
            this.itemView = itemView;

        }
    }
}
