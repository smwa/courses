package me.smwa.courses.controllers;

import me.smwa.courses.Main;
import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.abstract_classes.InterfaceNoSessionRequired;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.exceptions.SetUserException;
import me.smwa.courses.vb;

import java.util.Map;

public class Controller_Login extends AbstractPostController implements InterfaceNoSessionRequired {

    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {
        vb v = getViewBuilder();

        if (user != null) {
            return v().redirect(Main.ROUTE_MENU);
        }

        return v.page("Login",
                v.navbar(user)
                + v.column(
                        v.header("Login to continue")
                        + v.input("email", "", "E-mail Address")
                        + v.password("password", "", "Password")
                        + v.submitButton("Log In")
                )
        );
    }

    @Override
    public String post(Map<String, String> params, User user) throws ControllerException, SetUserException {
        String email = params.get("email");
        String password = params.get("password");

        User u = User.findByEmail(email);

        if (u != null && u.comparePassword(password)) {
            throw new SetUserException(u.id);
        }

        throw new ControllerException("Email or password is incorrect");
    }
}
