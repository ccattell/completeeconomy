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
public class InitMySQL {

    Database service = Database.getInstance();
    public Connection connection = service.getConnection();
    public Statement statement = null;

    public void CNUInitMySQL() {
    }

    public void initMYSQL() {
        try {
            statement = connection.createStatement();
            String queryCNU = "CREATE TABLE IF NOT EXISTS CNUMain (player_name text NOT NULL, cash float NOT NULL, bank float NOT NULL, xp float NOT NULL, last_login int(11) NOT NULL) CHARACTER SET utf8 COLLATE utf8_general_ci, chat_channel text NOT NULL";
            statement.executeUpdate(queryCNU);
            String queryJobs = "CREATE TABLE IF NOT EXISTS CNUJobs (player_name text NOT NULL, job text NOT NULL, experience int(11) NOT NULL, level int(11) NOT NULL, status text NOT NULL) CHARACTER SET utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(queryJobs);
            String querySkills = "CREATE TABLE IF NOT EXISTS CNUSkills (player_name text NOT NULL, skill text NOT NULL, experience int(11) NOT NULL, level int(11) NOT NULL) CHARACTER SET utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(querySkills);
            String queryShops = "CREATE TABLE IF NOT EXISTS CNUShops (player_name text NOT NULL, shop_name text NOT NULL, status text NOT NULL) CHARACTER SET utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(queryShops);
            String queryShopInventory = "CREATE TABLE IF NOT EXISTS CNUShopInventory (shop_name text NOT NULL, mode text NOT NULL, item text NOT NULL, quantity int(11) NOT NULL) CHARACTER SET utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(queryShopInventory);
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Could not create MySQL tables: " + e.getMessage() + ChatColor.RESET);
        }
    }
}
