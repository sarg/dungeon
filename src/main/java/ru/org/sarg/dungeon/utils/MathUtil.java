package ru.org.sarg.dungeon.utils;

import java.util.Random;

public class MathUtil {
    private static final Random rnd = new Random();

    public static int rand(int max) {
        return rnd.nextInt(max + 1);
    }

    public static int adjust(int x, int min, int max) {
        if (x < min) return min;
        if (x > max) return max;
        return x;
    }
}
