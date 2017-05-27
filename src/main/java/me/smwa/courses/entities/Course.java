package me.smwa.courses.entities;

import me.smwa.courses.abstract_classes.AbstractEntity;

import java.sql.*;
import java.util.ArrayList;

public class Course extends AbstractEntity {
    public Integer id;
    public String name = "";
    public String description = "";

    private static final String TABLE_NAME = "course";

    public void save()
    {
        Connection c = getConnection();
        String query;
        if (this.id == null) {
            this.id = createEntry(TABLE_NAME);
        }
        query = "UPDATE " + TABLE_NAME + " SET name = ?, description = ? WHERE id = ? ";
        try {
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, this.name);
            stmt.setString(2, this.description);
            stmt.setInt(3, this.id);
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

    public ArrayList<Prerequisite> getPrerequisites()
    {
        return Prerequisite.findMultipleBy("course_id", this.id.toString());
    }

    public ArrayList<Prerequisite> getRequiredFor()
    {
        return Prerequisite.findMultipleBy("dependency", this.id.toString());
    }

    public static void createTable()
    {
        executeUpdate("CREATE TABLE " + TABLE_NAME + " (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    name VARCHAR(255),\n" +
                "    description TEXT\n" +
                ")");
    }

    public static Course find(Integer id)
    {
        ResultSet rs = abstractFindOneBy(TABLE_NAME, "id", id.toString());
        if (rs == null) {
            return null;
        }
        return buildFromResult(rs);
    }

    public static ArrayList<Course> findAll()
    {
        ArrayList<Course> list = new ArrayList<>();

        try {
            ResultSet rs = executeQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY name ASC");
            while (rs.next()) {
                list.add(buildFromResult(rs));
            }
        }
        catch (SQLException e) {
            System.out.println("Error while finding all: "+e.getMessage());
        }
        return list;
    }

    private static Course buildFromResult(ResultSet rs)
    {
        try {
            Course u = new Course();
            u.id = rs.getInt("id");
            u.name = rs.getString("name");
            u.description = rs.getString("description");
            return u;
        }
        catch (SQLException e) {
            System.out.println("Failed to build from result: " + e.getMessage());
            return null;
        }
    }
}
