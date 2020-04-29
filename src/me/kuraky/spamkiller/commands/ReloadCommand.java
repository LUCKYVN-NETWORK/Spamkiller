package me.kuraky.spamkiller.commands;

import me.kuraky.spamkiller.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("spamkiller.reload")) {
            ConfigManager.init();

            String message = ChatColor.translateAlternateColorCodes('&', ConfigManager.getPrefix() + " Config reloaded");

            if(commandSender instanceof Player) {
                commandSender.sendMessage(message);
            }
            Bukkit.getConsoleSender().sendMessage(message);

            return true;
        }
        return false;
    }
}
