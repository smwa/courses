package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.entities.Course;
import me.smwa.courses.entities.Prerequisite;
import me.smwa.courses.entities.Receiver;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.utilities.Mailer;
import me.smwa.courses.utilities.text_report_holder;
import me.smwa.courses.utilities.Student;
import me.smwa.courses.utilities.students_holder;
import me.smwa.courses.utilities.studentCourse;

import java.util.ArrayList;
import java.util.Map;

public class Controller_Report_Two extends AbstractPostController {

    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {
        if (!user.permission_process_report) {
            throw new ControllerException("You do not have access to run the report");
        }

        String report = "";

        buildCache(); // used to load most recent courses and prerequisites into memory

        for (Map.Entry<String, Student> entry : students_holder.getStudents().entrySet()) {
            Student s = entry.getValue();

            for (studentCourse futureCourse : s.getFutureCourses()) {
                boolean ineligible = false;
                for (studentCourse pastCourse : s.getPastCourses()) {
                    String minimumGradeForDependency = getMinimumGradeForCombination(futureCourse.getName(), pastCourse.getName());
                    if (!ineligible && !pastCourse.hasMinimumGrade(minimumGradeForDependency)) {
                        report += s.getStudentID() + ", " + s.getEmail() + ", " + futureCourse.getName() + "." + futureCourse.getCrn() + ", " + getReason(minimumGradeForDependency, pastCourse) + "\n";
                        ineligible = true;
                    }
                }
            }
        }

        destroyCache();

        report = report.trim();

        if (report.isEmpty()) {
            text_report_holder.setReport(report);
            return v().page("Generated Report",
                    v().column(
                            v().header("Course Report")
                                    + v().horizontalLine()
                                    + v().subheader("Meets all course prerequisites")
                    )
            );
        } else {
            report = "BannerID, Email, Ineligible For Class, Reason\n" + report;
            text_report_holder.setReport(report);
            return v().page("Generated Report",
                    v().navbar(user)
                            + v().column(
                            v().header("Course Report")
                                    + v().horizontalLine()
                                    + v().pre(report)
                                    + v().submitButton("Send Report To Recipients")
                    )
            );
        }
    }

    @Override
    public String post(Map<String, String> params, User user) throws ControllerException {
        if (!user.permission_process_report) {
            throw new ControllerException("You do not have access to run the report");
        }
        String rpt = text_report_holder.getReport();
        if (rpt.equals("")) {
            throw new ControllerException("The report is empty");
        }

        //Retrieve mailing list and place into comma delimited String
        String emailList = "";
        //Retrieve a list of all email receivers
        ArrayList<Receiver> receivers = Receiver.findAll();

        for (Receiver mailer: receivers) {
            emailList += mailer.email + ",";
        }

        if(!Mailer.send(emailList,"Course Report","Please see attachment.","report.csv",rpt)) {
            throw new ControllerException("Failed to send email");
        }

        text_report_holder.setReport("");

        return "report3";
    }

    private ArrayList<Course> courses;
    private ArrayList<Prerequisite> prerequisites;

    private void buildCache() {
        courses = Course.findAll();
        prerequisites = Prerequisite.findAll();
    }

    private void destroyCache() {
        courses = null;
        prerequisites = null;
    }

    private String getMinimumGradeForCombination(String futureCourseName, String pastCourseName) {
        futureCourseName = futureCourseName.toUpperCase();
        pastCourseName = pastCourseName.toUpperCase();

        int pastCourseId = 0;
        int futureCourseId = 0;

        for (Course course : courses) {
            if (course.name.toUpperCase().equals(futureCourseName)) {
                futureCourseId = course.id;
            }
            else if (course.name.toUpperCase().equals(pastCourseName)) {
                pastCourseId = course.id;
            }
        }
        for (Prerequisite prerequisite : prerequisites) {
            if (prerequisite.course_id == futureCourseId && prerequisite.dependency == pastCourseId) {
                return prerequisite.minimum_grade;
            }
        }
        return "";
    }

    private String getReason(String minimumGrade, studentCourse pastCourse) {
        if (pastCourse.getGrade() == null || pastCourse.getGrade().equals("")) {
            return "Prerequisite "+pastCourse.getName()+" for course does not have a grade.";
        }
        return "Requires a grade of "
                + minimumGrade
                + " or higher in the course: "
                + pastCourse.getName()
                + ". A grade of "
                + pastCourse.getGrade()
                + " was earned.";
    }
}
