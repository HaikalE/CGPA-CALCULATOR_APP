package com.example.projectkelbaru1;

public class Course {
    private String courseCode;
    private String name;
    private int credit;
    private int semester;

    public Course(String courseCode, String name, int credit, int semester) {
        this.courseCode = courseCode;
        this.name = name;
        this.credit = credit;
        this.semester = semester;
    }

    // Getters and setters for the class variables
    // ...

    // Override toString() method to provide a readable representation of the object
    @Override
    public String toString() {
        return "Course{" +
                "courseCode='" + courseCode + '\'' +
                ", name='" + name + '\'' +
                ", credit=" + credit +
                ", semester=" + semester +
                '}';
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public int getCredit() {
        return credit;
    }
}
