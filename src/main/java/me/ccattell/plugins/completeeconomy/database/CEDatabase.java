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
 * @author Charlie
 */
public class CEDatabase {
    public Connection connection = null;
    public Statement statement = null;

    public void createTables() {
        try {
            statement = connection.createStatement();
            String queryCE = "CREATE TABLE IF NOT EXISTS CompleteEconomy (" +
"  player_id int(11) NOT NULL AUTO_INCREMENT," +
"  player_name text," +
"  cash_on_hand float DEFAULT NULL," +
"  last_login int(11) DEFAULT NULL," +
"  PRIMARY KEY (player_id)" +
")";
            statement.executeUpdate(queryCE);
        } catch (SQLException e) {
            
        }
    }
}