package me.ccattell.plugins.completeeconomy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

/**
 *
 * @author Charlie
 */
public class CEMainResultSet {

    public CompleteEconomy plugin;
    public ConsoleCommandSender console;
    private CEDatabase service = CEDatabase.getInstance();
    private Connection connection = service.getConnection();
    private HashMap<String, Object> where;
    private String player_name;
    private float cash;
    private float bank;
    private float xp;
    private long last_login;

    public CEMainResultSet(HashMap<String, Object> where) {
        this.where = where;
    }

    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String wheres = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                sbw.append(entry.getKey()).append(" = ? AND ");
            }
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
        }
        String query = "SELECT * FROM CEMain" + wheres;
        try {
            statement = connection.prepareStatement(query);
            if (where != null) {
                int i = 1;
                for (Map.Entry<String, Object> entry : where.entrySet()) {
                    if (entry.getValue().getClass().equals(String.class)) {
                        statement.setString(i, entry.getValue().toString());
                    } else {
                        if (entry.getValue().getClass().getName().contains("Float")) {
                            statement.setDouble(i, Float.parseFloat(entry.getValue().toString()));
                        } else if (entry.getValue().getClass().getName().contains("Long")) {
                            statement.setLong(i, Long.parseLong(entry.getValue().toString()));
                        } else {
                            statement.setInt(i, Integer.parseInt(entry.getValue().toString()));
                        }
                    }
                    i++;
                }
                where.clear();
            }
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                this.player_name = rs.getString("player_name");
                this.cash = rs.getFloat("cash");
                this.bank = rs.getFloat("bank");
                this.xp = rs.getFloat("xp");
                this.last_login = rs.getLong("last_login");
            } else {
                return false;
            }
        } catch (SQLException e) {
            console.sendMessage(plugin.pluginName + ChatColor.GOLD + "ResultSet error for CEMain table! " + e.getMessage() + ChatColor.RESET);
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Error closing CEMain table! " + e.getMessage() + ChatColor.RESET);
            }
        }
        return true;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public float getCash() {
        return cash;
    }

    public float getBank() {
        return bank;
    }

    public float getXp() {
        return xp;
    }

    public long getLast_login() {
        return last_login;
    }
}
