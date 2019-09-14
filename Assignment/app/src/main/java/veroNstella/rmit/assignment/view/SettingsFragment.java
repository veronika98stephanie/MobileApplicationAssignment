package veroNstella.rmit.assignment.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import veroNstella.rmit.assignment.R;
import veroNstella.rmit.assignment.controller.SharedPreferencesChangedListener;

public class SettingsFragment extends PreferenceFragment {
    private SharedPreferencesChangedListener sharedPreferencesChangedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        String notificationThreshold = "notification_threshold";
        String notificationPeriod = "notification_period";
        String remindAgain = "remind_again";

        EditTextPreference notificationThresholdPref = (EditTextPreference) findPreference(notificationThreshold);
        EditTextPreference notificationPeriodPref = (EditTextPreference) findPreference(notificationPeriod);
        EditTextPreference remindAgainPref = (EditTextPreference) findPreference(remindAgain);

        sharedPreferencesChangedListener = new SharedPreferencesChangedListener
                (this, notificationThresholdPref, notificationPeriodPref, remindAgainPref);

        this.setPreferencesSummary(notificationThresholdPref, getPreferenceScreen()
                .getSharedPreferences(), notificationThreshold);

        this.setPreferencesSummary(notificationPeriodPref, getPreferenceScreen()
                .getSharedPreferences(), notificationPeriod);

        this.setPreferencesSummary(remindAgainPref, getPreferenceScreen()
                .getSharedPreferences(), remindAgain);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(sharedPreferencesChangedListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(sharedPreferencesChangedListener);
    }

    public void setPreferencesSummary(
            EditTextPreference editTextPreference,
            SharedPreferences sharedPreferences,
            String key) {
        if (editTextPreference != null) {
            new Thread(() -> {
                String data = sharedPreferences.getString(key, "5");
                getActivity().runOnUiThread(() -> editTextPreference.setSummary(data + " minutes"));
            }).start();
        }
    }
}
