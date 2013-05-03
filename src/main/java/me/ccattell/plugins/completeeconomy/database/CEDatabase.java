package me.ccattell.plugins.completeeconomy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import static me.ccattell.plugins.completeeconomy.CompleteEconomy.plugin;
import org.bukkit.command.ConsoleCommandSender;


/**
 *
 * @author Charlie and Buwaroblahblah die
 */
public class CEDatabase {

    private static CEDatabase instance = new CEDatabase();
    public ConsoleCommandSender console;
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
            console.sendMessage(plugin.pluginName + "Loading driver...");
            Class.forName("com.mysql.jdbc.Driver");
            console.sendMessage(plugin.pluginName + "Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        String host = "jdbc:" + plugin.getConfig().getString("System.Database.URL");
        String user = plugin.getConfig().getString("System.Database.Username");
        String pass = plugin.getConfig().getString("System.Database.Password");
        try {
            console.sendMessage(plugin.pluginName + "Connecting database...");
            connection = DriverManager.getConnection(host, user, pass);
            console.sendMessage(plugin.pluginName + "Database connected!");
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect the database!", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
