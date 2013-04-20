/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ccattell.plugins.completeeconomy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;

/**
 *
 * @author Charlie
 */
public class CEDatabase {

    private static CEDatabase instance = new CEDatabase();
    public Connection connection = null;
    public Statement statement = null;

    public static synchronized CEDatabase getInstance() {
        return instance;
    }

    public void createTables() {
        try {
            statement = connection.createStatement();
            String type = CompleteEconomy.plugin.getConfig().getString("System.Database.Type");
            String queryCE = getSQL(type);
            statement.executeUpdate(queryCE);
        } catch (SQLException e) {
            System.out.println("Could not create table: " + e.getMessage());
        }
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

    private String getSQL(String type) {
        String SQL;
        if (type.equals("sqlite")) {
            // sqlite
            SQL = "CREATE TABLE IF NOT EXISTS CompleteEconomy ("
                    + "  player_id INTEGER NOT NULL PRIMARY KEY,"
                    + "  player_name TEXT,"
                    + "  cash_on_hand REAL DEFAULT NULL,"
                    + "  last_login INTEGER DEFAULT NULL"
                    + ")";
        } else {
            // mysql
            SQL = "CREATE TABLE IF NOT EXISTS CompleteEconomy ("
                    + "  player_id int(11) NOT NULL AUTO_INCREMENT,"
                    + "  player_name text,"
                    + "  cash_on_hand float DEFAULT NULL,"
                    + "  last_login int(11) DEFAULT NULL,"
                    + "  PRIMARY KEY (player_id)"
                    + ")";
        }
        return SQL;
    }
}
