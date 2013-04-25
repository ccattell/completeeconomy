package me.ccattell.plugins.completeeconomy.utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import me.ccattell.plugins.completeeconomy.database.CEQueryFactory;
import org.bukkit.entity.Player;

public class GiveInterest {

    boolean enabled = CompleteEconomy.plugin.getConfig().getBoolean("System.Interest.Enabled");
    final boolean online = CompleteEconomy.plugin.getConfig().getBoolean("System.Interest.Online");
    boolean announce = CompleteEconomy.plugin.getConfig().getBoolean("System.Interest.Announce");
    long interval = CompleteEconomy.plugin.getConfig().getInt("System.Interest.Interval") * 20;
    float cutoff = CompleteEconomy.plugin.getConfig().getInt("System.Interest.Amount.Cutoff");
    float percent = CompleteEconomy.plugin.getConfig().getInt("System.Interest.Amount.Percent");
    CEQueryFactory qf = new CEQueryFactory();

    // add class constructor
    public void GiveInterest() {
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
                    qf.alterBalance("bank", entry.getKey().getName(), credit);
                    if (announce) {
                        String s = new CEMajorMinor().getFormat(credit);
                        entry.getKey().sendMessage("You recieved " + s + " in interest on your savings!");
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
