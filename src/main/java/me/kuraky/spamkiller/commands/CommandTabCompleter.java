package me.kuraky.spamkiller.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {
    private List<String> subcommands = Arrays.asList("set", "reload");

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("spamkiller.reload") || commandSender.hasPermission("spamkiller.set")) {
            if(strings.length == 1) {
                ArrayList<String> suggestions = new ArrayList<>();
                StringUtil.copyPartialMatches(strings[0], subcommands, suggestions);
                Collections.sort(suggestions);

                return suggestions;
            }
            else if(strings[0].equals("reload")) return new ArrayList<>();
            else if(strings[0].equals("set") && strings.length > 2) return new ArrayList<>();
        }
        return null;
    }
}
