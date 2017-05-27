package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.abstract_classes.InterfaceNoSessionRequired;
import me.smwa.courses.entities.*;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.vb;

import java.util.Map;

public class Controller_Test extends AbstractPostController implements InterfaceNoSessionRequired {

    public String get(Map<String, String> params, User user) throws ControllerException
    {
        vb v = getViewBuilder();
        return v.page("Testing Page",
            v.column(
                v.header("Welcome!")
                + v.paragraph("This is just another test")
                + v.input("test", "", "Some value")
                + v.saveButton()
            )
        );
    }

    public String post(Map<String, String> params, User user) throws ControllerException
    {
        System.out.println("Redirect to /");
        return "/";
    }

}
