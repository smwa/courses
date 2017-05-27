package me.smwa.courses.entities;

import me.smwa.courses.abstract_classes.AbstractEntity;
import me.smwa.courses.utilities.Password;

import java.sql.*;
import java.util.ArrayList;

public class User extends AbstractEntity {
    public Integer id;
    public String email = "";
    private String password = "";
    public boolean is_temporary_password = false;
    public boolean permission_edit_users = false;
    public boolean permission_edit_recipients = false;
    public boolean permission_process_report = false;
    public boolean permission_edit_classes = false;

    private static final String TABLE_NAME = "login";

    public void setEmail(String email) { this.email = email;}
    public void setID(Integer id) { this.id = id;}

    public void save()
    {
        Connection c = getConnection();
        String query;
        if (this.id == null) {
            this.id = createEntry(TABLE_NAME);
        }
        query = "UPDATE " + TABLE_NAME + " SET email = ?, password = ?, is_temporary_password = ?, permission_edit_users = ?, permission_edit_receivers = ?, permission_run_report = ?, permission_edit_classes = ? WHERE id = ? ";
        try {
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, this.email);
            stmt.setString(2, this.password);
            stmt.setBoolean(3, this.is_temporary_password);
            stmt.setBoolean(4, this.permission_edit_users);
            stmt.setBoolean(5, this.permission_edit_recipients);
            stmt.setBoolean(6, this.permission_process_report);
            stmt.setBoolean(7, this.permission_edit_classes);
            stmt.setInt(8, this.id);
            stmt.executeUpdate();
        }
        catch (SQLException exc) {
//            return;
        }
    }

    public void remove()
    {
        Connection c = getConnection();
        String query;
        query = "DELETE FROM " + TABLE_NAME + " WHERE id = ? ";
        try {
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
        }
        catch (SQLException exc) {
//            return;
        }
    }

    public static void createTable()
    {
        executeUpdate("CREATE TABLE " + TABLE_NAME + " (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    email VARCHAR(255),\n" +
                "    password VARCHAR(255),\n" +
                "    is_temporary_password BIT DEFAULT 0,\n" +
                "    permission_edit_users BIT DEFAULT 0,\n" +
                "    permission_edit_receivers BIT DEFAULT 0,\n" +
                "    permission_run_report BIT DEFAULT 0,\n" +
                "    permission_edit_classes BIT DEFAULT 0\n" +
                "); INSERT INTO " + TABLE_NAME + "(email, password, is_temporary_password, permission_edit_users, permission_edit_receivers, permission_run_report, permission_edit_classes) VALUES ('admin', 'SLCwe12jaRF3WNAtypvsyfoAVuBMVdgtYA1VpuwGApQ=$wlynm6ILhCwbiPYNuNIp6O+SC3hmPoJdUAQ/xwGDNZo=', 1, 1, 1, 1, 1);");
        System.out.println("New user created: email: admin, password: admin");
    }

    public boolean comparePassword(String password) {
        if (this.is_temporary_password) return true;
        try {
            if (Password.check(password, this.password)) {
                return true;
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }

    public void setPassword(String password) throws Exception {
        try {
            this.password = Password.getSaltedHash(password);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public static User find(Integer user_id)
    {
        ResultSet rs = abstractFindOneBy(TABLE_NAME, "id", user_id.toString());
        if (rs == null) {
            return null;
        }
        return buildUserFromResult(rs);
    }

    public static User findByEmail(String email)
    {
        ResultSet rs = abstractFindOneBy(TABLE_NAME, "email", email);
        if (rs == null) {
            return null;
        }
        return buildUserFromResult(rs);
    }

    public static ArrayList<User> findAll()
    {
        ArrayList<User> users = new ArrayList<>();

        try {
            ResultSet rs = executeQuery("SELECT * FROM " + TABLE_NAME);
            while (rs.next()) {
                users.add(buildUserFromResult(rs));
            }
        }
        catch (SQLException e) {
            System.out.println("Error while finding user: "+e.getMessage());
        }
        return users;
    }

    private static User buildUserFromResult(ResultSet rs)
    {
        try {
            User u = new User();
            u.id = rs.getInt("id");
            u.email = rs.getString("email");
            u.password = rs.getString("password");
            u.is_temporary_password = rs.getBoolean("is_temporary_password");
            u.permission_edit_users = rs.getBoolean("permission_edit_users");
            u.permission_edit_recipients = rs.getBoolean("permission_edit_receivers");
            u.permission_process_report = rs.getBoolean("permission_run_report");
            u.permission_edit_classes = rs.getBoolean("permission_edit_classes");
            return u;
        }
        catch (SQLException e) {
            System.out.println("Failed to build user from result: " + e.getMessage());
            return null;
        }
    }

    public static boolean tableExists() {
        Connection c = getConnection();
        if (c == null) return false;
        try {
            c.createStatement().executeUpdate("SELECT * FROM " + TABLE_NAME + " LIMIT 1 ");
            return true;
        }
        catch (SQLException e) {
            return false;
        }
    }
}
