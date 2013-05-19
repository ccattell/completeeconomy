package me.ccattell.plugins.completeeconomy.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import me.ccattell.plugins.completeeconomy.CompleteEconomy;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Charlie
 */
public class CECustomConfigs {

    public ConsoleCommandSender console;
    private CompleteEconomy plugin;
    public FileConfiguration bankConfig = null;
    public FileConfiguration jobConfig = null;
    public FileConfiguration shopConfig = null;
    public FileConfiguration jobList = null;
    public FileConfiguration kitList = null;
    public FileConfiguration shopList = null;
    public FileConfiguration blockList = null;
    public FileConfiguration titleList = null;
    public FileConfiguration treasureList = null;
    private File bankConfigFile = null;
    private File jobConfigFile = null;
    private File shopConfigFile = null;
    private File jobListFile = null;
    private File kitListFile = null;
    private File shopListFile = null;
    private File blockListFile = null;
    private File titleListFile = null;
    private File treasureListFile = null;

    public CECustomConfigs(CompleteEconomy plugin) {
        this.plugin = plugin;
    }

    public void copyDefaultConfigs() {
        File listsDir = new File(plugin.getDataFolder() + File.separator + "lists");
        if (!listsDir.exists()) {
            boolean result = listsDir.mkdir();
            if (result) {
                listsDir.setWritable(true);
                listsDir.setExecutable(true);
            }
        }
        copy(plugin.getDataFolder() + File.separator + "bankConfig.yml", plugin.getResource("bankConfig.yml"));
        copy(plugin.getDataFolder() + File.separator + "jobConfig.yml", plugin.getResource("jobConfig.yml"));
        copy(plugin.getDataFolder() + File.separator + "shopConfig.yml", plugin.getResource("shopConfig.yml"));
        copy(plugin.getDataFolder() + File.separator + "lists" + File.separator + "jobs.yml", plugin.getResource("jobs.yml"));
        copy(plugin.getDataFolder() + File.separator + "lists" + File.separator + "kits.yml", plugin.getResource("kits.yml"));
        copy(plugin.getDataFolder() + File.separator + "lists" + File.separator + "shops.yml", plugin.getResource("shops.yml"));
        copy(plugin.getDataFolder() + File.separator + "lists" + File.separator + "blocks.yml", plugin.getResource("blocks.yml"));
        copy(plugin.getDataFolder() + File.separator + "lists" + File.separator + "titles.yml", plugin.getResource("titles.yml"));
        copy(plugin.getDataFolder() + File.separator + "lists" + File.separator + "treasures.yml", plugin.getResource("treasures.yml"));
    }

    public void loadCustomConfigs() {
        this.bankConfigFile = new File(plugin.getDataFolder(), "bankConfig.yml");
        this.bankConfig = YamlConfiguration.loadConfiguration(bankConfigFile);
        this.jobConfigFile = new File(plugin.getDataFolder(), "jobConfig.yml");
        this.jobConfig = YamlConfiguration.loadConfiguration(jobConfigFile);
        this.jobConfigFile = new File(plugin.getDataFolder(), "shopConfig.yml");
        this.shopConfig = YamlConfiguration.loadConfiguration(shopConfigFile);
        this.jobListFile = new File(plugin.getDataFolder(), "lists" + File.separator + "jobs.yml");
        this.jobList = YamlConfiguration.loadConfiguration(jobListFile);
        this.kitListFile = new File(plugin.getDataFolder(), "lists" + File.separator + "kits.yml");
        this.kitList = YamlConfiguration.loadConfiguration(kitListFile);
        this.shopListFile = new File(plugin.getDataFolder(), "lists" + File.separator + "shops.yml");
        this.shopList = YamlConfiguration.loadConfiguration(shopListFile);
        this.blockListFile = new File(plugin.getDataFolder(), "lists" + File.separator + "blocks.yml");
        this.blockList = YamlConfiguration.loadConfiguration(blockListFile);
        this.titleListFile = new File(plugin.getDataFolder(), "lists" + File.separator + "titles.yml");
        this.titleList = YamlConfiguration.loadConfiguration(titleListFile);
        this.treasureListFile = new File(plugin.getDataFolder(), "lists" + File.separator + "treasures.yml");
        this.treasureList = YamlConfiguration.loadConfiguration(treasureListFile);
    }

    public FileConfiguration getBankConfig() {
        return bankConfig;
    }

    public FileConfiguration getJobConfig() {
        return jobConfig;
    }

    public FileConfiguration getShopConfig() {
        return shopConfig;
    }

    public FileConfiguration getJobList() {
        return jobList;
    }

    public FileConfiguration getKitList() {
        return kitList;
    }

    public FileConfiguration getShopList() {
        return shopList;
    }

    public FileConfiguration getBlockList() {
        return blockList;
    }

    public FileConfiguration getTitleList() {
        return titleList;
    }

    public FileConfiguration getTreasureList() {
        return treasureList;
    }

    /**
     * Copies a custom config file to the Complete Economy plugin directory if
     * it is not present.
     *
     * @param filepath the path to the file to write to
     * @param in the input file to read from
     * @return a File
     */
    public File copy(String filepath, InputStream in) {
        File file = new File(filepath);
        if (!file.exists()) {
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                try {
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } catch (IOException io) {
                    console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Could not save the file (" + file.toString() + ")." + ChatColor.RESET);
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e) {
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                console.sendMessage(plugin.pluginName + ChatColor.GOLD + "File not found." + ChatColor.RESET);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return file;
    }
}
