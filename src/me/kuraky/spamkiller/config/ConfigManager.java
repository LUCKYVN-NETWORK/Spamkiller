package me.kuraky.spamkiller.config;

import me.kuraky.spamkiller.Spamkiller;
import me.kuraky.spamkiller.check.Check;
import me.kuraky.spamkiller.check.CheckManager;

public class ConfigManager {

    //GENERAL
    private static String PREFIX;
    private static int MINIMUM_MUTE_TIME;
    private static String MUTE_MESSAGE;
    private static String IN_MUTE_MESSAGE;
    private static String WARNING_MESSAGE;
    private static int MINIMUM_MODERATOR_NOTIFY_TIME;
    private static int MINIMUM_CONSOLE_NOTIFY_TIME;
    private static String NOTIFY_MESSAGE;
    private static boolean BLOCK_LAST_MESSAGE;
    private static long DATA_TASK_PERIOD;
    private static boolean ENABLE_LOGS;
    private static int LOG_TIME;

    //CHECKS
    private static String BLOCK_MESSAGE;
    private static boolean ACCOUNT_FOR_NICKNAMES;

    private static int LONG_SIMILARITY_ALLOWED_SIMILARITY;
    private static int LONG_SIMILARITY_MINIMUM_CHARACTERS;
    private static int LONG_SIMILARITY_WORDS_REQUIRED;

    private static int SHORT_SIMILARITY_ALLOWED_SIMILARITY;

    private static int REPEATED_CHAR_ALLOWED_CHARS;

    private static int CAPS_LOCK_ALLOWED_CHARS;

    private static int TYPING_SPEED_MINIMUM_LENGTH;
    private static int TYPING_SPEED_MAXIMUM_DIFFERENCE;
    private static int TYPING_SPEED_ALLOWED_TYPING_SPEED;

    private static int RAW_SPEED_ALLOWED_DELAY;

    private static int GROUP_SPAM_ALLOWED_SIMILARITY;

    private static int NON_ASCII_CHARACTERS_MINIMUM_LENGTH;
    private static int NON_ASCII_CHARACTERS_ALLOWED_PERCENTAGE;

    private static int LONG_WORDS_ALLOWED_WORD_LENGTH;
    private static boolean LONG_WORDS_IGNORE_LINKS;

    public static void init() {
        //GENERAL
        Spamkiller.INSTANCE.saveDefaultConfig();
        Spamkiller.INSTANCE.reloadConfig();
        PREFIX = getString("prefix");
        MINIMUM_MUTE_TIME = getIntNonNegative("minimum-mute-time")*1000;
        MUTE_MESSAGE = getString("mute-message");
        IN_MUTE_MESSAGE = getString("in-mute-message");
        WARNING_MESSAGE = getString("warning-message");
        MINIMUM_MODERATOR_NOTIFY_TIME = getIntNonNegative("minimum-moderator-notify-time")*1000;
        MINIMUM_CONSOLE_NOTIFY_TIME = getIntNonNegative("minimum-console-mute-time")*1000;
        NOTIFY_MESSAGE = getString("notify-message");
        BLOCK_LAST_MESSAGE = getBoolean("block-last-message");
        DATA_TASK_PERIOD = getLongPositive("data-task-period")*20;
        ENABLE_LOGS = getBoolean("enable-logs");
        LOG_TIME = getIntNonNegative("log-time");

        //CHECKS
        setWeights();
        toggleChecks();
        setMaximumMuteTimes();
        setBlockValues();

        BLOCK_MESSAGE = getString("block-message");
        ACCOUNT_FOR_NICKNAMES = getBoolean("account-for-nicknames");

        LONG_SIMILARITY_ALLOWED_SIMILARITY = getIntNonNegative("long-similarity.allowed-similarity");
        LONG_SIMILARITY_MINIMUM_CHARACTERS = getIntNonNegative("long-similarity.minimum-characters");
        LONG_SIMILARITY_WORDS_REQUIRED = getIntNonNegative("long-similarity.words-required");

        SHORT_SIMILARITY_ALLOWED_SIMILARITY = getIntNonNegative("short-similarity.allowed-similarity");

        REPEATED_CHAR_ALLOWED_CHARS = getIntNonNegative("repeated-char.allowed-chars");

        CAPS_LOCK_ALLOWED_CHARS = getIntNonNegative("caps-lock.allowed-chars");

        TYPING_SPEED_MINIMUM_LENGTH = getIntNonNegative("typing-speed.minimum-length");
        TYPING_SPEED_MAXIMUM_DIFFERENCE = getIntNonNegative("typing-speed.maximum-difference")*1000;
        TYPING_SPEED_ALLOWED_TYPING_SPEED = getIntNonNegative("typing-speed.allowed-typing-speed");

        RAW_SPEED_ALLOWED_DELAY = getIntNonNegative("raw-speed.allowed-delay");

        GROUP_SPAM_ALLOWED_SIMILARITY = getIntNonNegative("group-spam.allowed-similarity");

        NON_ASCII_CHARACTERS_MINIMUM_LENGTH = getIntNonNegative("non-ascii-characters.minimum-length");
        NON_ASCII_CHARACTERS_ALLOWED_PERCENTAGE = getIntNonNegative("non-ascii-characters.allowed-percentage");

        LONG_WORDS_ALLOWED_WORD_LENGTH = getIntNonNegative("long-words.allowed-word-length");
        LONG_WORDS_IGNORE_LINKS = getBoolean("long-words.ignore-links");
    }

    private static void setWeights() {
        for(Check check : CheckManager.getChecks()) {
            check.setWeight(getIntNonNegative(convertNameToPath(check.getName()) + ".weight"));
        }
    }

    private static void toggleChecks() {
        for(Check check : CheckManager.getChecks()) {
            check.setEnabled(getBoolean(convertNameToPath(check.getName()) + ".enabled"));
        }
    }

    private static void setMaximumMuteTimes() {
        for(Check check : CheckManager.getChecks()) {
            check.setMaximumMuteTime(getIntNonNegative(convertNameToPath(check.getName()) + ".maximum-mute-time")*1000);
        }
    }

    private static void setBlockValues() {
        for(Check check : CheckManager.getChecks()) {
            check.setBlockInstead(getBoolean(convertNameToPath(check.getName()) + ".block-instead"));
            if(check.getName().equals("PerfectDelay")) check.setBlockInstead(false);
        }
    }

    private static String convertNameToPath(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name.charAt(0));
        for(int i = 1; i < name.length(); i++) {
            if(Character.isUpperCase(name.charAt(i))) stringBuilder.append("-");
            stringBuilder.append(name.charAt(i));
        }

        return stringBuilder.toString().toLowerCase();
    }

    //GENERAL
    public static String getPrefix() {
        return PREFIX;
    }

    public static int getMinimumMuteTime() {
        return MINIMUM_MUTE_TIME;
    }

    public static String getMuteMessage() {
        return MUTE_MESSAGE;
    }

    public static String getInMuteMessage() {
        return IN_MUTE_MESSAGE;
    }

    public static String getWarningMessage() {
        return WARNING_MESSAGE;
    }

    public static int getMinimumModeratorNotifyTime() {
        return MINIMUM_MODERATOR_NOTIFY_TIME;
    }

    public static int getMinimumConsoleNotifyTime() {
        return MINIMUM_CONSOLE_NOTIFY_TIME;
    }

    public static String getNotifyMessage() {
        return NOTIFY_MESSAGE;
    }

    public static boolean getBlockLastMessage() {
        return BLOCK_LAST_MESSAGE;
    }

    public static long getDataTaskPeriod() {
        return DATA_TASK_PERIOD;
    }

    public static boolean getEnableLogs() {
        return ENABLE_LOGS;
    }

    public static int getLogTime() {
        return LOG_TIME;
    }

    //CHECKS
    public static String getBlockMessage() {
        return BLOCK_MESSAGE;
    }

    public static boolean getAccountForNicknames() {
        return  ACCOUNT_FOR_NICKNAMES;
    }

    public static int getLongSimilarityAllowedSimilarity() {
        return LONG_SIMILARITY_ALLOWED_SIMILARITY;
    }

    public static int getLongSimilarityMinimumCharacters() {
        return LONG_SIMILARITY_MINIMUM_CHARACTERS;
    }

    public static int getLongSimilarityWordsRequired() {
        return LONG_SIMILARITY_WORDS_REQUIRED;
    }

    public static int getShortSimilarityAllowedSimilarity() {
        return SHORT_SIMILARITY_ALLOWED_SIMILARITY;
    }

    public static int getRepeatedCharAllowedChars() {
        return REPEATED_CHAR_ALLOWED_CHARS;
    }

    public static int getCapsLockAllowedChars() {
        return CAPS_LOCK_ALLOWED_CHARS;
    }

    public static int getTypingSpeedMinimumLength() {
        return TYPING_SPEED_MINIMUM_LENGTH;
    }

    public static int getTypingSpeedMaximumDifference() {
        return TYPING_SPEED_MAXIMUM_DIFFERENCE;
    }

    public static int getTypingSpeedAllowedTypingSpeed() {
        return TYPING_SPEED_ALLOWED_TYPING_SPEED;
    }

    public static int getRawSpeedAllowedDelay() {
        return RAW_SPEED_ALLOWED_DELAY;
    }

    public static int getGroupSpamAllowedSimilarity() {
        return GROUP_SPAM_ALLOWED_SIMILARITY;
    }

    public static int getNonAsciiCharactersMinimumLength() {
        return NON_ASCII_CHARACTERS_MINIMUM_LENGTH;
    }

    public static int getNonAsciiCharactersAllowedPercentage() {
        return Math.min(100, NON_ASCII_CHARACTERS_ALLOWED_PERCENTAGE);
    }

    public static int getLongWordsAllowedWordLength() {
        return LONG_WORDS_ALLOWED_WORD_LENGTH;
    }

    public static boolean getLongWordsIgnoreLinks() {
        return LONG_WORDS_IGNORE_LINKS;
    }

    public static String replacePlaceholders(String message, Integer time, String playerName) {
        String newMessage = message;

        newMessage = newMessage.replace("%prefix%", PREFIX);
        if(time != null) {
            String timeString = time.toString();
            newMessage = newMessage.replace("%time%", formatTime(timeString));
        }
        if(playerName != null) newMessage = newMessage.replace("%player%", playerName);

        return newMessage;
    }

    public static String formatTime(String milliTime) {
        StringBuilder timeFormat = new StringBuilder();

        while(milliTime.length() - timeFormat.length() > 3) timeFormat.append(milliTime.charAt(timeFormat.length()));
        if(timeFormat.length() > 0)  {
            timeFormat.append(".");
            while(milliTime.length() - timeFormat.length() > 0) timeFormat.append(milliTime.charAt(timeFormat.length() - 1));
        }
        else {
            if(milliTime.equals("0")) timeFormat.append("0");
            else {
                timeFormat.append("0.");
                while (milliTime.length() - timeFormat.length() > -1)
                    timeFormat.append(milliTime.charAt(timeFormat.length() - 2));
            }
        }
        return  timeFormat.toString();
    }

    private static String getString(String path) {
        return Spamkiller.INSTANCE.getConfig().getString(path);
    }

    private static int getIntNonNegative(String path) {
        int value = Spamkiller.INSTANCE.getConfig().getInt(path);
        return Math.max(0, value);
    }

    private static boolean getBoolean(String path) {
        return Spamkiller.INSTANCE.getConfig().getBoolean(path);
    }

    private static long getLongPositive(String path) {
        long value = Spamkiller.INSTANCE.getConfig().getLong(path);
        return Math.max(1, value);
    }
}
