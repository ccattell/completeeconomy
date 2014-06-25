package me.ccattell.plugins.cnu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.ccattell.plugins.cnu.commands.BankCommand;
import me.ccattell.plugins.cnu.commands.SillyCommand;
import me.ccattell.plugins.cnu.commands.CashCommand;
import me.ccattell.plugins.cnu.commands.GamemodeCommand;
import me.ccattell.plugins.cnu.commands.JobsCommand;
import me.ccattell.plugins.cnu.commands.PayCommand;
import me.ccattell.plugins.cnu.commands.ShopCommand;
import me.ccattell.plugins.cnu.commands.XPBankCommand;
import me.ccattell.plugins.cnu.commands.AliasesCommand;
import me.ccattell.plugins.cnu.database.Database;
import me.ccattell.plugins.cnu.database.InitMySQL;
import me.ccattell.plugins.cnu.database.InitSQLite;
import me.ccattell.plugins.cnu.listeners.BreakListener;
import me.ccattell.plugins.cnu.listeners.DeathListener;
import me.ccattell.plugins.cnu.listeners.JoinListener;
import me.ccattell.plugins.cnu.listeners.ItemFrameListener;
import me.ccattell.plugins.cnu.listeners.RawCommandListener;
import me.ccattell.plugins.cnu.runnables.BreakRunnable;
import me.ccattell.plugins.cnu.runnables.BreakData;
import me.ccattell.plugins.cnu.utilities.CustomConfigs;
import me.ccattell.plugins.cnu.utilities.GiveInterest;
import me.ccattell.plugins.cnu.utilities.VersionCheck;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CompleteNovusUtilities extends JavaPlugin {

    public static CompleteNovusUtilities plugin;
    Database service = Database.getInstance();
    public PluginDescriptionFile pdfFile;
    public HashMap<String, String> trackPlayers = new HashMap<String, String>();
    public ConsoleCommandSender console;
    public String dbtype;
    public PluginManager pm = Bukkit.getServer().getPluginManager();
    public CustomConfigs configs;
    protected VersionCheck versionCheck;
    public String pluginName = ChatColor.DARK_PURPLE + "[Complete Novus Utilities]" + ChatColor.RESET + " ";
    // This is the break queue - data is added to it for processing in another thread
    public List<BreakData> breakQueue = new ArrayList<BreakData>();

    @Override
    public void onDisable() {
        // TODO: Place any custom disable code here.
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        pdfFile = getDescription();

        plugin = this;
        console = getServer().getConsoleSender();
        String UpdateChannel = plugin.getConfig().getString("System.UpdateChannel");
        if (!UpdateChannel.equalsIgnoreCase("none")) {
            console.sendMessage(plugin.pluginName + "Update Channel: " + UpdateChannel);
            this.versionCheck = new VersionCheck(this, "http://dev.bukkit.org/server-mods/complete-economy/files.rss");
            if (this.versionCheck.updateNeeded()) {
                String update = this.versionCheck.getUpdate();
                if (update.equalsIgnoreCase("none")) {
                    console.sendMessage(pluginName + "There are no files to test in your channel");
                } else if (update.equalsIgnoreCase("no")) {
                    console.sendMessage(pluginName + "Congratulations, you are running the latest version of CompleteNovusUtilities!");
                } else if (update.equalsIgnoreCase("yes")) {
                    console.sendMessage(pluginName + ChatColor.GOLD + "A new version is available: " + ChatColor.DARK_GREEN + this.versionCheck.getVersion() + ChatColor.RESET);
                    console.sendMessage(pluginName + ChatColor.GOLD + "Get it from: " + ChatColor.DARK_GREEN + this.versionCheck.getLink() + ChatColor.RESET);
                } else if (update.equalsIgnoreCase("dev")) {
                    console.sendMessage(pluginName + "You are using an unreleased version of the plugin!");
                }
            }
        }
        try {
            if (getConfig().getString("System.Database.Type").equals("sqlite")) {
                console.sendMessage(pluginName + "Loading SQLite Database");
                String path = getDataFolder() + File.separator + "CompleteNovusUtilities.db";
                service.setConnection(path);
                new InitSQLite().initSQLite();
            } else {
                // mysql
                console.sendMessage(pluginName + "Loading MySQL Database");
                service.setConnection();
                new InitMySQL().initMYSQL();
            }
        } catch (Exception e) {
            console.sendMessage(pluginName + ChatColor.GOLD + "Connection and Tables Error: " + e + ChatColor.RESET);
        }
        configs = new CustomConfigs(this);
        configs.copyDefaultConfigs();
        console.sendMessage(pluginName + "Loading config files");
        configs.loadCustomConfigs();
        pm.registerEvents(new JoinListener(this), this);
        pm.registerEvents(new DeathListener(this), this);
        pm.registerEvents(new BreakListener(this), this);
        pm.registerEvents(new ItemFrameListener(this), this);
        pm.registerEvents(new RawCommandListener(this), this);
        getCommand("alias").setExecutor(new AliasesCommand());
        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("gm").setExecutor(new GamemodeCommand());
        getCommand("survival").setExecutor(new GamemodeCommand());
        getCommand("creative").setExecutor(new GamemodeCommand());
        getCommand("adventure").setExecutor(new GamemodeCommand());
        getCommand("0").setExecutor(new GamemodeCommand());
        getCommand("1").setExecutor(new GamemodeCommand());
        getCommand("2").setExecutor(new GamemodeCommand());
        getCommand("poke").setExecutor(new SillyCommand());
        getCommand("ping").setExecutor(new SillyCommand());
        getCommand("marco").setExecutor(new SillyCommand());
        getCommand("cash").setExecutor(new CashCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("bank").setExecutor(new BankCommand());
        getCommand("xpbank").setExecutor(new XPBankCommand());
        getCommand("jobs").setExecutor(new JobsCommand());
        getCommand("shop").setExecutor(new ShopCommand());
        new GiveInterest().interest();
        // start a repeating task to process the mining/break queue
        // it starts it first round of processing 15 seconds after the plugin is enabled
        // then it processes the current break queue every 60 seconds thereafter
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new BreakRunnable(this), 300L, 1200L);
    }

    public List<BreakData> getBreakQueue() {
        return breakQueue;
    }
}
