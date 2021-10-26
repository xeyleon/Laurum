package com.laurum.Database;

import android.app.Application;
import android.content.ContentValues;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DatabaseHelper extends SQLiteOpenHelper {

    private DatabaseHelper sInstance;
    private final Context context;

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
    public static final String KEY_FACULTY_NAME = "name";
    public static final String KEY_FACULTY_TITLE = "title";
    public static final String KEY_FACULTY_DEP = "department";
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
        this.context = context;
    }

    public synchronized DatabaseHelper getInstance(Context context) {
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

        try
        {
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(loadJSONFromAsset("courses.json")));
            JSONArray jsonArray = jsonObject.getJSONArray("courses");
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject json = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(KEY_COURSE_ID, json.getString("id"));
                values.put(KEY_COURSE_TITLE, json.getString("title"));
                values.put(KEY_COURSE_DESC, json.getString("description"));
                values.put(KEY_COURSE_CREDIT, json.getString("credits"));
                db.insert(TABLE_COURSES, null, values);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void initFacultyTable(SQLiteDatabase db) {
        String CREATE_FACULTY_TABLE = "CREATE TABLE " + TABLE_FACULTY +
                "(" +
                KEY_FACULTY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_FACULTY_NAME + " TEXT," +
                KEY_FACULTY_TITLE + " TEXT," +
                KEY_FACULTY_DEP + " TEXT," +
                KEY_FACULTY_EMAIL + " TEXT" +
                ")";
        db.execSQL(CREATE_FACULTY_TABLE);

        try
        {
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(loadJSONFromAsset("faculty.json")));
            JSONArray jsonArray = jsonObject.getJSONArray("faculty");
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject json = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(KEY_FACULTY_NAME, json.getString("name"));
                values.put(KEY_FACULTY_TITLE, json.getString("title"));
                values.put(KEY_FACULTY_DEP, json.getString("department"));
                values.put(KEY_FACULTY_EMAIL, json.getString("email"));
                db.insert(TABLE_FACULTY, null, values);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
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

        try
        {
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(loadJSONFromAsset("resources.json")));
            JSONArray jsonArray = jsonObject.getJSONArray("resources");
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject json = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(KEY_RES_TITLE, json.getString("title"));
                values.put(KEY_RES_DESC, json.getString("description"));
                values.put(KEY_RES_URL, json.getString("url"));
                values.put(KEY_RES_ICON, json.getString("icon"));
                db.insert(TABLE_RESOURCES, null, values);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private String loadJSONFromAsset(String jsonFile){
        String json;
        try {
            InputStream is = context.getAssets().open(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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