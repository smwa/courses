package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractGetController;
import me.smwa.courses.entities.Receiver;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.exceptions.SetUserException;

import java.util.Map;

/**
 * Created by rcorrig on 4/6/2017.
 */
public class Controller_DeleteReceiver extends AbstractGetController {
    @Override
    public String get(Map<String, String> params, User user) throws ControllerException, SetUserException {
        Integer receiverId = Integer.parseInt(params.get("id"));

        Receiver receiver = Receiver.find(receiverId);
        receiver.remove();
        return v().redirect("receivers");
    }
}
