package me.kuraky.spamkiller.commands;

import me.kuraky.spamkiller.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class SpamkillerCommand implements CommandExecutor {

    private ReloadCommand reloadCommand;
    private SetCommand setCommand;

    public SpamkillerCommand() {
        reloadCommand = new ReloadCommand();
        setCommand = new SetCommand();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length > 0) {
            boolean perm = true;
            String[] newArgs = Arrays.copyOfRange(strings, 1, strings.length);
            switch(strings[0].toLowerCase()) {
                case "reload":
                    perm = reloadCommand.onCommand(commandSender, command, s, newArgs);
                    break;
                case "set":
                    perm = setCommand.onCommand(commandSender, command, s, newArgs);
                    break;
                default:
                    if(commandSender.hasPermission("spamkiller.reload") || commandSender.hasPermission("spamkiller.set")) {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getPrefix() + " §cInvalid subcommand"));
                        displayHelpMessage(commandSender);
                    }
                    break;
            }
            if(!perm) commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getPrefix() + " §cNo permission"));
        }
        else {
            if(commandSender.hasPermission("spamkiller.reload") || commandSender.hasPermission("spamkiller.set")) {
                displayHelpMessage(commandSender);
            }
        }
        return true;
    }

    private void displayHelpMessage(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getPrefix() + " &fHelp:"));
        if (commandSender.hasPermission("spamkiller.reload")) {
            commandSender.sendMessage("§f/spamkiller reload - §fReloads the config");
        }
        if(commandSender.hasPermission("spamkiller.set")) {
            commandSender.sendMessage("§f/spamkiller set <player> <time> - §fSets player's mute time");
        }
    }
}
