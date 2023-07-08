package com.example.projectkelbaru1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GradeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GPA_dbs";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "grades";
    private static final String COLUMN_GRADE_CODE = "grade_code";
    private static final String COLUMN_GRADE = "grade";
    private static final String COLUMN_POINT = "point";

    public GradeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_GRADE_CODE + " INTEGER PRIMARY KEY, "
                + COLUMN_GRADE + " TEXT, "
                + COLUMN_POINT + " REAL)";

        db.execSQL(createTableQuery);
        populateTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void populateTable(SQLiteDatabase db) {
        insertGrade(db, 1, "A+", 4.00);
        insertGrade(db, 2, "A", 4.00);
        insertGrade(db, 3, "A-", 3.67);
        insertGrade(db, 4, "B+", 3.33);
        insertGrade(db, 5, "B", 3.00);
        insertGrade(db, 6, "B-", 2.67);
        insertGrade(db, 7, "C+", 2.33);
        insertGrade(db, 8, "C", 2.00);
        insertGrade(db, 9, "C-", 1.67);
        insertGrade(db, 10, "D+", 1.33);
        insertGrade(db, 11, "D", 1.00);
        insertGrade(db, 12, "E", 0.67);
        insertGrade(db, 13, "F", 0.00);
    }

    private void insertGrade(SQLiteDatabase db, int gradeCode, String grade, double point) {
        String insertQuery = "INSERT INTO " + TABLE_NAME + " ("
                + COLUMN_GRADE_CODE + ", "
                + COLUMN_GRADE + ", "
                + COLUMN_POINT + ") VALUES ("
                + gradeCode + ", '"
                + grade + "', "
                + point + ")";

        db.execSQL(insertQuery);
    }
}
