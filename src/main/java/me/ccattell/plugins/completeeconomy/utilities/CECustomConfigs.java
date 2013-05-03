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
    public FileConfiguration kitConfig = null;
    public FileConfiguration shopConfig = null;
    public FileConfiguration skillConfig = null;
    public FileConfiguration titleConfig = null;
    private File bankConfigFile = null;
    private File jobConfigFile = null;
    private File kitConfigFile = null;
    private File shopConfigFile = null;
    private File skillConfigFile = null;
    private File titleConfigFile = null;

    public CECustomConfigs(CompleteEconomy plugin) {
        this.plugin = plugin;
    }

    public void copyDefaultConfigs() {
        copy(plugin.getDataFolder() + File.separator + "bankConfig.yml", plugin.getResource("bankConfig.yml"));
        copy(plugin.getDataFolder() + File.separator + "jobConfig.yml", plugin.getResource("jobConfig.yml"));
        copy(plugin.getDataFolder() + File.separator + "kitConfig.yml", plugin.getResource("kitConfig.yml"));
        copy(plugin.getDataFolder() + File.separator + "shopConfig.yml", plugin.getResource("shopConfig.yml"));
        copy(plugin.getDataFolder() + File.separator + "skillConfig.yml", plugin.getResource("skillConfig.yml"));
        copy(plugin.getDataFolder() + File.separator + "titleConfig.yml", plugin.getResource("titleConfig.yml"));
    }

    public void loadCustomConfigs() {
        this.bankConfigFile = new File(plugin.getDataFolder(), "bankConfig.yml");
        this.bankConfig = YamlConfiguration.loadConfiguration(bankConfigFile);
        this.jobConfigFile = new File(plugin.getDataFolder(), "jobConfig.yml");
        this.jobConfig = YamlConfiguration.loadConfiguration(jobConfigFile);
        this.kitConfigFile = new File(plugin.getDataFolder(), "kitConfig.yml");
        this.kitConfig = YamlConfiguration.loadConfiguration(kitConfigFile);
        this.shopConfigFile = new File(plugin.getDataFolder(), "shopConfig.yml");
        this.shopConfig = YamlConfiguration.loadConfiguration(shopConfigFile);
        this.skillConfigFile = new File(plugin.getDataFolder(), "skillConfig.yml");
        this.skillConfig = YamlConfiguration.loadConfiguration(skillConfigFile);
        this.titleConfigFile = new File(plugin.getDataFolder(), "titleConfig.yml");
        this.titleConfig = YamlConfiguration.loadConfiguration(titleConfigFile);
    }

    public FileConfiguration getBankConfig() {
        return bankConfig;
    }

    public FileConfiguration getJobConfig() {
        return jobConfig;
    }

    public FileConfiguration getKitConfig() {
        return kitConfig;
    }

    public FileConfiguration getShopConfig() {
        return shopConfig;
    }

    public FileConfiguration getSkillConfig() {
        return skillConfig;
    }

    public FileConfiguration getTitleConfig() {
        return titleConfig;
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
