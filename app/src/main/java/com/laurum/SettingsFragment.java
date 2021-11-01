package com.laurum;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.tabs.TabLayout;
import com.laurum.Courses.CoursesFragment;
import com.laurum.Degree.DegreeFragment;
import com.laurum.Faculty.FacultyFragment;
import com.laurum.R;
import com.laurum.Resources.ResourceFragment;

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
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.course_search_key));
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.faculty_search_key));

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

//        ListPreference coursePref = (ListPreference) findPreference("course_search_key");
//        if(coursePref.getValue() == null){
//            coursePref.setValueIndex(1);
//        }
//
//        ListPreference facultyPref = (ListPreference) findPreference("faculty_search_key");
//        if(facultyPref.getValue() == null){
//            facultyPref.setValueIndex(1);
//        }

        Preference developer = findPreference("developer_info");
        Objects.requireNonNull(developer).setOnPreferenceClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.developer_info_dialog, null);
            builder.setView(view);
            builder.setNegativeButton("Close", (dialog, id) -> {

            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });

        Preference version = findPreference("version_info");
        Objects.requireNonNull(version).setOnPreferenceClickListener(v->{
            Toast.makeText(getContext(), R.string.version_info_msg, Toast.LENGTH_SHORT).show();
            return true;
        });

        CheckBoxPreference checkbox = findPreference("theme_preference");
        Objects.requireNonNull(checkbox).setOnPreferenceChangeListener((v, c)->{
            if (c.toString().compareTo("true") == 0)
                Toast.makeText(getContext(), R.string.dark_enable_msg, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), R.string.dark_disable_msg, Toast.LENGTH_SHORT).show();
            return true;
        });

        Preference user_manual = findPreference("user_manual");
        Objects.requireNonNull(user_manual).setOnPreferenceClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.user_manual_dialog, null);

            TabLayout tabLayout  = view.findViewById(R.id.user_manual_tabs);
            FrameLayout frameLayout = view.findViewById(R.id.user_manual_frame);
            View frameView = inflater.inflate(R.layout.fragment_construction,frameLayout,false);
            frameLayout.addView(frameView);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
               public void onTabSelected(TabLayout.Tab tab) {
                   View frameView;
                   switch (tab.getPosition()) {
                       case 0:
                           frameView = inflater.inflate(R.layout.fragment_construction,frameLayout,false);
                           break;
                       case 1:
                           frameView = inflater.inflate(R.layout.fragment_construction,frameLayout,false);
                           break;
                       default:
                           frameView = inflater.inflate(R.layout.fragment_construction,frameLayout,false);
                           break;
                   }
                   frameLayout.removeAllViews();
                   frameLayout.addView(frameView);
               }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            builder.setView(view);
            builder.setNegativeButton("Close", (dialog, id) -> {

            });
            AlertDialog dialog = builder.create();
            dialog.show();
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