package me.kuraky.spamkiller;

import me.kuraky.spamkiller.check.CheckManager;
import me.kuraky.spamkiller.log.LogManager;
import me.kuraky.spamkiller.commands.CommandTabCompleter;
import me.kuraky.spamkiller.commands.SpamkillerCommand;
import me.kuraky.spamkiller.config.ConfigManager;
import me.kuraky.spamkiller.data.DataManager;
import me.kuraky.spamkiller.data.DataQueueTask;
import me.kuraky.spamkiller.listeners.ChatListener;
import me.kuraky.spamkiller.listeners.JoinListener;
import me.kuraky.spamkiller.listeners.QuitListener;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Spamkiller extends JavaPlugin {

    public static Spamkiller INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        String defaultPrefix = "§f[§bSK§f]";

        ConsoleCommandSender consoleCommandSender = getServer().getConsoleSender();

        //COMMAND
        PluginCommand spamkillerCommand = getCommand("spamkiller");
        if(spamkillerCommand != null) {
            spamkillerCommand.setExecutor(new SpamkillerCommand());
            spamkillerCommand.setTabCompleter(new CommandTabCompleter());
        }

        //LISTENERS
        consoleCommandSender.sendMessage(defaultPrefix + " Registering listeners...");
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new QuitListener(), this);

        //MANAGERS, HAVE TO BE IN THIS ORDER
        consoleCommandSender.sendMessage(defaultPrefix + " Loading checks...");
        CheckManager.init();

        consoleCommandSender.sendMessage(defaultPrefix + " Loading config...");
        ConfigManager.init();

        if(ConfigManager.getEnableLogs()) {
            consoleCommandSender.sendMessage(defaultPrefix + " Creating log file...");
            LogManager.init();
        }
        if(ConfigManager.getLogTime() > 0) {
            consoleCommandSender.sendMessage(defaultPrefix + " Deleting outdated log files...");
            LogManager.deleteOldLogs();
        }

        //TASK
        consoleCommandSender.sendMessage( defaultPrefix + " Starting data task...");
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new DataQueueTask(), ConfigManager.getDataTaskPeriod(), ConfigManager.getDataTaskPeriod());

        //ADDING ONLINE PLAYERS - IN CASE THE PLUGIN IS RELOADED, DATA WILL BE CREATED FOR ALL ONLINE PLAYERS, TO PREVENT ERRORS
        if(Bukkit.getOnlinePlayers().size() > 0) {
            consoleCommandSender.sendMessage(defaultPrefix + " Loading players...");
            for (Player player : Bukkit.getOnlinePlayers()) {
                DataManager.addPlayer(player.getUniqueId());
                DataManager.removeFromRemoveQueue(player.getUniqueId());
            }
        }

        consoleCommandSender.sendMessage(defaultPrefix + " §aDone!");
    }

    public void reload() {
        ConfigManager.init();
        LogManager.init();
    }
}
