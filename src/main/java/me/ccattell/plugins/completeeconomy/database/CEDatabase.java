/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ccattell.plugins.completeeconomy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Charlie and Buwaroblahblah      die
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
            String queryCE = "CREATE TABLE IF NOT EXISTS CompleteEconomy ("
                    + "  player_id INTEGER NOT NULL PRIMARY KEY,"
                    + "  player_name TEXT,"
                    + "  cash_on_hand REAL DEFAULT NULL,"
                    + "  last_login INTEGER DEFAULT NULL"
                    + ")";
            statement.executeUpdate(queryCE);
        } catch (SQLException e) {
            System.out.println("Could not create table: " + e.getMessage());
        }
    }

    public void setConnection(String path) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
    }
}
