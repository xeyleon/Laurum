package com.laurum;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.snackbar.Snackbar;

public class AboutActivity extends AppCompatActivity {
    static final String ACTIVITY_NAME = "About Laurum";
    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Force Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);

        // Load Theme
        setTheme(R.style.Theme_Laurum);

        setContentView(R.layout.activity_about);

        Snackbar.make(findViewById(R.id.about_laurum_details),"test",Snackbar.LENGTH_LONG).setAction("Action",null).show();

        // Change Status Bar Color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.wlu_purple));
        }
    }

    // ActionBar Back Button Functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            KeyEvent key_press = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
            this.dispatchKeyEvent(key_press);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}