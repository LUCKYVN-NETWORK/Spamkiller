package me.kuraky.spamkiller.check.checks;

import me.kuraky.spamkiller.check.Check;
import me.kuraky.spamkiller.config.ConfigManager;
import me.kuraky.spamkiller.data.PlayerData;
import me.kuraky.spamkiller.util.MessageUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.UUID;

public class GroupSpam extends Check {

    public GroupSpam() {
        super("GroupSpam");
    }

    @Override
    public int onChat(String message, PlayerData playerData) {
        LinkedList<String> globalMessages = PlayerData.getGlobalMessages();
        int size = globalMessages.size();

        int mute = 0;

        if(size >= 5) {
            int counter = 0;
            int allowedSimilarity = ConfigManager.getGroupSpamAllowedSimilarity();
            double avgSimilarity = 0;

            message = playerData.getMessages().getFirst();
            HashSet<UUID> globalSenders = new HashSet<>();
            globalSenders.add(playerData.getUuid());

            for(int i = 1; i < size; i++) {
                double similarity = MessageUtils.generalSimilarity(message, globalMessages.get(i))*100;

                if(similarity > allowedSimilarity) {
                    counter++;
                    avgSimilarity += similarity;
                    globalSenders.add(PlayerData.getGlobalSenders().get(i));
                }
            }
            
            if(counter > 3 && globalSenders.size() >= 3) {
                avgSimilarity /= counter;
                mute = (int) (avgSimilarity/allowedSimilarity*counter*400*Math.max(10, message.length()));
            }
        }

        return mute;
    }
}
