package veroNstella.rmit.assignment.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.model.Model;
import veroNstella.rmit.assignment.model.ModelImpl;
import veroNstella.rmit.assignment.model.event.Event;


public class CalendarAdapter extends ArrayAdapter<Calendar> {
    private List<Event> events;
    private Calendar currMonth;
    private LayoutInflater layoutInflater;
    private Calendar now;
    private Context context;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public CalendarAdapter(Context context, ArrayList<Calendar> dates, Calendar currMonth) {
        super(context, R.layout.layout_calendar_item, dates);
        this.context = context;
        this.currMonth = currMonth;
        layoutInflater = LayoutInflater.from(context);
        this.now = Calendar.getInstance();
        Model model = ModelImpl.getSingletonInstance(context);
        events = model.getEventList();
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        Calendar date = getItem(position);

        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_calendar_item, parent, false);
        }

        if (date != null && date.get(Calendar.DATE) == currMonth.get(Calendar.DATE)
                && date.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                && date.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            ((TextView) view).setTypeface(null, Typeface.BOLD);
            ((TextView) view).setTextColor(context.getColor(R.color.colorAccent));
        }

        if (date != null && date.get(Calendar.MONTH) != currMonth.get(Calendar.MONTH)) {
            ((TextView) view).setTextColor(context.getColor(R.color.greyedOut));
        }

        if (date != null) {
            ((TextView) view).setText(String.valueOf(date.get(Calendar.DATE)));
        }

        @SuppressLint("DefaultLocale")
        String formattedDate = null;
        if (date != null) {
            formattedDate = String.format("%02d/%02d/%04d", date.get(Calendar.DATE),
                    date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
        }

        // if there is event on current date, set the background of the text
        for (Event event : events) {
            if (sdf.format(event.getStartDate()).equals(formattedDate)) {
                ((TextView) view).setBackground(context.getDrawable(R.drawable.rounded_corner));
            }
        }

        view.setTag(date);

        return view;
    }
}
