package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractGetController;
import me.smwa.courses.abstract_classes.InterfaceNoSessionRequired;
import me.smwa.courses.entities.User;
import me.smwa.courses.vb;

import java.util.Map;

public class Controller_Error extends AbstractGetController implements InterfaceNoSessionRequired {
    @Override
    public String get(Map<String, String> params, User user) {
        vb v = getViewBuilder();
        return v.page("Error",
                v().navbar(user)
                        + v.column(
                    v.header("Something went wrong!!")
                            + v.paragraph(params.get("message"))
                            + v.cancelButton(null, "Go Back")
                )
        );
    }
}
