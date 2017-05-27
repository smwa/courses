package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.entities.Receiver;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.exceptions.SetUserException;

import java.util.ArrayList;
import java.util.Map;


public class Controller_Receivers extends AbstractPostController {
    @Override
    public String get(Map<String, String> params, User user) throws ControllerException, SetUserException {

        String emailList = "";

        ArrayList<Receiver> receivers = Receiver.findAll();

        //emailList += v().listItem("email1@okstate.edu");
        //emailList += v().listItem("email2@okstate.edu");
        //emailList += v().listItem("email3@okstate.edu");

        if (receivers.size() < 1) {
            emailList += v().listItem("There are no emails.", "info");
        }

        for (Receiver r : receivers) {
            emailList += v().listItem(r.email + v().linkRight("delete_receivers?id=" + r.id, "remove"));
        }


        String textEmail = v().input("email", "", "New Recipient");

        String newReceiverButton;

        if (user.permission_edit_recipients) {
            newReceiverButton = v().submitButton("Add Email");
        } else {
            throw new ControllerException("You do not have permissionto edit email recipients.");
        }

        return v().page("Receivers",
                v().navbar(user)
                        + v().column(
                        v().header("Email Recipients")
                                + textEmail
                                + newReceiverButton
                                + v().horizontalLine()
                                + v().list(
                                emailList
                        )
                )
        );
    }

    @Override
    public String post(Map<String, String> params, User user) throws ControllerException, SetUserException {
        String email = params.get("email");


        //Check to make sure something was entered into the email text field
        if ((email == null) || (email.isEmpty()) || (email.length() <= 0)) {
            throw new ControllerException("Please type a valid email address.");
        }


        //Create an email object and commit this to the database
        Receiver r = new Receiver();
        r.setEmail(email);
        r.save(); //Update the Receivers table

        return "receivers"; //Return back to the receivers page
    }
}

