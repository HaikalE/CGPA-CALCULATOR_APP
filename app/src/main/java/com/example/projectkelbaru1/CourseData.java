package com.example.projectkelbaru1;

public class CourseData {
    private int pkey;
    private int sid;
    private int semester;
    private String courseCode;
    private int gradeCode;

    public CourseData(int pkey, int sid, int semester, String courseCode, int gradeCode) {
        this.pkey = pkey;
        this.sid = sid;
        this.semester = semester;
        this.courseCode = courseCode;
        this.gradeCode = gradeCode;
    }

    public int getPkey() {
        return pkey;
    }

    public void setPkey(int pkey) {
        this.pkey = pkey;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(int gradeCode) {
        this.gradeCode = gradeCode;
    }
}
