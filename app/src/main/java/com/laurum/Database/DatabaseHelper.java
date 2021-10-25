package com.laurum.Database;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.laurum.Courses.Course;
import com.laurum.R;
import com.laurum.Resources.Resource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    // Database Info
    public static final String DATABASE_NAME = "laurum.db";
    public static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_COURSES = "courses";
    public static final String TABLE_FACULTY = "faculty";
    public static final String TABLE_DEGREE = "degree";
    public static final String TABLE_RESOURCES = "resources";
    public static final String TABLE_SETTINGS = "settings";

    // Courses Table Columns
    public static final String KEY_COURSE_ID = "course_id";
    public static final String KEY_COURSE_TITLE = "course_title";
    public static final String KEY_COURSE_DESC = "course_desc";
    public static final String KEY_COURSE_CREDIT = "course_credit";

    // Faculty Table Columns
    public static final String KEY_FACULTY_ID = "id";
    public static final String KEY_FACULTY_FNAME = "first_name";
    public static final String KEY_FACULTY_LNAME = "last_name";
    public static final String KEY_FACULTY_EMAIL = "email";

    // Degree Table Columns

    // Schedule Table Columns

    // Reminders Table Columns

    // Resources Table Columns
    public static final String KEY_RES_ID = "id";
    public static final String KEY_RES_TITLE = "res_title";
    public static final String KEY_RES_DESC = "res_desc";
    public static final String KEY_RES_URL = "res_url";
    public static final String KEY_RES_ICON = "res_icon";

    // Settings Table Columns


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {

        initCoursesTable(db);
        initFacultyTable(db);
        initResourcesTable(db);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACULTY);
            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEGREE);
            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESOURCES);
            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
            onCreate(db);
        }
    }

    private void initCoursesTable(SQLiteDatabase db) {
        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES +
                "(" +
                KEY_COURSE_ID + " TEXT PRIMARY KEY," + // Define a primary key
                KEY_COURSE_TITLE + " TEXT," + // Define a foreign key
                KEY_COURSE_DESC + " TEXT," +
                KEY_COURSE_CREDIT + " REAL" +
                ")";
        db.execSQL(CREATE_COURSES_TABLE);

        String INSERTION = String.format("INSERT INTO %s(%s, %s, %s, %s) VALUES", TABLE_COURSES, KEY_COURSE_ID, KEY_COURSE_TITLE, KEY_COURSE_DESC, KEY_COURSE_CREDIT);
        String[] INIT_COURSES_QUERIES = {
                String.format(INSERTION+"('%s', '%s', '%s', '%.2f')", "CP212","Uhh","YES!!", 2.5),
                String.format(INSERTION+"('%s', '%s', '%s', '%.2f')", "CP999","Woo!!","??", 9.9),
        };

        for (String query : INIT_COURSES_QUERIES) {
            db.execSQL(query);
        }
    }

    private void initFacultyTable(SQLiteDatabase db) {
        String CREATE_FACULTY_TABLE = "CREATE TABLE " + TABLE_FACULTY +
                "(" +
                KEY_FACULTY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_FACULTY_LNAME + " TEXT," +
                KEY_FACULTY_FNAME + " TEXT," +
                KEY_FACULTY_EMAIL + " TEXT" +
                ")";
        db.execSQL(CREATE_FACULTY_TABLE);

        String INSERTION = String.format("INSERT INTO %s(%s, %s, %s) VALUES", TABLE_FACULTY, KEY_FACULTY_FNAME, KEY_FACULTY_LNAME, KEY_FACULTY_EMAIL);
        String[] INIT_FACULTY_QUERIES = {
                String.format(INSERTION+"('%s', '%s', '%s')", "Joe","Blow","ho@gmail.com"),
                String.format(INSERTION+"('%s', '%s', '%s')", "Jim","Bob","lol@gmail.com"),
        };

        for (String query : INIT_FACULTY_QUERIES) {
            db.execSQL(query);
        }
    }

    private void initResourcesTable(SQLiteDatabase db) {
        String CREATE_RESOURCES_TABLE = "CREATE TABLE " + TABLE_RESOURCES +
                "(" +
                KEY_RES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_RES_TITLE + " TEXT," +
                KEY_RES_DESC + " TEXT," +
                KEY_RES_URL + " TEXT," +
                KEY_RES_ICON + " INTEGER" +
                ")";
        db.execSQL(CREATE_RESOURCES_TABLE);

        String INSERTION = String.format("INSERT INTO %s(%s, %s, %s, %s) VALUES", TABLE_RESOURCES, KEY_RES_TITLE, KEY_RES_DESC, KEY_RES_URL, KEY_RES_ICON);
        String[] INIT_RESOURCE_QUERIES = {
                String.format(INSERTION+"('%s', '%s', '%s', '%s')", "Campus Map (Waterloo)", "Map of Wilfrid Laurier University Waterloo campus", "https://map.concept3d.com/?id=638#!", R.drawable.ic_map),
                String.format(INSERTION+"('%s', '%s', '%s', '%s')", "Campus Map (Brantford)", "Map of Wilfrid Laurier University Brantford campus", "https://map.concept3d.com/?id=573#!", R.drawable.ic_map),
                String.format(INSERTION+"('%s', '%s', '%s', '%s')", "LORIS", "Laurier Online Registration and Information System", "https://loris.wlu.ca/", R.drawable.ic_golden_hawk),
                String.format(INSERTION+"('%s', '%s', '%s', '%s')", "myDegree", "A visual tool to help students with course planning.", "https://mydegree.wlu.ca/", R.drawable.ic_golden_hawk),
                String.format(INSERTION+"('%s', '%s', '%s', '%s')", "Visual Schedule Builder", "A visual tool to help students with course schedule planning", "https://scheduleme.wlu.ca", R.drawable.ic_schedule),
        };

        for (String query : INIT_RESOURCE_QUERIES) {
            db.execSQL(query);
        }

    }

//    private void copyDatabase() throws IOException {
//        try (InputStream inputStream = Application.getAssets().open("databasefilename")) {
//            //Create the file before
//            OutputStream outputStream = new FileOutputStream(mDbPath);
//            byte[] buffer = new byte[4096];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//            outputStream.flush();
//            outputStream.close();
//            inputStream.close();
//        }
//    }
}