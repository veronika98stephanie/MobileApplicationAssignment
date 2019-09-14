package veroNstella.rmit.assignment.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.controller.AttendeeRemoveListener;
import veroNstella.rmit.assignment.model.attendee.Attendee;

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.AttendeeViewHolder> {
    private List<Attendee> attendees;
    private Context context;

    public AttendeeAdapter(Context context, List<Attendee> attendeeList) {
        attendees = attendeeList;
        this.context = context;
    }

    @NonNull
    @Override
    public AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.layout_attendee_list, viewGroup, false);
        return new AttendeeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeAdapter.AttendeeViewHolder attendeeViewHolder, int i) {
        attendeeViewHolder.txtAttendeeName.setText(attendees.get(i).getName());
        attendeeViewHolder.imgRemoveAttendee.setTag(attendees.get(i));
        attendeeViewHolder.imgRemoveAttendee.setOnClickListener(
                new AttendeeRemoveListener(context, attendees.indexOf(
                        (Attendee) attendeeViewHolder.imgRemoveAttendee.getTag())));
    }

    @Override
    public int getItemCount() {
        return attendees.size();
    }

    class AttendeeViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgRemoveAttendee;
        private TextView txtAttendeeName;

        AttendeeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRemoveAttendee = itemView.findViewById(R.id.imgEventRemoveAttendee);
            txtAttendeeName = itemView.findViewById(R.id.txtEventAttendee);
        }
    }
}
