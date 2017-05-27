package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;

import java.util.Map;

public class Controller_Password extends AbstractPostController {
    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {
        return v().page("Update Password",
                v().navbar(user)
                        + v().column(
                    v().header("Update Password")
                    + v().password("password", "", "Password")
                    + v().password("password_confirm", "", "Confirm Password")
                    + v().submitButton("Update")
                )
        );
    }

    @Override
    public String post(Map<String, String> params, User user) throws ControllerException {
        String password = params.get("password");
        String password_confirm = params.get("password_confirm");
        if (!password.equals(password_confirm)) {
            throw new ControllerException("Your passwords do not match.");
        }
        if (password.length() < 7) {
            throw new ControllerException("Your password must be at least 7 characters.");
        }
        try {
            user.setPassword(password);
        }
        catch (Exception e) {
            throw new ControllerException("We were not able to set your password: " + e.getMessage());
        }
        user.is_temporary_password = false;
        user.save();
        return "menu";
    }
}
