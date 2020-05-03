package me.kuraky.spamkiller.check.checks;

import me.kuraky.spamkiller.check.Check;
import me.kuraky.spamkiller.config.ConfigManager;
import me.kuraky.spamkiller.data.PlayerData;

public class CapsLock extends Check {

    public CapsLock() {
        super("CapsLock");
    }

    @Override
    public int onChat(String message, PlayerData playerData) {
        int length = message.length();
        int amountOfUppercase = 0;

        for(int i = 0; i < length; i++) {
            if(Character.isUpperCase(message.charAt(i))) amountOfUppercase++;
        }

        if(amountOfUppercase > ConfigManager.getCapsLockAllowedChars()) {
            int diff = amountOfUppercase - ConfigManager.getCapsLockAllowedChars();
            int mute = diff*500;
            float percentage = (float)diff/length;
            if(percentage > 0.5) mute *= percentage*2;

            return mute;
        }
        else return 0;
    }
}
