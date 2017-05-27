package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.entities.Course;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;

import java.util.ArrayList;
import java.util.Map;

public class Controller_Courses extends AbstractPostController {

    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {

        String coursesList = "";

        ArrayList<Course> courses = Course.findAll();

        if (courses.size() < 1) {
            coursesList += v().listItem("There are no courses.", "info");
        }

        String descriptionAddition;

        for (Course c : courses) {
            if (c.name.equals("New Course")) {
                c.remove();
                continue;
            }
            if(c.description.length() > 161) {
                descriptionAddition = c.description.substring(0, 160) + "...";
            }
            else {
                descriptionAddition = c.description;
            }
            coursesList += v().listItemLink("course?id=" + c.id.toString(), c.name + " <small style='padding-left: 10px'>" + descriptionAddition + "</small>");
        }

        String newCourseButton = "";
        String courseNote = "";
        if (user.permission_edit_classes) {
            newCourseButton = v().submitButton("Create new course");
            courseNote = "<br><small>When course numbers change between semesters, a new course should be created. Do not edit the old course name. The old course can be deleted after 6 months.</small>";
        }

        return v().page("Courses",
                v().navbar(user)
                + v().column(
                        v().header("All courses")
                        + newCourseButton
                        + courseNote
                        + v().horizontalLine()
                        + v().list(coursesList
                        )
                )
        );

    }

    @Override
    public String post(Map<String, String> params, User user) throws ControllerException {
        Course c = new Course();
        c.name = "New Course";
        c.description = "";
        c.save();

        return "course?id=" + c.id.toString();
    }

}
