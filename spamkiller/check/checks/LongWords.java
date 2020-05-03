package me.kuraky.spamkiller.check.checks;

import me.kuraky.spamkiller.check.Check;
import me.kuraky.spamkiller.config.ConfigManager;
import me.kuraky.spamkiller.data.PlayerData;

public class LongWords extends Check {

    public LongWords() {
        super("LongWords");
    }

    @Override
    public int onChat(String message, PlayerData playerData) {
        int mute = 0;

        String[] splitted = message.split("\\s+");
        int allowedWordLength = ConfigManager.getLongWordsAllowedWordLength();

        for(String part : splitted) {
            if(ConfigManager.getLongWordsIgnoreLinks() && (part.startsWith("https://") || part.startsWith("http://"))) continue;
            int length = part.length();
            if(length > allowedWordLength) {
                mute += Math.pow(length - allowedWordLength, 1.1d)*500;
            }
        }

        return mute;
    }
}
