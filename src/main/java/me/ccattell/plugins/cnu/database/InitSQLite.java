package me.ccattell.plugins.cnu.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import static me.ccattell.plugins.cnu.CompleteNovusUtilities.plugin;
import org.bukkit.ChatColor;

/**
 *
 * @author Charlie
 */
public class InitSQLite {

    Database service = Database.getInstance();
    public Connection connection = service.getConnection();
    public Statement statement = null;

    public void CNUInitSQLite() {
    }

    public void initSQLite() {
        try {
            statement = connection.createStatement();
            String queryCNU = "CREATE TABLE IF NOT EXISTS CNUMain (player_name TEXT PRIMARY KEY COLLATE NOCASE, player_UUID TEXT PRIMARY KEY COLLATE NOCASE, cash REAL DEFAULT 0.0, bank REAL DEFAULT 0.0, xp REAL DEFAULT 0.0, last_login INTEGER DEFAULT 0, chat_channel text NOT NULL COLLATE NOCASE)";
            statement.executeUpdate(queryCNU);
            String queryJobs = "CREATE TABLE IF NOT EXISTS CNUJobs (player_name TEXT COLLATE NOCASE, job text NOT NULL COLLATE NOCASE, experience REAL DEFAULT 0, level REAL DEFAULT 1, status TEXT NOT NULL COLLATE NOCASE)";
            statement.executeUpdate(queryJobs);
            String querySkills = "CREATE TABLE IF NOT EXISTS CNUSkills (player_name TEXT COLLATE NOCASE, skill text NOT NULL COLLATE NOCASE, experience REAL DEFAULT 0, level REAL DEFAULT 1)";
            statement.executeUpdate(querySkills);
            String queryShops = "CREATE TABLE IF NOT EXISTS CNUShops (player_name TEXT COLLATE NOCASE, shop_name text NOT NULL COLLATE NOCASE, status text NOT NULL COLLATE NOCASE)";
            statement.executeUpdate(queryShops);
            String queryShopInventory = "CREATE TABLE IF NOT EXISTS CNUShopInventory (shop_name text NOT NULL COLLATE NOCASE, mode text NOT NULL COLLATE NOCASE, item text NOT NULL COLLATE NOCASE, quantity REAL DEFAULT 0, buy_price REAL DEFAULT 0, sell_price REAL DEFAULT 0 NOT NULL)";
            statement.executeUpdate(queryShopInventory);
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Could not create SQLite tables: " + e.getMessage() + ChatColor.RESET);
        }
    }
}
