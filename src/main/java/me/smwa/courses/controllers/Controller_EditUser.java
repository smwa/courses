package me.smwa.courses.controllers;

import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.entities.User;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.exceptions.SetUserException;

import java.util.Map;

/**
 * Created by Justin on 4/13/2017.
 */
public class Controller_EditUser extends AbstractPostController {
    @Override
    public String get(Map<String, String> params, User user) throws ControllerException, SetUserException {
        if (!user.permission_edit_users) {
            throw new ControllerException("You do not have permission to edit users");
        }
        Integer userId = Integer.parseInt(params.get("id"));
        User u = User.find(userId);

        String permissionCheckBoxes = "";
        if (u.id > 1) {
            permissionCheckBoxes = v().checkBox("permission_edit_classes", u.permission_edit_classes, "Permission to edit courses")
                    + v().checkBox("permission_edit_users", u.permission_edit_users, "Permission to edit users")
                    + v().checkBox("permission_edit_recipients", u.permission_edit_recipients, "Permission to edit recipients")
                    + v().checkBox("permission_process_report", u.permission_process_report, "Permission to run the report")
                    + v().horizontalLine();
        }

        return v().page("Edit User",
                v().navbar(user)
                        + v().column(
                        v().header("Editing User: " + u.email)
                                + v().horizontalLine()
                                + v().inputHidden("id", u.id.toString())

                                + permissionCheckBoxes

                                + v().checkBox("reset_password", false, "Reset Password <small>- Enter an empty password to log in after resetting your password, or creating a new account.</small>")
                                + v().horizontalLine()

                                + v().submitButton("Save")
                ));
    }

    @Override
    public String post(Map<String, String> params, User user) throws ControllerException, SetUserException {
        if (!user.permission_edit_users) {
            throw new ControllerException("You do not have permission to edit users");
        }

        User editingUser = User.find(Integer.parseInt(params.get("id")));
        if (editingUser == null) {
            throw new ControllerException("Invalid user");
        }


        editingUser.permission_edit_classes = parsePermission( params.get("permission_edit_classes") );
        editingUser.permission_edit_users = parsePermission( params.get("permission_edit_users") );
        editingUser.permission_edit_recipients = parsePermission( params.get("permission_edit_recipients") );
        editingUser.permission_process_report = parsePermission( params.get("permission_process_report") );

        if (parsePermission(params.get("reset_password"))) {
            editingUser.is_temporary_password = true;
        }

        editingUser.save();

        return "users";
    }

    private boolean parsePermission(String perm) {
        if (perm == null) return false;
        if (perm.equals("0") || perm.equals("")) return false;
        return true;
    }
}
