package me.kuraky.spamkiller.check.checks;

import me.kuraky.spamkiller.check.Check;
import me.kuraky.spamkiller.config.ConfigManager;
import me.kuraky.spamkiller.data.PlayerData;
import me.kuraky.spamkiller.util.MessageUtils;

import java.util.LinkedList;

public class ShortSimilarity extends Check {

    public ShortSimilarity() {
        super("ShortSimilarity");
    }

    @Override
    public int onChat(String message, PlayerData playerData) {
        LinkedList<String> messageList = playerData.getMessages();
        int size = messageList.size();

        message = messageList.getFirst();

        int mute = 0;

        if(message.length() < ConfigManager.getLongSimilarityMinimumCharacters()) {
            if(size > 1) {
                size = Math.min(4, size);

                int highestSimilarity = 0;

                for(int i = 1; i < size; i++) {
                    double similarity = MessageUtils.generalSimilarity(message, messageList.get(i))*100;

                    if(similarity > highestSimilarity) highestSimilarity = (int) similarity;
                }

                int allowedSimilarity = ConfigManager.getShortSimilarityAllowedSimilarity();

                if(highestSimilarity > allowedSimilarity) {
                    playerData.setSimilarityViolations(playerData.getSimilarityViolations() + 1);

                    if(playerData.getSimilarityViolations() > 1) {
                        mute = Math.min(2, highestSimilarity/allowedSimilarity)*message.length()*150;
                    }
                }
                else playerData.setSimilarityViolations(0);
            }
        }
        return mute;
    }
}
