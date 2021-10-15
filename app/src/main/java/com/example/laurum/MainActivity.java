package com.example.laurum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Laurum);
        setContentView(R.layout.activity_main);

        //Remove Title Bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Populate tabs
        tabLayout = findViewById(R.id.MainTabLayout);
        TabLayout.Tab reminderTab = tabLayout.newTab();
        reminderTab.setText("Reminders"); // set the text for the tab
        reminderTab.setIcon(R.drawable.ic_reminders_tab); // set an icon for the tab
        tabLayout.addTab(reminderTab);
    }
}