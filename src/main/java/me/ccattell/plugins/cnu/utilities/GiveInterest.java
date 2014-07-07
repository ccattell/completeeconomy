package me.ccattell.plugins.cnu.utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ccattell.plugins.cnu.CompleteNovusUtilities;
import me.ccattell.plugins.cnu.database.QueryFactory;
import org.bukkit.entity.Player;

public class GiveInterest {

    private CompleteNovusUtilities plugin;
    boolean enabled;
    boolean online;
    boolean announce;
    long interval;
    float cutoff;
    float percent;
    QueryFactory qf;

    // add class constructor
    public void CNUGiveInterest(CompleteNovusUtilities plugin) {
        this.plugin = plugin;
        this.enabled = plugin.configs.getBankConfig().getBoolean("Banking.Interest.Enabled");
        this.online = plugin.configs.getBankConfig().getBoolean("Banking.Interest.Online");
        this.announce = plugin.configs.getBankConfig().getBoolean("Banking.Interest.Announce");
        this.interval = plugin.configs.getBankConfig().getInt("Banking.Interest.Interval") * 20;
        // convert to float
        this.cutoff = plugin.configs.getBankConfig().getInt("Banking.Interest.Cutoff") * 1.0F;
        // convert to float
        this.percent = plugin.configs.getBankConfig().getInt("Banking.Interest.Amount") * 1.0F;
        this.qf = new QueryFactory();
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
                            String s = new MajorMinor().getFormat(credit);
                            entry.getKey().sendMessage("You recieved " + s + " in interest on your savings!");
                        }
                    }
                }
            }
        }
    }

    public static float toCents(float value) {
        value *= 100;
        long tmp = Math.round(value);
        return (float) tmp / 100;
    }
}
