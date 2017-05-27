package me.smwa.courses.entities;

import me.smwa.courses.abstract_classes.AbstractEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Prerequisite extends AbstractEntity {
    public Integer id;
    public Integer course_id = 0;
    public Integer dependency = 0;
    public String minimum_grade = "C";

    private static final String TABLE_NAME = "prerequisite";

    public void save()
    {
        Connection c = getConnection();
        String query;
        if (this.id == null) {
            this.id = createEntry(TABLE_NAME);
        }
        query = "UPDATE " + TABLE_NAME + " SET course_id = ?, dependency = ?, minimum_grade = ? WHERE id = ? ";
        try {
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, this.course_id);
            stmt.setInt(2, this.dependency);
            stmt.setString(3, this.minimum_grade);
            stmt.setInt(4, this.id);
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
                "    course_id INTEGER,\n" +
                "    dependency INTEGER,\n" +
                "    minimum_grade VARCHAR(1)\n" +
                ")");
    }

    public static Prerequisite find(Integer id)
    {
        ResultSet rs = abstractFindOneBy(TABLE_NAME, "id", id.toString());
        if (rs == null) {
            return null;
        }
        return buildFromResult(rs);
    }

    public static ArrayList<Prerequisite> findMultipleBy(String column, String value)
    {
        ArrayList<Prerequisite> prereqs = new ArrayList<>();

        ResultSet rs = abstractFindBy(TABLE_NAME, column, value);
        if (rs == null) {
            return prereqs;
        }

        try {
            while (rs.next()) {
                prereqs.add(buildFromResult(rs));
            }
        }
        catch (SQLException e) {
            System.out.println("Failed to find multiple");
        }

        return prereqs;
    }

    public static ArrayList<Prerequisite> findAll()
    {
        ArrayList<Prerequisite> list = new ArrayList<>();

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

    public Course getCourse() {
        return Course.find(this.course_id);
    }

    public Course getDependency() {
        return Course.find(this.dependency);
    }

    private static Prerequisite buildFromResult(ResultSet rs)
    {
        try {
            Prerequisite u = new Prerequisite();
            u.id = rs.getInt("id");
            u.course_id = rs.getInt("course_id");
            u.dependency = rs.getInt("dependency");
            u.minimum_grade = rs.getString("minimum_grade");
            return u;
        }
        catch (SQLException e) {
            System.out.println("Failed to build from result: " + e.getMessage());
            return null;
        }
    }
}
