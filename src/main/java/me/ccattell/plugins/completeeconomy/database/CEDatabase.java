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
        if(CompleteEconomy.plugin.getConfig().getString("System.Database.Type").equals("sqlite")){
            try {
                statement = connection.createStatement();

                String queryCE = "CREATE TABLE IF NOT EXISTS CEMain (player_name TEXT PRIMARY KEY,  cash REAL DEFAULT NULL,  bank REAL DEFAULT NULL,  last_login INTEGER DEFAULT NULL)";
                statement.executeUpdate(queryCE);
                String queryJobs = "CREATE TABLE IF NOT EXISTS CEJobs (player_name TEXT, job_id INTEGER DEFAULT NULL, experience INTEGER DEFAULT NULL, level INTEGER DEFAULT NULL)";
                statement.executeUpdate(queryJobs);
            } catch (SQLException e) {
                System.out.println(CompleteEconomy.plugin.pluginName + "Could not create table: " + e.getMessage());
            }
        }else{
        }
    }

    public void setConnection(String path) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
    }
}
