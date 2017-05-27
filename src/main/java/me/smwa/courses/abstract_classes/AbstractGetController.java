package me.smwa.courses.abstract_classes;

import java.util.Map;

import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.exceptions.SetUserException;

abstract public class AbstractGetController extends AbstractController {
    //RETURNS HTML
    abstract public String get(Map<String, String> params, User user) throws ControllerException, SetUserException;
}
