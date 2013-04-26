package me.ccattell.plugins.completeeconomy.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CEGiveInterest {
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;
    
    public void reloadCustomConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(CompleteEconomy.plugin.getDataFolder()+File.separator+ "bankConfig.yml");
        }
        if(!customConfigFile.exists()) {
            try {
                customConfigFile.createNewFile();
            } catch (IOException e) {
                CompleteEconomy.plugin.getLogger().severe("Could not create Config.yml! " + e);
            }
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        // Look for defaults in the jar
        InputStream defConfigStream = CompleteEconomy.plugin.getResource("bankConfig.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
        customConfig.saveResource("bankConfig.yml", false);
    }
    public FileConfiguration getCustomConfig() {
        if (customConfig == null) {
            this.reloadCustomConfig();
        }
        return customConfig;
    }
    boolean enabled = getCustomConfig().getBoolean("Banking.Interest.Enabled");
    boolean online = getCustomConfig().getBoolean("Banking.Interest.Online");
    boolean announce = getCustomConfig().getBoolean("Banking.Interest.Announce");
    long interval = getCustomConfig().getInt("Banking.Interest.Interval") * 20;
    float cutoff = getCustomConfig().getInt("Banking.Interest.Cutoff");
    float percent = getCustomConfig().getInt("Banking.Interest.Amount");
    CEQueryFactory qf = new CEQueryFactory();

    // add class constructor
    public void CEGiveInterest() {
    }

    public void interest() {

        if (enabled) {
            CompleteEconomy.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(CompleteEconomy.plugin, new Runnable() {
                @Override
                public void run() {
                    CompleteEconomy.plugin.getServer().getConsoleSender().sendMessage("Give Interest");
                    // do stuff
                    doInterest();
                }
            }, 60L, interval);
        }
    }

    private void doInterest() {

        List<Player> online_players = Arrays.asList(CompleteEconomy.plugin.getServer().getOnlinePlayers());
        HashMap<Player, Float> db_players = qf.getPlayers();
        for (Map.Entry<Player, Float> entry : db_players.entrySet()) {
            if ((online && online_players.contains(entry.getKey())) || !online) {
                // calculate interest
                if (entry.getValue() >= cutoff) {
                    float credit = toCents((entry.getValue() / 100) * percent);
                    if (credit > 0) { // don't give interest unless there is some
                        qf.alterBalance("bank", entry.getKey().getName(), credit);
                        if (announce) {
                            String s = new CEMajorMinor().getFormat(credit);
                            entry.getKey().sendMessage("You recieved " + s + " in interest on your savings!");
                        }
                    }
                }
            }
        }
    }

    public static float toCents(float value) {
        value = value * 100;
        long tmp = Math.round(value);
        return (float) tmp / 100;
    }
}
