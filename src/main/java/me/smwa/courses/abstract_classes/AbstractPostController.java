package me.smwa.courses.abstract_classes;

import java.util.Map;

import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.exceptions.SetUserException;

abstract public class AbstractPostController extends AbstractGetController {
    //RETURNS Redirect URL
    abstract public String post(Map<String, String> params, User user) throws ControllerException, SetUserException;
}
