package com.example.laurum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.laurum.Courses.CoursesFragment;
import com.example.laurum.Faculty.FacultyFragment;
import com.example.laurum.Reminders.ReminderFragment;
import com.example.laurum.Resources.ResourceFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    static final String ACTIVITY_NAME = "Main";
    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Laurum);
        setContentView(R.layout.activity_main);

        //Remove Title Bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        tabLayout = findViewById(R.id.MainTabLayout);
        frameLayout = findViewById(R.id.MainFrameLayout);
        switch(tabLayout.getSelectedTabPosition()){
            case 0:
                fragment = new ScheduleFragment();
                break;
            case 1:
                fragment = new ReminderFragment();
                break;
            case 2:
                fragment = new DegreeFragment();
                break;
            case 3:
                fragment = new CoursesFragment();
                break;
            case 4:
                fragment = new FacultyFragment();
                break;
            case 5:
                fragment = new ResourceFragment();
                break;
            case 6:
                fragment = new SettingsFragment();
                break;
            default:
                fragment = new ScheduleFragment();
                break;
        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.MainFrameLayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new ScheduleFragment();
                        break;
                    case 1:
                        fragment = new ReminderFragment();
                        break;
                    case 2:
                        fragment = new DegreeFragment();
                        break;
                    case 3:
                        fragment = new CoursesFragment();
                        break;
                    case 4:
                        fragment = new FacultyFragment();
                        break;
                    case 5:
                        fragment = new ResourceFragment();
                        break;
                    case 6:
                        fragment = new SettingsFragment();
                        break;
                    default:
                        fragment = new ScheduleFragment();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.MainFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }
}