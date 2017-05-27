package me.smwa.courses;

import me.smwa.courses.abstract_classes.AbstractGetController;
import me.smwa.courses.abstract_classes.AbstractPostController;
import me.smwa.courses.abstract_classes.InterfaceNoSessionRequired;
import me.smwa.courses.controllers.*;
import me.smwa.courses.entities.*;
import me.smwa.courses.exceptions.ControllerException;
import me.smwa.courses.exceptions.SetUserException;
import spark.QueryParamsMap;
import spark.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.util.*;

import static spark.Spark.*;

public class Main {
    //Route Constants
    final public static String ROUTE_LOGIN = "login";
    final public static String ROUTE_MENU = "menu";
    final public static String ROUTE_UPDATE_PASSWORD = "password";


    //Static instances of controllers
    static Controller_Error controller_error = new Controller_Error();
    static Controller_Login controller_login = new Controller_Login();
    static Controller_Logout controller_logout = new Controller_Logout();
    static Controller_Menu controller_menu = new Controller_Menu();
    static Controller_Password controller_password = new Controller_Password();
    static Controller_Users controller_users = new Controller_Users();
    static Controller_Courses controller_courses = new Controller_Courses();
    static Controller_Course controller_course = new Controller_Course();
    static Controller_DeleteCourse controller_deleteCourse = new Controller_DeleteCourse();
    static Controller_DeletePrereq controller_deletePrereq = new Controller_DeletePrereq();
    static Controller_DeleteReceiver controller_deleteReceiver = new Controller_DeleteReceiver();
    static Controller_DeleteUser controller_deleteUser = new Controller_DeleteUser();
    static Controller_Receivers controller_recievers = new Controller_Receivers();

    static Controller_Course_Dependency controller_course_dependency = new Controller_Course_Dependency();
    static Controller_EditUser controller_editUser = new Controller_EditUser();
    static Controller_Report_One controller_report_one = new Controller_Report_One();
    static Controller_Report_Two controller_report_two = new Controller_Report_Two();
    static Controller_Report_Three controller_report_three = new Controller_Report_Three();

    public static void main(String[] args) {
        // #Entities
        if (!User.tableExists()) {
            User.createTable();
            Receiver.createTable();
            Course.createTable();
            Prerequisite.createTable();
        }
        else {
            System.out.println("Application has started");
        }

        // #Routes
        staticFiles.location("/public");

        get("/menu", (req, res) ->  processGet(req, res, controller_menu));

        get("/password", (req, res) ->  processGet(req, res, controller_password));
        post("/password", (req, res) ->  processPost(req, res, controller_password));

        get("/users", (req, res) ->  processGet(req, res, controller_users));
        post("/users", (req, res) ->  processPost(req, res, controller_users));

        get("/login", (req, res) ->  processGet(req, res, controller_login));
        post("/login", (req, res) ->  processPost(req, res, controller_login));

        get("/courses", (req, res) ->  processGet(req, res, controller_courses));
        post("/courses", (req, res) ->  processPost(req, res, controller_courses));

        get("/course", (req, res) ->  processGet(req, res, controller_course));
        post("/course", (req, res) ->  processPost(req, res, controller_course));

        get("/add_dependency", (req, res) ->  processGet(req, res, controller_course_dependency));
        post("/add_dependency", (req, res) ->  processPost(req, res, controller_course_dependency));

        get("/receivers", (req, res) ->  processGet(req, res, controller_recievers));
        post("/receivers", (req, res) ->  processPost(req, res, controller_recievers));

        get("/delete", (req, res) ->  processGet(req, res, controller_deleteCourse));
        post("/delete", (req, res) ->  processPost(req, res, controller_deleteCourse));

        get("/delete_prerequisite", (req, res) ->  processGet(req, res, controller_deletePrereq));

        get("/delete_receivers", (req, res) ->  processGet(req, res, controller_deleteReceiver));

        get("/delete_user", (req, res) ->  processGet(req, res, controller_deleteUser));
        post("/delete_user", (req, res) ->  processPost(req, res, controller_deleteUser));

        get("/edit_user", (req, res) ->  processGet(req, res, controller_editUser));
        post("/edit_user", (req, res) ->  processPost(req, res, controller_editUser));

        get("/report1", (req, res) ->  processGet(req, res, controller_report_one));
        post("/report1", (req, res) ->  processPost(req, res, controller_report_one));

        get("/report2", (req, res) ->  processGet(req, res, controller_report_two));
        post("/report2", (req, res) ->  processPost(req, res, controller_report_two));

        get("/report3", (req, res) ->  processGet(req, res, controller_report_three));
        post("/report3", (req, res) ->  processPost(req, res, controller_report_three));



        get("/logout", (req, res) ->  processGet(req, res, controller_logout));

        get("/", (req, res) -> { res.redirect(ROUTE_LOGIN); return "";});
    }

    protected static String processGet(spark.Request req, spark.Response res, AbstractGetController c)
    {
        User u = getUserFromReq(req);
        //Handle users that aren't logged in but need to be
        if (!(c instanceof InterfaceNoSessionRequired) && u == null) {
            res.redirect(ROUTE_LOGIN);
            return "";
        }
        try {
            return c.get(queryMapToHashMap(req.queryMap()), u);
        }
        catch (ControllerException e) {
            return processError(e.getMessage(), req, res);
        }
        catch (SetUserException e) {
            req.session().attribute("user_id", e.getMessage());
            u = getUserFromReq(req);
            if (u != null && u.is_temporary_password) {
                res.redirect(ROUTE_UPDATE_PASSWORD);
            }
            res.redirect(ROUTE_MENU);
            return "";
        }
    }

    protected static User getUserFromReq(spark.Request req)
    {
        String userId = req.session().attribute("user_id");
        User u = null;
        if (userId != null) {
            u = User.find(Integer.parseInt(userId));
        }
        return u;
    }

    protected static String processPost(spark.Request req, spark.Response res, AbstractPostController c)
    {
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

        User u = getUserFromReq(req);
        //Handle users that aren't logged in but need to be
        if (!(c instanceof InterfaceNoSessionRequired) && u == null) {
            res.redirect(ROUTE_LOGIN);
            return "";
        }

        try {
            HashMap<String, String> params = multipartRequestToHashMap(req);

            //Run controller
            res.redirect(c.post(params, u));
        }
        catch (ControllerException e) {
            return processError(e.getMessage(), req, res);
        }
        catch (SetUserException e) {
            req.session().attribute("user_id", e.getMessage());
            u = getUserFromReq(req);
            if (u != null && u.is_temporary_password) {
                res.redirect(ROUTE_UPDATE_PASSWORD);
            }
            res.redirect(ROUTE_MENU);
        }
        return "";
    }

    protected static String processError(String message, spark.Request req, spark.Response res)
    {
        Map<String, String> params = new HashMap<>();
        params.put("message", message);
        res.status(500);
        return controller_error.get(params, null);
    }

    protected static HashMap<String, String> multipartRequestToHashMap(Request req)
    {
        //Load parameters, including files, into hashmap of parameters
        HashMap<String, String> params = new HashMap<>();
        try {
            for (Part part : req.raw().getParts()) {
                InputStream input = part.getInputStream();
                Scanner s = new Scanner(input, "ISO-8859-1").useDelimiter("\\A");
                if (s.hasNext()) {
                    params.put(part.getName(), s.next());
                }
                input.close();
            }
        }
        catch (Exception e) {return params;}
        return params;
    }

    protected static HashMap<String, String> queryMapToHashMap(QueryParamsMap q)
    {
        HashMap<String, String> m = new HashMap<>();
        Map<String, String[]> multimap = q.toMap();
        Object[] keys = multimap.keySet().toArray();
        for (Object key : keys) {
            m.put((String) key, multimap.get(key)[0]);
        }
        return m;
    }
}