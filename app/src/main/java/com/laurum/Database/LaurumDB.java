package com.laurum.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.laurum.Courses.Course;
import com.laurum.Faculty.Faculty;
import com.laurum.Resources.Resource;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class LaurumDB {
    private SQLiteDatabase db;

    // Database Info
    public static final String DATABASE_NAME = "laurum.db";
    public static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_COURSES = "courses";
    public static final String TABLE_FACULTY = "faculty";
    public static final String TABLE_DEGREE = "degree";
    public static final String TABLE_SCHEDULE = "schedule";
    public static final String TABLE_REMINDERS = "reminders";
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
                    Integer icon = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_RES_ICON));
                    Resource resource = new Resource(id, title, desc, url, icon);
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

    public static List<Course> getCourseList(SQLiteDatabase db) {
        List<Course> courses = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ", TABLE_COURSES);

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
                } while (cursor.moveToNext());
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

    public static List<Faculty> getFacultyList(SQLiteDatabase db) {
        List<Faculty> faculty = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ", TABLE_FACULTY);

        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_FACULTY_ID));
                    String first_name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FACULTY_FNAME));
                    String last_name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FACULTY_LNAME));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FACULTY_EMAIL));
                    Faculty member = new Faculty(id, first_name, last_name, email);
                    faculty.add(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("E", "Error while trying to get courses from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return faculty;
    }
}