package me.smwa.courses.utilities;

import java.util.ArrayList;

public class Student {
    private ArrayList<studentCourse> pastCourses = new ArrayList<>();
    private ArrayList<studentCourse> futureCourses = new ArrayList<>();

    private String StudentID;
    private String Email;

    public ArrayList<studentCourse> getPastCourses() {
        return pastCourses;
    }

    public ArrayList<studentCourse> getFutureCourses() {
        return futureCourses;
    }

    public String getStudentID() {
        return StudentID;
    }

    public String getEmail() {
        return Email;
    }

    public Student(String StudentID, String email) {
        this.StudentID = StudentID;
        Email = email;
    }

    public void addPastCourse(studentCourse c) {
        pastCourses.add(c);
    }

    public void addFutureCourse(studentCourse c) {
        futureCourses.add(c);
    }
}
