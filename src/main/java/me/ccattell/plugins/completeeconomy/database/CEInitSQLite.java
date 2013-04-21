package me.ccattell.plugins.completeeconomy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;

/**
 *
 * @author Charlie
 */
public class CEInitSQLite {

    CEDatabase service = CEDatabase.getInstance();
    public Connection connection = service.getConnection();
    public Statement statement = null;

    public void CEInitSQLite() {
    }

    public void initSQLite() {
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
}
