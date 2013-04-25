package me.ccattell.plugins.completeeconomy.utilities;

import me.ccattell.plugins.completeeconomy.CompleteEconomy;

public class GiveInterest {

    // add class constructor
    public void GiveInterest() {
    }

    public void interest() {

        boolean enabled = CompleteEconomy.plugin.getConfig().getBoolean("System.Interest.Enabled");
        boolean online = CompleteEconomy.plugin.getConfig().getBoolean("System.Interest.Online");
        boolean announce = CompleteEconomy.plugin.getConfig().getBoolean("System.Announce.Enabled");
        long interval = CompleteEconomy.plugin.getConfig().getInt("System.Interval.Seconds") * 20;
        float cutoff = CompleteEconomy.plugin.getConfig().getInt("System.Amount.Cutoff");
        float percent = CompleteEconomy.plugin.getConfig().getInt("System.Amount.Percent");

        CompleteEconomy.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(CompleteEconomy.plugin, new Runnable() {
            @Override
            public void run() {
                CompleteEconomy.plugin.getServer().getConsoleSender().sendMessage("Give Interest");

            }
        }, 60L, interval);
    }
}
