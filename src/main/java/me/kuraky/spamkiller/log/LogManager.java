package me.kuraky.spamkiller.log;

import me.kuraky.spamkiller.Spamkiller;
import me.kuraky.spamkiller.config.ConfigManager;
import org.bukkit.Bukkit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LogManager {

    private static File activeLogFile;
    private static Spamkiller spamkiller;
    private static File logsDirectory;

    public static void init() {
        spamkiller = Spamkiller.INSTANCE;

        if(!ConfigManager.getEnableLogs()) return;

        String path = spamkiller.getDataFolder().getPath() + "/logs";

        logsDirectory = new File(path);

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
                        bufferedWriter.write("Loaded with " + players + " players online.");
                        bufferedWriter.newLine();
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        bufferedWriter.close();
                    }
                } else if (!activeLogFile.exists()) spamkiller.getLogger().warning("Couldn't create log file");
            } catch (IOException e) {
                e.printStackTrace();
                spamkiller.getLogger().warning("Invalid file path, couldn't create a log file");
            }
        }
    }

    public static void deleteOldLogs() {
        if(logsDirectory == null || !logsDirectory.exists() ||!logsDirectory.isDirectory()) return;
        File[] files = logsDirectory.listFiles();
        if(files != null) {
            String format = "yyyy-MM-dd_HH-mm-ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

            long outdatedTime = System.currentTimeMillis() - ConfigManager.getLogTime()*24*60*60*1000;
            Date outdatedDate = new Date(outdatedTime);

            ArrayList<File> outdated = new ArrayList<>();
            int count = 0;

            for (File file : files) {
                String fileName = file.getName();
                if(fileName.startsWith("log_") && fileName.endsWith(".txt")) {
                    fileName = fileName.substring(4, fileName.length() - 4);
                    try {
                        Date fileDate = simpleDateFormat.parse(fileName);
                        if(fileDate.before(outdatedDate)) {
                            outdated.add(file);
                            count++;
                        }
                    }
                    //not important to handle, exception will be thrown if a file name is not of the proper format,
                    //which is intended, that file will simply not be deleted
                    catch (ParseException ignored) {
                    }
                }
            }
            int cantDeleteCount = 0;
            for(File outdatedFile : outdated) {
                if(!outdatedFile.delete()) cantDeleteCount++;
            }
            int deletedCount = count - cantDeleteCount;
            spamkiller.getLogger().info("Deleted " + (count - cantDeleteCount) + " outdated log file"
                    + (deletedCount == 1 ? "" : "s"));
            if(cantDeleteCount > 0) spamkiller.getLogger().warning("Couldn't delete " + cantDeleteCount
                    + " outdated log file" + (cantDeleteCount == 1 ? "" : "s"));
        }
    }

    public static void log(ArrayList<String> messages) {
        if(activeLogFile == null || !activeLogFile.exists() || !activeLogFile.isFile()) {
            spamkiller.getLogger().info("The log file was not located, despite logs being enabled in the config, "
                    + "attempting to create a new log file.");
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
