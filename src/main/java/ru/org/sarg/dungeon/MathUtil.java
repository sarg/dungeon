package ru.org.sarg.dungeon;

import java.util.Random;

public class MathUtil {
    private static final Random rnd = new Random();

    public static int rand(int max) {
        return rnd.nextInt(max + 1);
    }
}
