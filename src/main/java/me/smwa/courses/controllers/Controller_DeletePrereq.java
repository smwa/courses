package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractGetController;
import me.smwa.courses.entities.Prerequisite;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;

import java.util.Map;

public class Controller_DeletePrereq extends AbstractGetController {

    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {
        Integer returnId = Integer.parseInt(params.get("courseid"));
        Integer prereqId = Integer.parseInt(params.get("id"));

        Prerequisite prereq = Prerequisite.find(prereqId);


        if (!user.permission_edit_classes) {
            throw new ControllerException("You do not have access to delete prerequisites");
        }

        prereq.remove();

        return v().redirect("course?id=" + returnId);
    }

}
