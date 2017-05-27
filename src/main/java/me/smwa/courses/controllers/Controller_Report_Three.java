package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.entities.Receiver;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import java.util.Map;

public class Controller_Report_Three extends AbstractPostController {

    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {
        if (!user.permission_process_report) {
            throw new ControllerException("You do not have access to run the report");
        }
        String list = "";

        for (Receiver r : Receiver.findAll()) {
            list += v().listItem(r.email);
        }

        return v().page("Report Complete",
                v().navbar(user)
                        + v().column(
                        v().header("Report Complete")
                        + v().subheader("An email has been sent to the following addresses with the" +
                                " report from the previous page")
                        +v().list(
                                list
                        )
                        +v().submitButton("Return")
                )
        );
    }

    @Override
    public String post(Map<String, String> params, User user) throws ControllerException {
        return "menu";
    }
}
