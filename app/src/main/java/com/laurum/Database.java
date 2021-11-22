package com.laurum;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Database extends SQLiteOpenHelper {

    private Database sInstance;
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
    public static final String KEY_DEGREE_STATUS = "status";

    // Resources Table Columns
    public static final String KEY_RES_ID = "id";
    public static final String KEY_RES_TITLE = "res_title";
    public static final String KEY_RES_DESC = "res_desc";
    public static final String KEY_RES_URL = "res_url";
    public static final String KEY_RES_ICON = "res_icon";

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public synchronized Database getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new Database(context.getApplicationContext());
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
        initDegreeTable(db);
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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEGREE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESOURCES);
            onCreate(db);
        }
    }

    private void initDegreeTable(SQLiteDatabase db) {
        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_DEGREE +
                "(" +
                KEY_COURSE_ID + " TEXT PRIMARY KEY," +
                KEY_DEGREE_STATUS + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY ("+KEY_COURSE_ID+") REFERENCES " + TABLE_COURSES + "(" + KEY_COURSE_ID +
                "));";

        db.execSQL(CREATE_COURSES_TABLE);
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

    public static class LaurumDB extends Database {
        private static SQLiteDatabase db = null;
        private static SharedPreferences sharedPreferences = null;

        public LaurumDB(Context context) {
            super(context);
            db = new Database(context).getWritableDatabase();
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

        
        public static List<String> getCourseCategory(List<Course> courses){
            List<String> courseCategory = new ArrayList<>();

            for (int i = 0; i < courses.size(); i++){
                if (courseCategory.contains(courses.get(i)) == false){
                    courseCategory.add(courses.get(i).getId());
                }
            }


            return courseCategory;
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

        // Add a course to degree tracker
        public static void addToDegree(String course_id) {
            ContentValues values = new ContentValues();
            values.put(KEY_COURSE_ID, course_id);
            db.insert(TABLE_DEGREE, null, values);
        }

        // Remove a course from degree tracker
        public static void removeFromDegree(String course_id) {
            db.delete(TABLE_DEGREE, String.format("%s = ?", KEY_COURSE_ID), new String[] {course_id}) ;
        }

        @SuppressLint("DefaultLocale")
        public static void degreeCourseStatusUpdate(String course_id, int status){
            String UPDATE_QUERY = String.format("UPDATE %s SET %s = %d ", TABLE_DEGREE, KEY_DEGREE_STATUS, status);
            UPDATE_QUERY = UPDATE_QUERY.concat(String.format("WHERE %s = '%s'", KEY_COURSE_ID, course_id));
            db.execSQL(UPDATE_QUERY);
        }
    }

}