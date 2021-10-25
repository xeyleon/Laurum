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
    private static final String DATABASE_NAME = LaurumDB.DATABASE_NAME;
    private static final int DATABASE_VERSION = LaurumDB.DATABASE_VERSION;

    // Table Names
    private static final String TABLE_COURSES = LaurumDB.TABLE_COURSES;
    private static final String TABLE_FACULTY = LaurumDB.TABLE_FACULTY;
    private static final String TABLE_DEGREE = LaurumDB.TABLE_DEGREE;
    private static final String TABLE_RESOURCES = LaurumDB.TABLE_RESOURCES;
    private static final String TABLE_SETTINGS = LaurumDB.TABLE_SETTINGS;

    // Courses Table Columns
    private static final String KEY_COURSE_ID = LaurumDB.KEY_COURSE_ID;
    private static final String KEY_COURSE_TITLE = LaurumDB.KEY_COURSE_TITLE;
    private static final String KEY_COURSE_DESC = LaurumDB.KEY_COURSE_DESC;
    public static final String KEY_COURSE_CREDIT = LaurumDB.KEY_COURSE_CREDIT;

    // Faculty Table Columns
    private static final String KEY_FACULTY_ID = LaurumDB.KEY_FACULTY_ID;
    private static final String KEY_FACULTY_FNAME = LaurumDB.KEY_FACULTY_FNAME;
    private static final String KEY_FACULTY_LNAME = LaurumDB.KEY_FACULTY_LNAME;
    private static final String KEY_FACULTY_EMAIL = LaurumDB.KEY_FACULTY_EMAIL;

    // Degree Table Columns

    // Schedule Table Columns

    // Reminders Table Columns

    // Resources Table Columns
    private static final String KEY_RES_ID = LaurumDB.KEY_RES_ID;
    private static final String KEY_RES_TITLE = LaurumDB.KEY_RES_TITLE;
    private static final String KEY_RES_DESC = LaurumDB.KEY_RES_DESC;
    private static final String KEY_RES_URL = LaurumDB.KEY_RES_URL;
    private static final String KEY_RES_ICON = LaurumDB.KEY_RES_ICON;

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

    public List<Course> getCourseList() {
        List<Course> courses = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ", TABLE_COURSES);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_TITLE));
                    String desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_DESC));
                    Double credits = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_COURSE_CREDIT));
                    Course course = new Course(id, title, desc, credits);
                    courses.add(course);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("E", "Error while trying to get courses from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return courses;
    }

    private void initCoursesTable(SQLiteDatabase db) {
        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES +
                "(" +
                KEY_COURSE_ID + " TEXT PRIMARY KEY," + // Define a primary key
                KEY_COURSE_TITLE + " TEXT REFERENCES " + TABLE_COURSES + "," + // Define a foreign key
                KEY_COURSE_DESC + " TEXT," +
                KEY_COURSE_CREDIT + " REAL" +
                ")";
        db.execSQL(CREATE_COURSES_TABLE);
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
                String.format(INSERTION+"('%s', '%s', '%s', '%s')", "Student Services", "...", "", R.drawable.ic_golden_hawk),
                String.format(INSERTION+"('%s', '%s', '%s', '%s')", "Wellness Centre", "....", "", R.drawable.ic_golden_hawk),
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