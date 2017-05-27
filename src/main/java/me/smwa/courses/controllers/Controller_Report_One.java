package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.utilities.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rcorrig on 3/30/2017.
 * Loads excel file from user and puts it into the student's data structure
 */
public class Controller_Report_One extends AbstractPostController {

    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {
        if (!user.permission_process_report) {
            throw new ControllerException("You do not have access to run the report");
        }
        return v().page("Report",
                v().navbar(user)
                        + v().column(
                        v().header("Generate Report")
                        + v().inputfile("upload","Upload Excel File")
                        + v().submitButton("Process Report")
                )
        );
    }

    @Override
    public String post(Map<String, String> params, User user) throws ControllerException {
        if (!user.permission_process_report) {
            throw new ControllerException("You do not have access to run the report");
        }

        HashMap<String, Student> students = new HashMap<>();

        String term,
                subject,
                course,
                crn,
                banner_id,
                email,
                grade,
                courseName;
        boolean isFutureClass;

        List<List<String>> spreadsheet;

        try {
            spreadsheet = XLSUtils.parse(params.get("upload").getBytes("ISO-8859-1"));
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        //Define a hashmap and fill it with 1's and null's. If a term has a grade, then it has to be in the past
        //Otherwise we don't know
        HashMap<String, Integer> termIsInPast = new HashMap<>();
        for (int i = 0; i < spreadsheet.size(); i++) {
            if (i < 1) continue; //Skip headers
            List<String> line = spreadsheet.get(i);

            term = line.get(0).trim();
            grade = line.get(6).trim();

            if (!grade.isEmpty()) {
                //There is a grade
                termIsInPast.put(term, 1);
            }
        }

        for (int i = 0; i < spreadsheet.size(); i++) {
            if (i < 1) continue; //Skip headers
            List<String> line = spreadsheet.get(i);

            term = line.get(0).trim();
            subject = line.get(1).trim();
            course = line.get(2).trim();
            crn = line.get(3).trim();
            banner_id = line.get(4).trim();
            email = line.get(5).trim();
            grade = line.get(6).trim();

            courseName = subject +  "-" + course;
            //We assume if a can't prove a term is in the past, then it's in the future
            isFutureClass = (termIsInPast.get(term) == null);

            if (!students.containsKey(banner_id)) {
                students.put(banner_id, new Student(banner_id, email));
            }
            if (isFutureClass) {
                //future course
                students.get(banner_id).addFutureCourse(new studentCourse(courseName,crn));
            }
            else {
                //current course
                students.get(banner_id).addPastCourse(new studentCourse(courseName, grade, crn));
            }
        }
        students_holder.setStudents(students);
        return "report2";
    }
}
