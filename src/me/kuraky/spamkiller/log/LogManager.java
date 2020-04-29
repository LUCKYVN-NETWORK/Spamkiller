package me.kuraky.spamkiller.log;

import me.kuraky.spamkiller.Spamkiller;
import org.bukkit.Bukkit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LogManager {

    private static File activeLogFile;

    public static void init() {
        Spamkiller spamkiller = Spamkiller.INSTANCE;

        String path = spamkiller.getDataFolder() + "/logs";

        File logsDirectory = new File(path);

        if(!logsDirectory.mkdir() && (!logsDirectory.exists() || !logsDirectory.isDirectory())) {
            spamkiller.getLogger().warning("Logs directory couldn't be created");
        }
        if(logsDirectory.exists() && logsDirectory.isDirectory()) {
            String format = "yyyy-MM-dd_HH-mm-ss";
            DateFormat dateFormat = new SimpleDateFormat(format);

            activeLogFile = new File(path + "/log_" + dateFormat.format(Calendar.getInstance().getTime()) + ".txt");

            try {
                if (activeLogFile.createNewFile()) {
                    int players = Bukkit.getOnlinePlayers().size();

                    if(players > 0) {
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(activeLogFile));
                        bufferedWriter.write("Loaded with " + players + " players online. Plugin might have been reloaded");
                        bufferedWriter.newLine();
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        bufferedWriter.close();
                    }
                } else if (!activeLogFile.exists()) spamkiller.getLogger().warning("Couldn't create log file");
            } catch (IOException e) {
                spamkiller.getLogger().warning("Invalid file path, couldn't create a log file");
            }
        }
    }

    public static void log(ArrayList<String> messages) {
        if(activeLogFile == null || !activeLogFile.exists() || !activeLogFile.isFile()) {
            Spamkiller spamkiller = Spamkiller.INSTANCE;
            spamkiller.getLogger().info("The log file was not located, despite logs being enabled in the config, " +
                    "attempting to create a new log file.");
            spamkiller.getLogger().info("Set enable-logs in the config file to false to disable this");
            init();
        }

        if(messages.size() > 0 && activeLogFile.exists() && activeLogFile.isFile()) {
            String format = "dd-HH:mm:ss";
            DateFormat dateFormat = new SimpleDateFormat(format);

            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(activeLogFile, true));
                bufferedWriter.write("[");
                bufferedWriter.write(dateFormat.format(Calendar.getInstance().getTime()));
                bufferedWriter.write("] ");

                for(String message : messages) {
                    bufferedWriter.write(message);
                    bufferedWriter.newLine();
                }

                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException ignored) {

            }
        }
    }
}
