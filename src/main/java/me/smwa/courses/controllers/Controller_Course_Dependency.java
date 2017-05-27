package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.entities.Course;
import me.smwa.courses.entities.Prerequisite;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.exceptions.SetUserException;

import java.util.ArrayList;
import java.util.Map;

public class Controller_Course_Dependency extends AbstractPostController {

    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {
        Integer courseId = Integer.parseInt(params.get("id"));
        Course course = Course.find(courseId);

        if (course == null) {
            throw new ControllerException("Invalid course");
        }

        ArrayList<Prerequisite> currentPrereqs = course.getPrerequisites();

        String html = v().subheader("Minimum Grade")
                + v().radioButton("A", "grade", "A")
                + v().radioButton("B", "grade", "B")
                + v().radioButtonSelected("C", "grade", "C")
                + v().radioButton("D", "grade", "D");

        String coursesList = "";

        ArrayList<Course> courses = Course.findAll();

        for (Course c : courses) {
            boolean exists = false;
            for (Prerequisite currentPrereq : currentPrereqs) {
                if (currentPrereq.dependency.intValue() == c.id.intValue()) {
                    exists = true;
                }
            }

            if (c.id.intValue() == course.id.intValue() || exists) {
                continue;
            }
            coursesList += v().listItemLink("#" + c.id, c.name);
        }

        if (coursesList.equals("")) {
            coursesList += v().listItem("There are no courses available to be added", "info");
        }

        String javascript = "<script> window.onhashchange = function() {\n" +
                "var hash = location.hash.substring(1);\n" +
                "document.getElementById('dependency').value = hash;\n" +
                "document.forms[0].submit();\n" +
                "} </script>";

        return v().page("Add Dependency to " + course.name,
                v().navbar(user)
                        + v().column( 4,
                v().header("Add prerequisite for " + course.name)
                        + html
                        + javascript
                        + v().horizontalLine()
                        + v().inputHidden("id", course.id.toString())
                        + v().inputHidden("dependency", "0")
                        + v().list(
                            coursesList
                        )
                        + v().cancelButton("course?id="+courseId, "Back"),
                        "",
                        "",
                        4
                )
            );
    }

    @Override
    public String post(Map<String, String> params, User user) throws ControllerException, SetUserException {
        if (!user.permission_edit_classes) {
            throw new ControllerException("You can not add dependencies");
        }
        Integer courseId = Integer.parseInt(params.get("id"));
        Integer dependencyId = Integer.parseInt(params.get("dependency"));
        Course course = Course.find(courseId);
        Course requires = Course.find(dependencyId);

        if (requires == null || course == null) {
            throw new ControllerException("Dependency was not selected.");
        }

        String grade = params.get("grade");

        Prerequisite p = new Prerequisite();
        p.minimum_grade = grade;
        p.course_id = course.id;
        p.dependency = requires.id;
        p.save();

        return "course?id=" + course.id;
    }
}
