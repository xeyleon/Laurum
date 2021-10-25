package com.laurum.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.laurum.Resources.Resource;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class LaurumDB {
    private SQLiteDatabase db;

    // Database Info
    private static final String DATABASE_NAME = "laurum.db";
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

    //Constructor
    public static List<Resource> getResourceList(SQLiteDatabase db) {
        List<Resource> resources = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ", TABLE_RESOURCES);

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
                } while (cursor.moveToNext());
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
}