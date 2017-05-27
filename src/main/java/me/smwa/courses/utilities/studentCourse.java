package me.smwa.courses.utilities;

/**
 * Created by Michael on 2017-04-23.
 */
public class studentCourse {
    private String name;
    private String crn;

    public String getCrn() {
        return crn;
    }

    private boolean isFutureCourse;
    private String grade;

    public String getName() {
        return name;
    }

    public boolean isFutureCourse() {
        return isFutureCourse;
    }

    public String getGrade() {
        return grade;
    }

    public studentCourse(String name, String grade, String crn) {
        this.name = name;
        this.grade = grade;
        this.isFutureCourse = false;
        this.crn = crn;
    }

    public studentCourse(String name, String crn) {
        this.name = name;
        this.grade = null;
        this.isFutureCourse = true;
        this.crn = crn;
    }

    public boolean hasMinimumGrade(String minimumGrade) {
        return (gradeToNumber(this.grade) >= gradeToNumber(minimumGrade));
    }

    public static int gradeToNumber(String grade) {
        grade = grade.toUpperCase().trim();
        if (grade == null) return 0;
        if (grade.equals("W")) return 1;
        if (grade.equals("F!")) return 2;
        if (grade.equals("F")) return 3;
        if (grade.equals("D")) return 4;
        if (grade.equals("C")) return 5;
        if (grade.equals("B")) return 6;
        if (grade.equals("A")) return 7;
        return 0;
    }
}
