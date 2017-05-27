package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractGetController;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;

import java.util.Map;

public class Controller_Menu extends AbstractGetController {
    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {
        String listItems = "";

        if (user.permission_edit_classes) {
            listItems += v().listItemLink("courses", "Edit Courses");
        }
        else {
            listItems += v().listItemLink("courses", "View Courses");
        }

        if (user.permission_edit_recipients) {
            listItems += v().listItemLink("receivers", "Edit Email Recipients");
        }

        if (user.permission_edit_users) {
            listItems += v().listItemLink("users", "Edit Users");
        }

        if (user.permission_process_report) {
            listItems += v().listItemLink("report1", "Upload Report");
        }

        listItems += v().listItemLink("logout", "Logout", "warning");

        return v().page("Menu",
                v().navbar(user)
                        + v().column(
                        v().header("Menu")
                        +
                        v().list(
                            listItems
                        )
                )
        );
    }
}
