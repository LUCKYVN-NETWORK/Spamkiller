package me.kuraky.spamkiller.check.checks;

import me.kuraky.spamkiller.check.Check;
import me.kuraky.spamkiller.config.ConfigManager;
import me.kuraky.spamkiller.data.PlayerData;

public class NonAsciiCharacters extends Check {

    public NonAsciiCharacters() {
        super("NonAsciiCharacters");
    }

    @Override
    public int onChat(String message, PlayerData playerData) {
        int mute = 0;
        int length = message.length();

        int minimumLength = ConfigManager.getNonAsciiCharactersMinimumLength();
        if(length > minimumLength) {
            int nonAsciiAmount = 0;

            for (int i = 0; i < message.length(); i++) {
                if(message.charAt(i) > 127) nonAsciiAmount++;
            }

            float percentage = (float)nonAsciiAmount/length*100;
            int allowedPercentage = ConfigManager.getNonAsciiCharactersAllowedPercentage();
            int nonAsciiCharactersViolations = playerData.getNonAsciiCharactersViolations();

            if(percentage > allowedPercentage) {
                if(nonAsciiCharactersViolations > 0) {
                    mute = (int) ((percentage / allowedPercentage) * (1 + length / 100f) * 2000);
                    playerData.setNonAsciiCharactersViolations(0);
                }
                else playerData.setNonAsciiCharactersViolations(nonAsciiCharactersViolations + 5);
            }
        }

        return mute;
    }
}
