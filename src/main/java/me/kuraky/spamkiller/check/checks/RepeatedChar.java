package me.kuraky.spamkiller.check.checks;

import me.kuraky.spamkiller.check.Check;
import me.kuraky.spamkiller.config.ConfigManager;
import me.kuraky.spamkiller.data.PlayerData;

public class RepeatedChar extends Check {

    public RepeatedChar() {
        super("RepeatedChar");
    }

    @Override
    public int onChat(String message, PlayerData playerData) {
        int mute = 0;
        int streak = 1;

        int allowedChars = ConfigManager.getRepeatedCharAllowedChars();

        char previousChar = message.charAt(0);

        for(int i = 1; i < message.length(); i++) {
            char currentChar = message.charAt(i);

            if(currentChar == previousChar) streak++;
            else if(streak > allowedChars) {
                mute += Math.pow(streak - allowedChars, 1.3d)*1000;
                streak = 1;
            }
            previousChar = currentChar;
        }
        if(streak > allowedChars) mute += Math.pow(streak - allowedChars, 1.2d)*200;

        return mute;
    }
}
