/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ccattell.plugins.completeeconomy.database;

import java.io.File;
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
        CompleteEconomy.plugin.console.sendMessage(CompleteEconomy.plugin.pluginName + "Loading Database");
        if(CompleteEconomy.plugin.getConfig().getString("System.Database.Type").equals("sqlite")){
            
        }else{
            
        }
    }
    public void initSQLite() {
        String path = getDataFolder() + File.separator + "CompleteEconomy.db";
        service.setConnection(path);
        try {
            statement = connection.createStatement();
            String queryCE = "CREATE TABLE IF NOT EXISTS CEMain (player_name TEXT PRIMARY KEY,  cash REAL DEFAULT NULL,  bank REAL DEFAULT NULL,  xp REAL DEFAULT NULL,  last_login INTEGER DEFAULT NULL)";
            statement.executeUpdate(queryCE);
            String queryJobs = "CREATE TABLE IF NOT EXISTS CEJobs (player_name TEXT PRIMARY KEY, job text NOT NULL, experience INTEGER DEFAULT NULL, level INTEGER DEFAULT NULL)";
            statement.executeUpdate(queryJobs);
        } catch (SQLException e) {
            System.out.println(CompleteEconomy.plugin.pluginName + "Could not create SQLite tables: " + e.getMessage());
        }
    }
    public void initMYSQL() {
        try {
            String queryCE = "CREATE TABLE IF NOT EXISTS CEMain (player_name text NOT NULL, cash float NOT NULL, bank float NOT NULL, xp float NOT NULL, last_login int(11) NOT NULL)";
            statement.executeUpdate(queryCE);
            String queryJobs = "CREATE TABLE IF NOT EXISTS CEJobs (player_name text NOT NULL, job text NOT NULL, experience int(11) NOT NULL, level int(11) NOT NULL)";
            statement.executeUpdate(queryJobs);
        } catch (SQLException e) {
            System.out.println(CompleteEconomy.plugin.pluginName + "Could not create MySQL tables: " + e.getMessage());
        }
    }
    public void setConnection(String path) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
    }
}
