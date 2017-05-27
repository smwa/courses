package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.entities.Course;
import me.smwa.courses.entities.Prerequisite;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;

import java.util.ArrayList;
import java.util.Map;

public class Controller_Course extends AbstractPostController {

    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {
        Integer courseId = Integer.parseInt(params.get("id"));
        Course c = Course.find(courseId);

        if (!user.permission_edit_classes) {
            return v().page("View course: " + c.name,
                    v().navbar(user)
                            + v().column(10,
                            v().link("courses", "Back to courses")
                                    + v().header(c.name)
                                    + v().subheader(c.description)
                                    + v().cancelButton("courses", "Back"),
                            "", "", 1)
                            + v().column(6, v().header("Required for " + c.name) + getDependsOn(c, user), "", "", 0)
                            + v().column(6, v().header("Requires completion of " + c.name) + getRequiredFor(c, user), "", "", 0)
            );
        }

        if (c.name.equals("New Course")) {
            c.name = "";
        }

        return v().page("Edit course: " + c.name,
                v().navbar(user)
                        + v().column(10,
                    v().input("name", c.name, "Course Title, i.e. MSIS-5053")
                + v().textarea("description", c.description, "Course Description")
                + v().inputHidden("id", c.id.toString())
                + v().saveButton()
                + v().cancelButton("courses", "Back"),
            "", "", 1)
            + v().column(7,
                    v().subheader("Required for " + c.name)
                            + getDependsOn(c, user)
                            + v().subheader(v().link("add_dependency?id=" + c.id, "Add prerequisite")),
            "", "", 0)
            + v().column(5,
                    v().subheader("Requires completion of " + c.name)
                            + getRequiredFor(c, user),
            "", "", 0)
            + v().column(v().link("delete?id=" + c.id, "Delete this course"))
        );
    }

    private String getDependsOn(Course c, User user) {
        String t = "";

        ArrayList<Prerequisite> prereqs = c.getPrerequisites();

        if (prereqs.size() < 1) {
            t += v().listItem("There are no prerequisites.", "info");
        }

        for (Prerequisite prereq : prereqs) {
            Course course = Course.find(prereq.dependency);

            String deleteCourse = "";
            if (user.permission_edit_classes) {
                deleteCourse = v().linkRight("delete_prerequisite?courseid=" + c.id + "&id=" + prereq.id, "remove");
            }
            t += v().listItem(
                    v().link(
                            "course?id=" + course.id.toString(),
                            course.name + " <small>Requires a grade of " + prereq.minimum_grade + "</small>"
                    )
                    + deleteCourse
            );
        }

        return v().list(t);
    }

    private String getRequiredFor(Course c, User user) {
        String t = "";

        ArrayList<Prerequisite> prereqs = c.getRequiredFor();

        if (prereqs.size() < 1) {
            t += v().listItem("This course is not required for any other courses.", "info");
        }

        for (Prerequisite prereq : prereqs) {
            String deleteCourse = "";
            if (user.permission_edit_classes) {
                deleteCourse = v().linkRight("delete_prerequisite?courseid=" + c.id + "&id=" + prereq.id, "remove");
            }
            Course course = Course.find(prereq.course_id);
            t += v().listItem(
                    v().link(
                            "course?id=" + course.id.toString(),
                            course.name + " <small>Requires a grade of " + prereq.minimum_grade + "</small>"
                    )
                    + deleteCourse
            );
        }

        return v().list(t);
    }

    @Override
    public String post(Map<String, String> params, User user) throws ControllerException {
        if (!user.permission_edit_classes) {
            throw new ControllerException("You are not allowed to edit courses");
        }

        Integer courseId = Integer.parseInt(params.get("id"));
        String name = params.get("name");
        String description = params.get("description");
        if (name == null || name.equals("")) {
            name = "Unnamed";
        }
        if (description == null) {
            description = "";
        }

        Course c = Course.find(courseId);
        c.name = name;
        c.description = description;
        c.save();
        return "courses";
    }

}
