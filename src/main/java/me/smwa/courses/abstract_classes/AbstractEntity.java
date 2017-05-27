package me.smwa.courses.abstract_classes;

import java.sql.*;

abstract public class AbstractEntity {
    private static Connection connection;

    static protected Connection getConnection()// throws SQLException
    {
        try {
            Class.forName("org.sqlite.JDBC");
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:sqlite:courses.db");
            }
            return connection;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to connect");
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("(class not found)");
        }
        return null;
    }

    static protected void executeUpdate(String sql)
    {
        Connection c = getConnection();
        if (c == null) return;
        try {
            c.createStatement().executeUpdate(sql);
        }
        catch (SQLException e) {
            System.out.println("Failed to run executeUpdate for sql: "+sql + " - Because: " + e.getMessage());
        }
    }

    static protected ResultSet executeQuery(String sql) throws SQLException
    {
        Connection c = getConnection();
        if (c == null) return null;
        PreparedStatement stmt = c.prepareStatement(sql);
        return stmt.executeQuery();
    }

    static protected ResultSet abstractFindOneBy(String table, String column, String value)
    {
        ResultSet rs = abstractFindBy(table, column, value);

        try {
            if (rs == null || !rs.next()) return null;
            return rs;
        }
        catch (SQLException e) {
            System.out.println("Error while finding one " + table + " by " + column + ": "+e.getMessage());
            return null;
        }
    }

    static protected ResultSet abstractFindBy(String table, String column, String value)
    {
        if (value == null) return null;

        try {
            Connection c = getConnection();
            if (c == null) return null;
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM " + table + " WHERE " + column + " = ?");
            stmt.setString(1, value);
            return stmt.executeQuery();
        }
        catch (SQLException e) {
            System.out.println("Error while finding " + table + " by " + column + ": "+e.getMessage());
            return null;
        }
    }

    static protected int createEntry(String table)
    {
        String query = "INSERT INTO " + table + " DEFAULT VALUES";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next())
            {
                return rs.getInt(1);
            }
        }
        catch (SQLException exc) {
            System.out.println("Failed to create entry for " + table);
        }
        return 0;
    }

    public abstract void save();
    public abstract void remove();
}
