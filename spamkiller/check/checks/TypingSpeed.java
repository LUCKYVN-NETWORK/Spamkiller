package me.kuraky.spamkiller.check.checks;

import me.kuraky.spamkiller.check.Check;
import me.kuraky.spamkiller.config.ConfigManager;
import me.kuraky.spamkiller.data.PlayerData;

import java.util.LinkedList;

public class TypingSpeed extends Check {

    public TypingSpeed() {
        super("TypingSpeed");
    }

    @Override
    public int onChat(String message, PlayerData playerData) {
        int mute = 0;
        int length = message.length();

        if(length >= ConfigManager.getTypingSpeedMinimumLength()) {
            LinkedList<Long> messageTimes = playerData.getMessageTimes();

            if(messageTimes.size() >= 2) {
                int delay = (int) (messageTimes.get(0) - messageTimes.get(1));

                if(delay <= ConfigManager.getTypingSpeedMaximumDifference()) {
                    float speed = (float)length/(delay/1000f);

                    int typingSpeedViolations = playerData.getTypingSpeedViolations();
                    int allowedTypingSpeed = ConfigManager.getTypingSpeedAllowedTypingSpeed();

                    if(speed > allowedTypingSpeed) {
                        if (typingSpeedViolations > 0) {
                            mute = (int) (((speed - allowedTypingSpeed)/allowedTypingSpeed)*1000);
                            playerData.setTypingSpeedViolations(0);
                        }
                        else playerData.setTypingSpeedViolations(typingSpeedViolations + 5);
                    }
                }
            }
        }
        return mute;
    }
}
