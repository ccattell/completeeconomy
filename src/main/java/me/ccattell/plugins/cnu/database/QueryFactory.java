package me.ccattell.plugins.cnu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import static me.ccattell.plugins.cnu.CompleteNovusUtilities.plugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Do basic SQL INSERT, UPDATE and DELETE queries.
 *
 * @author Charlie
 */
public class QueryFactory {

    Database service = Database.getInstance();
    Connection connection = service.getConnection();

    public QueryFactory() {
    }

    /**
     * Inserts data into an SQLite database table. This method builds a prepared
     * SQL statement from the parameters supplied and then executes the insert.
     *
     * @param table the database table name to insert the data into.
     * @param data a HashMap<String, Object> of table fields and values to
     * insert.
     * @return the number of records that were inserted
     */
    public int doInsert(String table, HashMap<String, Object> data) {
        PreparedStatement ps = null;
        ResultSet idRS = null;
        String fields;
        String questions;
        StringBuilder sbf = new StringBuilder();
        StringBuilder sbq = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sbf.append(entry.getKey()).append(",");
            sbq.append("?,");
        }
        fields = sbf.toString().substring(0, sbf.length() - 1);
        questions = sbq.toString().substring(0, sbq.length() - 1);
        try {
            ps = connection.prepareStatement("INSERT INTO " + table + " (" + fields + ") VALUES (" + questions + ")");
            int i = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue().getClass().equals(String.class)) {
                    ps.setString(i, entry.getValue().toString());
                } else {
                    if (entry.getValue().getClass().getName().contains("Float")) {
                        ps.setDouble(i, Float.parseFloat(entry.getValue().toString()));
                    } else if (entry.getValue().getClass().getName().contains("Long")) {
                        ps.setLong(i, Long.parseLong(entry.getValue().toString()));
                    } else {
                        ps.setInt(i, Integer.parseInt(entry.getValue().toString()));
                    }
                }
                i++;
            }
            data.clear();
            ps.executeUpdate();
            idRS = ps.getGeneratedKeys();
            return (idRS.next()) ? idRS.getInt(1) : -1;
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Update error for " + table + "! " + ChatColor.RESET + e.getMessage());
            return -1;
        } finally {
            try {
                if (idRS != null) {
                    idRS.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Error closing " + table + "! " + ChatColor.RESET + e.getMessage());
            }
        }
    }

    /**
     * Updates data in an SQLite database table. This method builds an SQL query
     * string from the parameters supplied and then executes the update.
     *
     * @param table the database table name to update.
     * @param data a HashMap<String, Object> of table fields and values update.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to update.
     * @return true or false depending on whether the database update was
     * successful
     */
    public boolean doUpdate(String table, HashMap<String, Object> data, HashMap<String, Object> where) {
        PreparedStatement statement = null;
        String updates;
        String wheres;
        StringBuilder sbu = new StringBuilder();
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sbu.append(entry.getKey()).append(" = ?,");
        }
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("' AND ");
            } else {
                sbw.append(entry.getValue()).append(" AND ");
            }
        }
        where.clear();
        updates = sbu.toString().substring(0, sbu.length() - 1);
        wheres = sbw.toString().substring(0, sbw.length() - 5);
        String query = "UPDATE " + table + " SET " + updates + " WHERE " + wheres;
        try {
            statement = connection.prepareStatement(query);
            int s = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue().getClass().equals(String.class)) {
                    statement.setString(s, entry.getValue().toString());
                }
                if (entry.getValue() instanceof Integer) {
                    statement.setInt(s, (Integer) entry.getValue());
                }
                if (entry.getValue() instanceof Long) {
                    statement.setLong(s, (Long) entry.getValue());
                }
                s++;
            }
            data.clear();
            return (statement.executeUpdate() > 0);
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Update error for " + table + "! " + ChatColor.RESET + e.getMessage());
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Error closing " + table + "! " + ChatColor.RESET + e.getMessage());
            }
        }
    }

    /**
     * Deletes rows from an SQLite database table. This method builds an SQL
     * query string from the parameters supplied and then executes the delete.
     *
     * @param table the database table name to insert the data into.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to delete.
     * @return true or false depending on whether the data was deleted
     * successfully
     */
    public boolean doDelete(String table, HashMap<String, Object> where) {
        Statement statement = null;
        String values;
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("' AND ");
            } else {
                sbw.append(entry.getValue()).append(" AND ");
            }
        }
        where.clear();
        values = sbw.toString().substring(0, sbw.length() - 5);
        String query = "DELETE FROM " + table + " WHERE " + values;
        try {
            statement = connection.createStatement();
            return (statement.executeUpdate(query) > 0);
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Delete error for " + table + "! " + ChatColor.RESET + e.getMessage());
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Error closing " + table + "! " + ChatColor.RESET + e.getMessage());
            }
        }
    }

    public void alterBalance(String field, String player, float amount) {
        Statement statement = null;
        String query = "UPDATE CNUMain SET " + field + " = " + field + " + " + amount + " WHERE player_name = '" + player + "'";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Alter " + field + " balance error! " + ChatColor.RESET + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Alter " + field + " balance error, closing CNUMain! " + ChatColor.RESET + e.getMessage());
            }
        }
    }

    public void alterExperience(double amount, String player, String job) {
        Statement statement = null;
        String query = "UPDATE CNUJobs SET experience = (experience + " + amount + ") WHERE player_name = '" + player + "' AND job = '" + job + "'";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Alter experience level error! " + ChatColor.RESET + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Alter experience level error, closing CNUJobs! " + ChatColor.RESET + e.getMessage());
            }
        }
    }

    public void killBalance(String field, String player) {
        Statement statement = null;
        String query = "UPDATE CNUMain SET " + field + " = '0' WHERE player_name = '" + player + "'";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Alter " + field + " balance error! " + ChatColor.RESET + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Alter " + field + " balance error, closing CNUMain! " + ChatColor.RESET + e.getMessage());
            }
        }
    }

    public String checkPlayerJob(String job, String player) {
        Statement statement = null;
        String query = "select COUNT(*) AS rowcount from CNUJobs WHERE player_name = '" + player + "' and job = '" + job + "' and status = 'active'";
        String query2 = "select COUNT(*) AS rowcount from CNUJobs WHERE player_name = '" + player + "' and job = '" + job + "' and status = 'inactive'";
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            int rowCount = rs.getInt("rowcount");

            ResultSet rs2 = statement.executeQuery(query2);
            int rowCount2 = rs2.getInt("rowcount");

            if (rowCount2 != 0) {
                return "inactive";
            } else if (rowCount != 0) {
                return "active";
            } else {
                return "none";
            }
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Couldn't get " + job + " info! " + ChatColor.RESET + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Couldn't get " + job + " info, closing CNUJobs! " + ChatColor.RESET + e.getMessage());
            }
        }
        return "none";
    }

    public String checkShop(String shop, String player) {
        Statement statement = null;
        String query = "select COUNT(*) AS rowcount from CNUShops WHERE shop_name = '" + shop + "'";
        String query2 = "select COUNT(*) AS rowcount from CNUShops WHERE shop_name = '" + shop + "' and player_name = '" + player + "'";
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            int rowCount = rs.getInt("rowcount");

            ResultSet rs2 = statement.executeQuery(query2);
            int rowCount2 = rs2.getInt("rowcount");

            if (rowCount2 != 0) {
                return "found";
            } else if (rowCount != 0) {
                return "other";
            } else {
                return "none";
            }
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Couldn't get " + shop + " info! " + ChatColor.RESET + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Couldn't get " + shop + " info, closing CNUSHopss! " + ChatColor.RESET + e.getMessage());
            }
        }
        return "none";
    }

    public String checkShopStatus(String shop) {
        Statement statement = null;
        String query = "select * from CNUShops WHERE shop_name = '" + shop + "'";
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            String current_status = rs.getString("status");
            return current_status;
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Couldn't get " + shop + " info! " + e.getMessage() + ChatColor.RESET);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Couldn't get " + shop + " info, closing CNUShops! " + e.getMessage() + ChatColor.RESET);
            }
        }
        return "none";
    }

    public int checkPLayerShops(String player) {
        Statement statement = null;
        String query = "select COUNT(*) as rowcount from CNUShops WHERE player_name = '" + player + "' and status = 'edit'";
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            int rowCount = rs.getInt("rowcount");
            return rowCount;
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Couldn't get info! " + e.getMessage() + ChatColor.RESET);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Couldn't get info, closing CNUShops! " + e.getMessage() + ChatColor.RESET);
            }
        }
        return 0;
    }

    public HashMap<String, String> getPlayerJobs(String player) {
        long level_exp;
        HashMap<String, String> data = new HashMap<String, String>();
        Statement statement = null;
        String query = "select * from CNUJobs WHERE player_name = '" + player + "' and status = 'active'";
//        String query2 = "select COUNT(*) AS rowcount from CNUJobs WHERE player_name = '" + player + "' and status = 'active'";
        try {
            statement = connection.createStatement();

//            ResultSet rs2 = statement.executeQuery(query2);
//            int num_jobs = rs2.getInt("rowcount");

            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                level_exp = Math.round(100 * Math.pow(1.13, rs.getInt("level")));
                data.put(rs.getString("job"), rs.getInt("level") + "," + rs.getInt("experience") + "," + level_exp);
            }
            //need to return the jobs found
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Couldn't get " + player + "'s jobs! " + ChatColor.RESET + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Couldn't get " + player + "'s jobs, closing CNUJobs! " + ChatColor.RESET + e.getMessage());
            }
        }
        return data;
    }

    public HashMap<Player, Float> getPlayers() {
        HashMap<Player, Float> data = new HashMap<Player, Float>();
        Statement statement = null;
        String query = "SELECT player_name, bank FROM CNUMain";
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.put(plugin.getServer().getPlayer(rs.getString("player_name")), rs.getFloat("bank"));
            }
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Get Players error! " + ChatColor.RESET + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Get Players error closing CNUMain! " + ChatColor.RESET + e.getMessage());
            }
        }
        return data;
    }
}
