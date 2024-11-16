package me.kuraky.spamkiller.check.checks;

import me.kuraky.spamkiller.check.Check;
import me.kuraky.spamkiller.config.ConfigManager;
import me.kuraky.spamkiller.data.PlayerData;

import java.util.LinkedList;

public class RawSpeed extends Check {

    public RawSpeed () {
        super("RawSpeed");
    }

    @Override
    public int onChat(String message, PlayerData playerData) {
        LinkedList<Long> messageTimes = playerData.getMessageTimes();

        int mute = 0;

        if(messageTimes.size() > 3) {
            int avg = 0;
            int illegalMessageCounter = 0;

            int allowedDelay = ConfigManager.getRawSpeedAllowedDelay();

            int messages = Math.min(6, messageTimes.size());

            for(int i = 0; i < messages - 1; i++) {
                int diff = (int) (messageTimes.get(i) - messageTimes.get(i + 1));

                if(diff < allowedDelay) illegalMessageCounter++;
                if(i == 0 && illegalMessageCounter == 0) break;

                avg += diff;
            }
            avg /= messageTimes.size() - 1;

            if(avg < allowedDelay && (double)illegalMessageCounter/(messages - 1) > 0.7) {
                mute = (int) ((double)(allowedDelay - avg)/allowedDelay*4000);
            }
        }
        return  mute;
    }
}
