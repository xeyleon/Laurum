package com.example.laurum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    static final String ACTIVITY_NAME = "Main";
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Laurum);
        setContentView(R.layout.activity_main);

        //Remove Title Bar
        Objects.requireNonNull(getSupportActionBar()).hide();

//        tabLayout = findViewById(R.id.MainTabLayout);
//        TabLayout.OnTabSelectedListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(android.R.color.black));
//            }
//        });


//        TabLayout.Tab coursesTab = tabLayout.newTab();
//        coursesTab.setText("Courses"); // set the text for the tab
//        coursesTab.setIcon(R.drawable.ic_courses_tab); // set an icon for the tab
//        tabLayout.addTab(coursesTab);
//        TabLayout.Tab facultyTab = tabLayout.newTab();
//        facultyTab.setText("Faculty"); // set the text for the tab
//        facultyTab.setIcon(R.drawable.ic_faculty_tab); // set an icon for the tab
//        tabLayout.addTab(facultyTab);
//        TabLayout.Tab resourcesTab = tabLayout.newTab();
//        resourcesTab.setText("Resources"); // set the text for the tab
//        resourcesTab.setIcon(R.drawable.ic_resources_tab); // set an icon for the tab
//        tabLayout.addTab(resourcesTab);
//        TabLayout.Tab settingsTab = tabLayout.newTab();
//        settingsTab.setText("Settings"); // set the text for the tab
//        settingsTab.setIcon(R.drawable.ic_settings_tab); // set an icon for the tab
//        tabLayout.addTab(settingsTab);
    }
}