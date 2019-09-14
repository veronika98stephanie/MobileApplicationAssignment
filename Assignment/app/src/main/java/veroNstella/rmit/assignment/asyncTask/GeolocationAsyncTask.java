package veroNstella.rmit.assignment.asyncTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import veroNstella.rmit.assignment.model.event.Event;
import veroNstella.rmit.assignment.view.CreateNotification;

public class GeolocationAsyncTask extends AsyncTask<String, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private Event event;
    private String duration = null;
    private int notificationId;

    public GeolocationAsyncTask(Context context, Event event, int notificationId) {
        this.context = context;
        this.event = event;
        this.notificationId = notificationId;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statuscode = con.getResponseCode();
            if (statuscode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    line = br.readLine();
                }
                String json = sb.toString();
                JSONObject root = new JSONObject(json);
                JSONArray arrayRows = root.getJSONArray("rows");
                JSONObject objectRows = arrayRows.getJSONObject(0);
                JSONArray arrayElements = objectRows.getJSONArray("elements");
                JSONObject objectElements = arrayElements.getJSONObject(0);
                JSONObject duration = objectElements.getJSONObject("duration");

                this.duration = duration.getString("value");
                return duration.getString("value");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String aDouble) {
        super.onPostExecute(aDouble);
        if (duration != null) {
            Date today = new Date();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Date eventStartDate = event.getStartDate();
            Date eventEndDate = event.getEndDate();

            //duration in seconds
            //shared preferences in minutes
            if (eventStartDate.after(today) && today.before(eventEndDate)) {
                //if today + distance matrix(seconds) + notification threshold (minutes) >= event start date;
                // send notif
                Calendar todayCal = Calendar.getInstance();
                todayCal.setTime(today);
                todayCal.add(Calendar.SECOND, Integer.parseInt(duration));
                todayCal.add(Calendar.MINUTE, Integer.parseInt(prefs.getString("notification_threshold", "5")));
                Calendar eventCal = Calendar.getInstance();
                eventCal.setTime(eventStartDate);
                if (todayCal.after(eventCal) || todayCal.equals(eventCal)) {
                    CreateNotification createNotification = new CreateNotification(context);
                    createNotification.sendNotification(event.getEventId(), event.getTitle(),
                            event.getVenue() + ", at " + event.getStartDate().toString(),
                            notificationId);
                }
            }
        }
    }
}

