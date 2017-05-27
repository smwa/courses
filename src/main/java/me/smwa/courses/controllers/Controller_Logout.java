package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractGetController;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.exceptions.SetUserException;

import java.util.Map;

public class Controller_Logout extends AbstractGetController {

    @Override
    public String get(Map<String, String> params, User user) throws ControllerException, SetUserException {
        throw new SetUserException(0);
    }
}
