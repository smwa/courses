package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import java.util.Map;

/**
 * Created by Justin on 4/13/2017.
 */
public class Controller_DeleteUser  extends AbstractPostController {

    @Override
    public String get(Map<String, String> params, User user) throws ControllerException {
        Integer userId = Integer.parseInt(params.get("id"));
        User u = User.find(userId);

        if (!user.permission_edit_users) {
            throw new ControllerException("You do not have permission to delete users");
        }

        return v().page("Delete User",
                v().navbar(user)
                        + v().column(
                        v().subheader("Are you sure you want to delete this user?")
                                + v().subheader(u.email)
                                + v().inputHidden("id", u.id.toString())
                                + v().submitButton("Yes, I'm sure")
                                + v().cancelButton("users", "Back to users")
                )
        );
    }
    @Override
    public String post(Map<String, String> params, User user) throws ControllerException {
        if (!user.permission_edit_users) {
            throw new ControllerException("You are not allowed to delete users");
        }

        Integer userId = Integer.parseInt(params.get("id"));
        User u = User.find(userId);



        u.remove();

        return "users";
    }
}
