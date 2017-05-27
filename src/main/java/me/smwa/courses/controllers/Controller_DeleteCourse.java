package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.entities.Course;
import me.smwa.courses.entities.Prerequisite;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;

import java.util.ArrayList;
import java.util.Map;

public class Controller_DeleteCourse extends AbstractPostController {

    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {
        Integer courseId = Integer.parseInt(params.get("id"));
        Course c = Course.find(courseId);

        if (!user.permission_edit_classes) {
            throw new ControllerException("You do not have access to delete courses");
        }

        return v().page("Delete Course",
                v().navbar(user)
                        + v().column(
                        v().subheader("Are you sure you want to delete this course?")
                        + v().subheader(c.name)
                        + v().inputHidden("id", c.id.toString())
                        + v().submitButton("Yes, I'm sure")
                        + v().cancelButton("course?id="+courseId, "Back to course")
                )
            );
    }

    @Override
    public String post(Map<String, String> params, User user) throws ControllerException {
        if (!user.permission_edit_classes) {
            throw new ControllerException("You are not allowed to delete courses");
        }

        Integer courseId = Integer.parseInt(params.get("id"));
        Course c = Course.find(courseId);

        ArrayList<Prerequisite> prereqs = c.getPrerequisites();
        ArrayList<Prerequisite> dependents = c.getRequiredFor();

        for (Prerequisite prereq : prereqs) {
            prereq.remove();
        }

        for (Prerequisite dependent : dependents) {
            dependent.remove();
        }

        c.remove();

        return "courses";
    }

}
