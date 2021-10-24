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
    private static final String DATABASE_NAME = "LaurumDB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_COURSES = "courses";
    private static final String TABLE_FACULTY = "faculty";
    private static final String TABLE_DEGREE = "degree";
    private static final String TABLE_SCHEDULE = "schedule";
    private static final String TABLE_REMINDERS = "reminders";
    private static final String TABLE_RESOURCES = "resources";
    private static final String TABLE_SETTINGS = "settings";

    // Courses Table Columns
    private static final String KEY_COURSE_ID = "course_id";
    private static final String KEY_COURSE_TITLE = "course_title";
    private static final String KEY_COURSE_DESC = "course_desc";
    public static final String KEY_COURSE_CREDIT = "course_credit";

    // Faculty Table Columns
    private static final String KEY_FACULTY_ID = "id";
    private static final String KEY_FACULTY_FNAME = "first_name";
    private static final String KEY_FACULTY_LNAME = "last_name";
    private static final String KEY_FACULTY_EMAIL = "email";

    // Degree Table Columns

    // Schedule Table Columns

    // Reminders Table Columns

    // Resources Table Columns
    private static final String KEY_RES_ID = "id";
    private static final String KEY_RES_TITLE = "res_id";
    private static final String KEY_RES_DESC = "res_desc";
    private static final String KEY_RES_URL = "res_url";

    // Settings Table Columns


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
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
        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES +
                "(" +
                KEY_COURSE_ID + " TEXT PRIMARY KEY," + // Define a primary key
                KEY_COURSE_TITLE + " TEXT REFERENCES " + TABLE_COURSES + "," + // Define a foreign key
                KEY_COURSE_DESC + " TEXT," +
                KEY_COURSE_CREDIT + " REAL" +
                ")";

        String CREATE_FACULTY_TABLE = "CREATE TABLE " + TABLE_FACULTY +
                "(" +
                KEY_FACULTY_ID + " INTEGER PRIMARY KEY," +
                KEY_FACULTY_LNAME + " TEXT," +
                KEY_FACULTY_FNAME + " TEXT," +
                KEY_FACULTY_EMAIL + " TEXT" +
                ")";

        db.execSQL(CREATE_COURSES_TABLE);
        db.execSQL(CREATE_FACULTY_TABLE);
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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEGREE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESOURCES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
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

    public List<Resource> getResourceList() {
        List<Resource> resources = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ", TABLE_RESOURCES);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_RES_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_RES_TITLE));
                    String desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_RES_DESC));
                    String url = cursor.getString(cursor.getColumnIndexOrThrow(KEY_RES_URL));
                    Resource resource = new Resource(id, title, desc, url);
                    resources.add(resource);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("E", "Error while trying to get courses from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return resources;
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