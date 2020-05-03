package me.kuraky.spamkiller.util;

import me.kuraky.spamkiller.config.ConfigManager;

public class MessageUtils {

    public static double wordSimilarity(String[] mainSplitted, String compared) {
        int words = mainSplitted.length;
        int requiredWords = ConfigManager.getLongSimilarityWordsRequired();

        if(words >= requiredWords) {
            String[] comparedSplitted = compared.split("\\s+");

            int found = 0;
            for(String word : mainSplitted) {
                for(String comparedWord : comparedSplitted) {
                    if(comparedWord.equalsIgnoreCase(word)) {
                        found++;
                        break;
                    }
                }
            }
            return Math.min((double)found/words, (double)found/comparedSplitted.length);
        }
        else return 0;
    }

    public static double generalSimilarity(String s1, String s2) {
        String longer = s1, shorter = s2;

        if (s1.length() < s2.length()) {
            longer = s2; shorter = s1;
        }

        int longerLength = longer.length();
        if (longerLength == 0) return 1;

        return (longerLength - distance(longer, shorter))/(double) longerLength;

    }

    //from http://rosettacode.org/wiki/Levenshtein_distance#Java
    private static int distance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();

        int[] costs = new int[b.length() + 1];

        for (int j = 0; j < costs.length; j++)
            costs[j] = j;

        for (int i = 1; i < a.length() + 1; i++) {
            costs[0] = i;

            int nw = i - 1;

            for (int j = 1; j < b.length() + 1; j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);

                nw = costs[j];

                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
}
