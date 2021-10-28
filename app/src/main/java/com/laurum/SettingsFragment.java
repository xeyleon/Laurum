package com.laurum;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.laurum.R;

import java.util.Objects;

/**
 * shows the settings option for choosing the movie categories in ListPreference.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //add xml
        addPreferencesFromResource(R.xml.settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.settings_test_key));

    }


    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Preference about = findPreference("settings_about");
        Objects.requireNonNull(about).setOnPreferenceClickListener(v->{
            Toast.makeText(getContext(), "Developed by Group 11", Toast.LENGTH_SHORT).show();
            return true;
        });

        CheckBoxPreference checkbox = findPreference("theme_preference");
        Objects.requireNonNull(checkbox).setOnPreferenceChangeListener((v, c)->{
            if (c.toString().compareTo("true") == 0)
                Toast.makeText(getContext(), "Dark Theme enabled", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Dark Theme disabled", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        else if (preference instanceof CheckBoxPreference) {
                CheckBoxPreference check = (CheckBoxPreference) preference;
                check.setChecked(sharedPreferences.getBoolean(key, true));
        } else {
            preference.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}