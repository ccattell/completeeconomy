package me.ccattell.plugins.completeeconomy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;

/**
 *
 * @author Charlie and Buwaroblahblah die
 */
public class CEDatabase {

    private static CEDatabase instance = new CEDatabase();
    public Connection connection = null;
    public Statement statement = null;

    public static synchronized CEDatabase getInstance() {
        return instance;
    }

    public void setConnection(String path) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
    }

    public void setConnection() throws Exception {
        try {
            System.out.println("Loading driver...");
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        String host = "jdbc:" + CompleteEconomy.plugin.getConfig().getString("System.Database.URL");
        String user = CompleteEconomy.plugin.getConfig().getString("System.Database.Username");
        String pass = CompleteEconomy.plugin.getConfig().getString("System.Database.Password");
        try {
            System.out.println("Connecting database...");
            connection = DriverManager.getConnection(host, user, pass);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect the database!", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
