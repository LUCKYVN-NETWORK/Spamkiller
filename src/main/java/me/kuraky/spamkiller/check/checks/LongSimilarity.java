package me.kuraky.spamkiller.check.checks;

import me.kuraky.spamkiller.check.Check;
import me.kuraky.spamkiller.config.ConfigManager;
import me.kuraky.spamkiller.data.PlayerData;
import me.kuraky.spamkiller.util.MessageUtils;

import java.util.LinkedList;

public class LongSimilarity extends Check {

    public LongSimilarity() {
        super("LongSimilarity");
    }

    @Override
    public int onChat(String message, PlayerData playerData) {
        LinkedList<String> messageList = playerData.getMessages();
        int size = messageList.size();

        message = messageList.getFirst();

        int mute = 0;

        if(message.length() >= ConfigManager.getLongSimilarityMinimumCharacters()) {
            if (size > 1) {
                size = Math.min(3, size);

                int highestSimilarity = 0;

                String[] splitted = message.split("\\s+");

                for(int i = 1; i < size; i++) {
                    double generalSimilarity = MessageUtils.generalSimilarity(message, messageList.get(i))*100;
                    double wordSimilarity;
                    if(splitted.length >= ConfigManager.getLongSimilarityWordsRequired()) wordSimilarity = MessageUtils.wordSimilarity(splitted, messageList.get(i))*100;
                    else wordSimilarity = generalSimilarity;

                    double similarity = (generalSimilarity + wordSimilarity)/2;

                    if(similarity > highestSimilarity) highestSimilarity = (int) similarity;
                }

                int allowedSimilarity = ConfigManager.getLongSimilarityAllowedSimilarity();

                if(highestSimilarity > allowedSimilarity) {
                    mute = Math.min(2, highestSimilarity/allowedSimilarity)*message.length()*250;
                }
            }
        }
        return mute;
    }
}
