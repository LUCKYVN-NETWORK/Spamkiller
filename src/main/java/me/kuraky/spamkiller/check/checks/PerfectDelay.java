package me.kuraky.spamkiller.check.checks;

import me.kuraky.spamkiller.check.Check;
import me.kuraky.spamkiller.data.PlayerData;

import java.util.LinkedList;

public class PerfectDelay extends Check {

    public PerfectDelay() {
        super("PerfectDelay");
    }

    @Override
    public int onChat(String message, PlayerData playerData) {
        LinkedList<Long> messageTimes = playerData.getMessageTimes();

        int mute = 0;

        if(messageTimes.size() > 2) {
            int messages = Math.min(7, messageTimes.size());
            int diff = (int) (messageTimes.get(0) - messageTimes.get(1));
            if(diff > 50) { //cause lags n stuff
                int counter = 0;
                for (int i = 1; i < messages - 1; i++) {
                    int comparedDiff = (int) (messageTimes.get(i) - messageTimes.get(i + 1));

                    double threshold = Math.min(diff/90d, 70);
                    int diffDiff = Math.abs(diff - comparedDiff);

                    if (diffDiff < threshold) {
                        counter++;
                        mute += (threshold - diffDiff)/threshold*10000d;
                    }
                }
                if (counter < 2) mute = 0;
            }
        }
        return mute;
    }
}
