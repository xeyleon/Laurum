package com.laurum.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.laurum.Courses.Course;
import com.laurum.Faculty.Faculty;
import com.laurum.R;
import com.laurum.Resources.Resource;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class LaurumDB extends DatabaseHelper{
    private static SQLiteDatabase db = null;
    private static SharedPreferences sharedPreferences = null;

    public LaurumDB(Context context) {
        super(context);
        db = new DatabaseHelper(context).getWritableDatabase();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static List<Resource> getResourceList() {
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
                    String icon = cursor.getString(cursor.getColumnIndexOrThrow(KEY_RES_ICON));
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

    public static List<Course> getCourseList() {
        List<Course> courses = new ArrayList<>();

        String POSTS_SELECT_QUERY = String.format("SELECT * FROM %s ", TABLE_COURSES);
        POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("ORDER BY %s ASC", KEY_COURSE_ID));

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

    public static List<Faculty> getFacultyList() {
        List<Faculty> faculty = new ArrayList<>();

        String POSTS_SELECT_QUERY = String.format("SELECT * FROM %s ", TABLE_FACULTY);

        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_FACULTY_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FACULTY_NAME));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FACULTY_TITLE));
                    String department = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FACULTY_DEP));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FACULTY_EMAIL));
                    Faculty member = new Faculty(id, name, title, department, email);
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

    public static List<Course> searchCourseList(String search) {
        List<Course> courses = new ArrayList<>();
        String search_settings = sharedPreferences.getString("course_search_key","1");

        String POSTS_SELECT_QUERY = String.format("SELECT * FROM %s ", TABLE_COURSES);

        switch (Integer.parseInt(search_settings)) {
            case 1:
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("WHERE %s LIKE '%%%s%%' ",KEY_COURSE_ID, search));
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("OR %s LIKE '%%%s%%' ", KEY_COURSE_TITLE, search));
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("OR %s LIKE '%%%s%%' ", KEY_COURSE_DESC, search));
                break;
            case 2:
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("WHERE %s LIKE '%%%s%%' ",KEY_COURSE_ID, search));
                break;
            case 3:
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("WHERE %s LIKE '%%%s%%' ",KEY_COURSE_TITLE, search));
                break;
            case 4:
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("WHERE %s LIKE '%%%s%%' ",KEY_COURSE_DESC, search));
                break;
            default:
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("WHERE %s LIKE '%%%s%%' ",KEY_COURSE_ID, search));
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("OR %s LIKE '%%%s%%' ", KEY_COURSE_TITLE, search));
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("OR %s LIKE '%%%s%%' ", KEY_COURSE_DESC, search));
                break;
        }

        POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("ORDER BY %s ASC", KEY_COURSE_ID));
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

    public static Course getCourse(String course_id) {
        Course course = new Course();
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM %s ", TABLE_COURSES);
        POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("WHERE %s LIKE '%%%s%%' ",KEY_COURSE_ID, course_id));
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_TITLE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_DESC));
                    Double credits = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_COURSE_CREDIT));
                    course = new Course(id, title, desc, credits);
            }
        } catch (Exception e) {
            Log.e("E", "Error while trying to get courses from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return course;
    }

    public static List<Faculty> searchFacultyList(String search) {
        List<Faculty> faculty = new ArrayList<>();
        String search_settings = sharedPreferences.getString("faculty_search_key","1");

        String POSTS_SELECT_QUERY = String.format("SELECT * FROM %s ", TABLE_FACULTY);

        switch (Integer.parseInt(search_settings)) {
            case 1:
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("WHERE %s LIKE '%%%s%%' ",KEY_FACULTY_NAME, search));
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("OR %s LIKE '%%%s%%' ", KEY_FACULTY_TITLE, search));
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("OR %s LIKE '%%%s%%' ", KEY_FACULTY_DEP, search));
                break;
            case 2:
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("WHERE %s LIKE '%%%s%%' ",KEY_FACULTY_NAME, search));
                break;
            case 3:
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("WHERE %s LIKE '%%%s%%' ",KEY_FACULTY_TITLE, search));
                break;
            case 4:
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("WHERE %s LIKE '%%%s%%' ",KEY_FACULTY_DEP, search));
                break;
            default:
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("WHERE %s LIKE '%%%s%%' ",KEY_FACULTY_NAME, search));
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("OR %s LIKE '%%%s%%' ", KEY_FACULTY_TITLE, search));
                POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("OR %s LIKE '%%%s%%' ", KEY_FACULTY_DEP, search));
                break;
        }

        POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("ORDER BY %s ASC", KEY_FACULTY_NAME));
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_FACULTY_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FACULTY_NAME));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FACULTY_TITLE));
                    String department = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FACULTY_DEP));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FACULTY_EMAIL));
                    Faculty member = new Faculty(id, name, title, department, email);
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

    public static List<Course> getDegreeCourses() {
        List<Course> courses = new ArrayList<>();
        String search_settings = sharedPreferences.getString("course_search_key","1");

        String SELECT_QUERY = String.format("SELECT * FROM %s AS D, %s as C ", TABLE_DEGREE, TABLE_COURSES);
        SELECT_QUERY = SELECT_QUERY.concat(String.format("WHERE D.%s=C.%s ", KEY_COURSE_ID, KEY_COURSE_ID));
        //POSTS_SELECT_QUERY = POSTS_SELECT_QUERY.concat(String.format("ORDER BY %s ASC", KEY_COURSE_ID));
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_TITLE));
                    String desc = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_DESC));
                    Double credits = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_COURSE_CREDIT));
                    int status = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_DEGREE_STATUS));
                    Course course = new Course(id, title, desc, credits);
                    course.setStatus(status);
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

    public static void addToDegree(String course_id) {
        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_ID, course_id);
        db.insert(TABLE_DEGREE, null, values);
    }

    public static void removeFromDegree(String course_id) {
        db.delete(TABLE_DEGREE, String.format("%s = ?", KEY_COURSE_ID), new String[] {course_id}) ;
    }

    public static void degreeCourseStatusUpdate(String course_id, int status){
        ContentValues values = new ContentValues();
        values.put(KEY_DEGREE_STATUS, status);
        String[] whereArgs = {KEY_COURSE_ID, course_id};

        int value = db.update(TABLE_DEGREE, values, "? = ?", whereArgs);
        Log.i("I",""+value);
        //String UPDATE_QUERY = String.format("UPDATE %s SET %s = %d ", TABLE_DEGREE, KEY_DEGREE_STATUS, status);
        //UPDATE_QUERY = UPDATE_QUERY.concat(String.format("WHERE %s = \'%s\'", KEY_COURSE_ID, course_id));
        //Cursor cursor = db.rawQuery(UPDATE_QUERY, null);
        //cursor.close();
    }
}