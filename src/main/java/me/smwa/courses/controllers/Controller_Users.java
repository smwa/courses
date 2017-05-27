package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.exceptions.SetUserException;

import java.util.ArrayList;
import java.util.Map;

public class Controller_Users extends AbstractPostController {

    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {

        if (!user.permission_edit_users) {
            //Throw Exception here
            throw new ControllerException("You do not have permission to edit users");
        }
        String userList = "";

        ArrayList<User> users = User.findAll();

        if (users.size() < 1) {
            userList += v().listItem("There are no users.", "info");
        }

        for (User u : users) {
            if (u.id == 1) {
                userList += v().listItem(v().link("edit_user?id=" + u.id, u.email));
            } else {
                userList += v().listItem(v().link("edit_user?id=" + u.id, u.email) + v().linkRight("delete_user?id=" + u.id, "Delete User"));
            }
        }

        String newReceiverButton;

        String textEmail = v().input("email","","Enter email address");
        newReceiverButton = v().submitButton("Add User");

        return v().page("Users",
                v().navbar(user)
                        + v().column(
                        v().header("All users")
                                + textEmail
                                + newReceiverButton
                                + v().horizontalLine()
                                + v().list(
                                userList
                        )
                )
        );
    }

    @Override
    public String post(Map<String, String> params, User user) throws ControllerException, SetUserException {
        String email = params.get("email");

        //Check to make sure something was entered into the email text field
        if ((email == null) || (email.isEmpty()) || (email.length() <=0)) {
            throw new ControllerException("Please type an email address.");
        }
        //Check email text exists and the post data %40 has an @ character included in email address
        User u = new User();
        u.setEmail(email);
        u.is_temporary_password = true;

        u.save();
        return "edit_user?id="+u.id;
    }
}