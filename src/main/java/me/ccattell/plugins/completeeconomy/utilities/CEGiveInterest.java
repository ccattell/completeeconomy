package me.ccattell.plugins.completeeconomy.utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import org.bukkit.entity.Player;

public class CEGiveInterest {

    public CompleteEconomy plugin;
    boolean enabled = plugin.configs.getBankConfig().getBoolean("Banking.Interest.Enabled");
    boolean online = plugin.configs.getBankConfig().getBoolean("Banking.Interest.Online");
    boolean announce = plugin.configs.getBankConfig().getBoolean("Banking.Interest.Announce");
    long interval = plugin.configs.getBankConfig().getInt("Banking.Interest.Interval") * 20;
    // convert to float
    float cutoff = plugin.configs.getBankConfig().getInt("Banking.Interest.Cutoff") * 1.0F;
    // convert to float
    float percent = plugin.configs.getBankConfig().getInt("Banking.Interest.Amount") * 1.0F;
    CEQueryFactory qf = new CEQueryFactory();

    // add class constructor
    public void CEGiveInterest() {
    }

    public void interest() {

        if (enabled) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
//                    plugin.getServer().getConsoleSender().sendMessage("Give Interest");
                    // do stuff
                    doInterest();
                }
            }, interval, interval);
        }
    }

    private void doInterest() {

        List<Player> online_players = Arrays.asList(plugin.getServer().getOnlinePlayers());
        HashMap<Player, Float> db_players = qf.getPlayers();
        for (Map.Entry<Player, Float> entry : db_players.entrySet()) {
            if ((online && online_players.contains(entry.getKey())) || !online) {
                // calculate interest
                if (entry.getValue() >= cutoff) {
                    float credit = toCents((interval / 86400) * (percent / 100) * entry.getValue());
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
