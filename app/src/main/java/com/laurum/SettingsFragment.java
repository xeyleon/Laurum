package com.laurum;

import static androidx.core.app.ActivityCompat.recreate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.tabs.TabLayout;

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

        //Developer Info
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

        //Version Info
        Preference version = findPreference("version_info");
        Objects.requireNonNull(version).setOnPreferenceClickListener(v->{
            Toast.makeText(getContext(), R.string.version_info_msg, Toast.LENGTH_SHORT).show();
            return true;
        });

        //Dark Theme Toggle
//        CheckBoxPreference checkbox = findPreference("dark_theme_enabled");
//        Objects.requireNonNull(checkbox).setOnPreferenceChangeListener((v, c)->{
//            if (c.toString().compareTo("true") == 0){
//                Toast.makeText(getContext(), R.string.dark_enable_msg, Toast.LENGTH_SHORT).show();
//                getActivity().setTheme(R.style.Theme_LaurumDark);
//            }
//            else {
//                Toast.makeText(getContext(), R.string.dark_disable_msg, Toast.LENGTH_SHORT).show();
//                getActivity().setTheme(R.style.Theme_Laurum);
//            }
//            recreate(getActivity());
//            return true;
//        });

        //Starting Credits
        EditTextPreference starting_credits = findPreference("starting_credits_key");
        if (starting_credits != null) {
            starting_credits.setSummary(sharedPreferences.getString("starting_credits_key", ""));
            starting_credits.setOnBindEditTextListener(editText ->
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL)
            );
        }

        //Required Credits
        EditTextPreference required_credits = findPreference("required_credits_key");
        if (required_credits != null) {
            required_credits.setSummary(sharedPreferences.getString("required_credits_key", ""));
            required_credits.setOnBindEditTextListener(editText ->
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL)
            );
        }

        //About Laurum
        Preference user_manual = findPreference("about_laurum");
        Objects.requireNonNull(user_manual).setOnPreferenceClickListener(v->{
//            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.user_manual_dialog, null);
//
//            TabLayout tabLayout  = view.findViewById(R.id.user_manual_tabs);
//            FrameLayout frameLayout = view.findViewById(R.id.user_manual_frame);
//            View frameView = inflater.inflate(R.layout.info_dialog,frameLayout,false);
//            frameLayout.addView(frameView);
//            TextView info_dlg = frameView.findViewById(R.id.info_dlg_msg);
//            TranslateAnimation animObj= new TranslateAnimation(300, info_dlg.getWidth(), 0, 0);
//            animObj.setDuration(500);
//            info_dlg.startAnimation(animObj);
//            info_dlg.setText(R.string.how_to_use_msg);
//            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//               public void onTabSelected(TabLayout.Tab tab) {
//                   info_dlg.startAnimation(animObj);
//                   switch (tab.getPosition()) {
//                       case 0:
//                           info_dlg.setText(R.string.how_to_use_msg);
//                           break;
//                       case 1:
//                           info_dlg.setText(R.string.degree_tracking_msg);
//                           break;
//                       default:
//                           info_dlg.setText(R.string.how_to_use_msg);
//                           break;
//                   }
//               }
//                @Override
//                public void onTabUnselected(TabLayout.Tab tab) {
//                }
//                @Override
//                public void onTabReselected(TabLayout.Tab tab) {
//                }
//            });
//
//            builder.setView(view);
//            builder.setNegativeButton("Close", (dialog, id) -> {
//
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
            Intent intent = new Intent(this.getContext(), AboutActivity.class);
            startActivity(intent);
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
        }
        else if (preference instanceof EditTextPreference) {
            if (key.compareTo("required_credits_key") == 0)
                if (Float.parseFloat(String.valueOf(((EditTextPreference) preference).getText())) == 0.0) {
                    ((EditTextPreference) preference).setText("20");
                    Toast.makeText(preference.getContext(), "Required credits must be greater than 0.", Toast.LENGTH_SHORT).show();
                }
            preference.setSummary(((EditTextPreference) preference).getText());
        } else {
            if (preference != null) {
                preference.setSummary(sharedPreferences.getString(key, ""));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}