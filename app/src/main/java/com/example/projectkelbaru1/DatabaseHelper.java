package com.example.projectkelbaru1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GPA_dbs";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the "courses" table
        String createTableQuery = "CREATE TABLE courses (" +
                "course_code TEXT PRIMARY KEY," +
                "name TEXT," +
                "credit INTEGER," +
                "semester INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table if needed and recreate it
        String dropTableQuery = "DROP TABLE IF EXISTS courses";
        db.execSQL(dropTableQuery);
        onCreate(db);
    }
    public void updateCourse(String courseCode, String courseName, int creditHours, int semester) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", courseName);
        values.put("credit", creditHours);
        values.put("semester", semester);

        String selection = "course_code = ?";
        String[] selectionArgs = {courseCode};

        db.update("courses", values, selection, selectionArgs);
        db.close();
    }
    public void insertCourse(String courseCode, String courseName, int creditHours, int semester) {
        SQLiteDatabase db = getWritableDatabase();

        String insertQuery = "INSERT INTO courses (course_code, name, credit, semester) " +
                "VALUES ('" + courseCode + "', '" + courseName + "', " + creditHours + ", '" + semester + "')";

        db.execSQL(insertQuery);
        db.close();
    }
    public List<Course> getCoursesBySemester(int semesterType) {
        SQLiteDatabase db = getReadableDatabase();
        List<Course> courses = new ArrayList<>();

        String selectQuery = "SELECT course_code, name, credit, semester " +
                "FROM courses " +
                "WHERE semester % 2 = " + semesterType;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String  courseCode = cursor.getString(cursor.getColumnIndex("course_code"));
                String courseName = cursor.getString(cursor.getColumnIndex("name"));
                int creditHours = cursor.getInt(cursor.getColumnIndex("credit"));
                int semester = cursor.getInt(cursor.getColumnIndex("semester"));

                Course course = new Course(courseCode, courseName, creditHours, semester);
                courses.add(course);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return courses;
    }
    public Course getCourseDetailsByCode(String courseCode) {
        SQLiteDatabase db = getReadableDatabase();
        Course course = null;

        String selectQuery = "SELECT name, credit, semester FROM courses WHERE course_code = ?";
        String[] selectionArgs = {courseCode};

        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor.moveToFirst()) {
            String courseName = cursor.getString(cursor.getColumnIndex("name"));
            int creditHours = cursor.getInt(cursor.getColumnIndex("credit"));
            int semester = cursor.getInt(cursor.getColumnIndex("semester"));
            course = new Course(courseCode, courseName, creditHours,semester);
        }

        cursor.close();
        db.close();

        return course;
    }
    public void deleteCourseByCode(String courseCode) {
        SQLiteDatabase db = getWritableDatabase();

        String selection = "course_code = ?";
        String[] selectionArgs = {courseCode};

        db.delete("courses", selection, selectionArgs);
        db.close();
    }

}
