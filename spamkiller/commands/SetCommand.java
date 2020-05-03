package me.kuraky.spamkiller.commands;

import me.kuraky.spamkiller.config.ConfigManager;
import me.kuraky.spamkiller.data.DataManager;
import me.kuraky.spamkiller.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("spamkiller.set")) {
            String prefix = ChatColor.translateAlternateColorCodes('&', ConfigManager.getPrefix());

            if(strings.length == 2) {
                try {
                    int time = Integer.parseInt(strings[1]);
                    Player player = Bukkit.getPlayer(strings[0]);

                    if(player != null) {
                        PlayerData playerData = DataManager.getDataFromUUID(player.getUniqueId());

                        if(time*1000 < ConfigManager.getMinimumMuteTime()) time = 0;
                        playerData.setMuteTime(time*1000);

                        commandSender.sendMessage(prefix + " §fSet §c" + player.getName()
                                + "§f's mute time to §c" + ConfigManager.formatTime(String.valueOf(playerData.getMuteTime())) + " §fseconds");
                    }
                    else commandSender.sendMessage(prefix + " §c" + strings[0] + " §cis not a valid player");

                }
                catch (NumberFormatException exception) {
                    commandSender.sendMessage(prefix + " §c" + strings[1] + " §cis not a valid number");
                }
            }
            else {
                if(strings.length > 2) commandSender.sendMessage(prefix + " §cToo many arguments");
                else commandSender.sendMessage(prefix + " §cNot enough arguments");
            }

            return true;
        }
        return false;
    }
}
