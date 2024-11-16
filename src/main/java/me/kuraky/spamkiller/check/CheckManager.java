package me.kuraky.spamkiller.check;

import me.kuraky.spamkiller.check.checks.*;

import java.util.ArrayList;

public class CheckManager {

    private static ArrayList<Check> checks = new ArrayList<>();

    public static void init() {
        checks.add(new PerfectDelay());
        checks.add(new LongSimilarity());
        checks.add(new ShortSimilarity());
        checks.add(new RepeatedChar());
        checks.add(new CapsLock());
        checks.add(new TypingSpeed());
        checks.add(new RawSpeed());
        checks.add(new GroupSpam());
        checks.add(new NonAsciiCharacters());
        checks.add(new LongWords());
    }

    public static ArrayList<Check> getChecks() {
        return checks;
    }
}
