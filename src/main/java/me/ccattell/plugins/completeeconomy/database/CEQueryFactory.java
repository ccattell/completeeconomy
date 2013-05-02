package me.ccattell.plugins.completeeconomy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import org.bukkit.entity.Player;

/**
 * Do basic SQL INSERT, UPDATE and DELETE queries.
 *
 * @author Charlie
 */
public class CEQueryFactory {

    CEDatabase service = CEDatabase.getInstance();
    Connection connection = service.getConnection();

    public CEQueryFactory() {
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
            System.out.println("Update error for " + table + "! " + e.getMessage());
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
                System.out.println("Error closing " + table + "! " + e.getMessage());
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
        //System.out.println(query);
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
            System.out.println("Update error for " + table + "! " + e.getMessage());
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println("Error closing " + table + "! " + e.getMessage());
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
        //System.out.println(query);
        try {
            statement = connection.createStatement();
            return (statement.executeUpdate(query) > 0);
        } catch (SQLException e) {
            System.out.println("Delete error for " + table + "! " + e.getMessage());
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println("Error closing " + table + "! " + e.getMessage());
            }
        }
    }

    public void alterBalance(String field, String player, float amount) {
        Statement statement = null;
        String query = "UPDATE CEMain SET " + field + " = " + field + " + " + amount + " WHERE player_name = '" + player + "'";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Alter " + field + " balance error! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println("Alter " + field + " balance error closing CEMain! " + e.getMessage());
            }
        }
    }

    public void killBalance(String field, String player) {
        Statement statement = null;
        String query = "UPDATE CEMain SET " + field + " = '0' WHERE player_name = '" + player + "'";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Alter " + field + " balance error! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println("Alter " + field + " balance error closing CEMain! " + e.getMessage());
            }
        }
    }

    public void checkPlayerJob(String job, String player) {
        Statement statement = null;
        String query = "select * from CEJobs WHERE player_name = '" + player + "' and job = '" + job + "'";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            //need to return the number of rows found
        } catch (SQLException e) {
            System.out.println("Couldn't get " + job + " info! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println("Couldn't get " + job + " info, closing CEJobs! " + e.getMessage());
            }
        }
    }

    public void getPlayerJobs(String player) {
        Statement statement = null;
        String query = "select * from CEJobs WHERE player_name = '" + player + "'";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            //need to return the jobs found
        } catch (SQLException e) {
            System.out.println("Couldn't get " + player + "'s jobs! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println("Couldn't get " + player + "'s jobs, closing CEJobs! " + e.getMessage());
            }
        }
    }

    public HashMap<Player, Float> getPlayers() {
        HashMap<Player, Float> data = new HashMap<Player, Float>();
        Statement statement = null;
        String query = "SELECT player_name, bank FROM CEMain";
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                data.put(CompleteEconomy.plugin.getServer().getPlayer(rs.getString("player_name")), rs.getFloat("bank"));
            }
        } catch (SQLException e) {
            System.out.println("Get Players error! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println("Get Players error closing CEMain! " + e.getMessage());
            }
        }
        return data;
    }
}
