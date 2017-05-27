package me.smwa.courses.utilities;

import java.util.HashMap;

public class students_holder {
    private static HashMap<String, Student> students = new HashMap<>();

    public static HashMap<String, Student> getStudents() {
        return students;
    }

    public static void setStudents(HashMap<String, Student> l) {
        students = l;
    }
}
