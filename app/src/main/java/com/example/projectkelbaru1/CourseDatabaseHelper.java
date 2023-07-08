package com.example.projectkelbaru1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CourseDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GPA_dbs";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "registered_courses";
    private static final String COLUMN_ID = "pkey";
    private static final String COLUMN_SID = "sid";
    private static final String COLUMN_SEM = "sem";
    private static final String COLUMN_COURSE_CODE = "course_code";
    private static final String COLUMN_GRADE_CODE = "grade_code";

    public CourseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SID + " INTEGER, "
                + COLUMN_SEM + " INTEGER, "
                + COLUMN_COURSE_CODE + " TEXT, "
                + COLUMN_GRADE_CODE + " INTEGER, "
                + "FOREIGN KEY (" + COLUMN_SID + ") REFERENCES user(sid), "
                + "FOREIGN KEY (" + COLUMN_COURSE_CODE + ") REFERENCES courses(course_code), "
                + "FOREIGN KEY (" + COLUMN_GRADE_CODE + ") REFERENCES grades(grade_code))";
        db.execSQL(createTableQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void deleteBySidAndCourseCode(int sid, String courseCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_SID + " = ? AND " + COLUMN_COURSE_CODE + " = ?";
        String[] whereArgs = {String.valueOf(sid), courseCode};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }
    public void insertCourse(int sid, int sem, String courseCode, Integer gradeCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SID, sid);
        values.put(COLUMN_SEM, sem);
        values.put(COLUMN_COURSE_CODE, courseCode);
        values.put(COLUMN_GRADE_CODE, gradeCode);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateGrade(int sid, String courseCode, Integer gradeCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GRADE_CODE, gradeCode);
        String whereClause = COLUMN_SID + " = ? AND " + COLUMN_COURSE_CODE + " = ?";
        String[] whereArgs = { String.valueOf(sid), courseCode };
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public boolean selectByCourseCodeAndSid(String courseCode, int sid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID};
        String selection = COLUMN_COURSE_CODE + " = ? AND " + COLUMN_SID + " = ?";
        String[] selectionArgs = {courseCode, String.valueOf(sid)};
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        boolean dataExists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return dataExists;
    }

    public List<CourseData> selectBySemesterAndSid(int semester, int sid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_SID, COLUMN_SEM, COLUMN_COURSE_CODE, COLUMN_GRADE_CODE};
        String selection = COLUMN_SEM + " = ? AND " + COLUMN_SID + " = ?";
        String[] selectionArgs = {String.valueOf(semester), String.valueOf(sid)};
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        List<CourseData> courseList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int pkey = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            int fetchedSid = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SID));
            int fetchedSemester = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SEM));
            String courseCode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_CODE));
            int gradeCode = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GRADE_CODE));

            CourseData courseData = new CourseData(pkey, fetchedSid, fetchedSemester, courseCode, gradeCode);
            courseList.add(courseData);
        }

        cursor.close();
        db.close();

        return courseList;
    }


    public Integer getGradeCode(int pkey) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = { COLUMN_GRADE_CODE };
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(pkey) };
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Integer gradeCode = null;
        if (cursor.moveToFirst()) {
            gradeCode = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GRADE_CODE));
        }

        cursor.close();
        db.close();
        return gradeCode;
    }
}
