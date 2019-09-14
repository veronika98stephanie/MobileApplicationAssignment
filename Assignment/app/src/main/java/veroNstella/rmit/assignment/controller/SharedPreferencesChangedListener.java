package veroNstella.rmit.assignment.controller;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;

import veroNstella.rmit.assignment.service.ApplicationServices;
import veroNstella.rmit.assignment.view.SettingsFragment;

public class SharedPreferencesChangedListener implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SettingsFragment fragment;
    private EditTextPreference notificationThresholdPref;
    private EditTextPreference notificationPeriodPref;
    private EditTextPreference remindAgainPref;

    public SharedPreferencesChangedListener(
            SettingsFragment fragment,
            EditTextPreference notificationThresholdPref,
            EditTextPreference notificationPeriodPref,
            EditTextPreference remindAgainPref) {
        this.fragment = fragment;
        this.notificationThresholdPref = notificationThresholdPref;
        this.notificationPeriodPref = notificationPeriodPref;
        this.remindAgainPref = remindAgainPref;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (notificationThresholdPref != null) {
            this.fragment.setPreferencesSummary(notificationThresholdPref, sharedPreferences,
                    "notification_threshold");
        }

        if (notificationPeriodPref != null) {
            this.fragment.setPreferencesSummary(notificationPeriodPref, sharedPreferences,
                    "notification_period");
        }

        if (remindAgainPref != null) {
            this.fragment.setPreferencesSummary(remindAgainPref, sharedPreferences,
                    "remind_again");
        }

        ApplicationServices applicationServices = new ApplicationServices(this.fragment.getActivity());
        applicationServices.restartNotificationService();
    }
}
