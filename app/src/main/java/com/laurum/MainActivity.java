package com.laurum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.laurum.Courses.CoursesFragment;
import com.laurum.Database.DatabaseHelper;
import com.laurum.Database.LaurumDB;
import com.laurum.Faculty.FacultyFragment;
import com.laurum.Resources.Resource;
import com.laurum.Resources.ResourceFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    static final String ACTIVITY_NAME = "Main";
    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private DatabaseHelper dbHelper;
    private LaurumDB db;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Force Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Laurum);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.wlu_purple));
        }

        setContentView(R.layout.activity_main);

        //Remove Title Bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Init Database
        db = new LaurumDB(this);

        tabLayout = findViewById(R.id.MainTabLayout);
        frameLayout = findViewById(R.id.MainFrameLayout);
        switch(tabLayout.getSelectedTabPosition()){
            case 0:
                fragment = new DegreeFragment();
                break;
            case 1:
                fragment = new CoursesFragment();
                break;
            case 2:
                fragment = new FacultyFragment();
                break;
            case 3:
                fragment = new ResourceFragment();
                break;
            case 4:
                fragment = new SettingsFragment();
                break;
            default:
                fragment = new DegreeFragment();
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
                        fragment = new DegreeFragment();
                        break;
                    case 1:
                        fragment = new CoursesFragment();
                        break;
                    case 2:
                        fragment = new FacultyFragment();
                        break;
                    case 3:
                        fragment = new ResourceFragment();
                        break;
                    case 4:
                        fragment = new SettingsFragment();
                        break;
                    default:
                        fragment = new DegreeFragment();
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