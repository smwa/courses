package me.smwa.courses.entities;

import me.smwa.courses.abstract_classes.AbstractEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Receiver extends AbstractEntity {
    public Integer id;
    public String email = "";

    private static final String TABLE_NAME = "receiver";

    public void setEmail(String email) { this.email = email;}
    public void setID(Integer id) { this.id = id;}

    public void save()
    {
        Connection c = getConnection();
        String query;
        if (this.id == null) {
            this.id = createEntry(TABLE_NAME);
        }
        query = "UPDATE " + TABLE_NAME + " SET email = ? WHERE id = ? ";
        try {
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, this.email);
            stmt.setInt(2, this.id);
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
                "    email VARCHAR(255)\n" +
                ")");
    }

    public static Receiver find(Integer id)
    {
        ResultSet rs = abstractFindOneBy(TABLE_NAME, "id", id.toString());
        if (rs == null) {
            return null;
        }
        return buildFromResult(rs);
    }

    public static ArrayList<Receiver> findAll()
    {
        ArrayList<Receiver> list = new ArrayList<>();

        try {
            ResultSet rs = executeQuery("SELECT * FROM " + TABLE_NAME);
            while (rs.next()) {
                list.add(buildFromResult(rs));
            }
        }
        catch (SQLException e) {
            System.out.println("Error while finding all: "+e.getMessage());
        }
        return list;
    }

    private static Receiver buildFromResult(ResultSet rs)
    {
        try {
            Receiver u = new Receiver();
            u.id = rs.getInt("id");
            u.email = rs.getString("email");
            return u;
        }
        catch (SQLException e) {
            System.out.println("Failed to build from result: " + e.getMessage());
            return null;
        }
    }
}
//user.findAll()
//Reciever r = new Reciever();
//r.email = "ajkhsdfk@gmail";
//r.save();
//
