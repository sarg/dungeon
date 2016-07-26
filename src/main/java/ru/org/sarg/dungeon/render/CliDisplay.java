package ru.org.sarg.dungeon.render;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class CliDisplay implements IDisplay {

    public static final String ANSI_CLEAR_SCREEN = "\u001B[2J";
    public static final String ANSI_MOVE_TOP_LEFT = "\u001B[0;0H";

    private final int height;
    private final int width;

    private char[] buffer;

    public CliDisplay(int width, int height) {
        this.height = height;
        this.width = width;
        this.buffer = new char[width * height];

        Arrays.fill(buffer, '.');
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int bufPtr(int x, int y) {
        return y * width + x;
    }

    @Override
    public void text(int x, int y, Color color, CharSequence txt) {
        AtomicInteger ptr = new AtomicInteger(bufPtr(x, y));
        int safeLength = getSafeLength(x, txt);

        txt.subSequence(0, safeLength).chars().forEach(
                c -> buffer[ptr.getAndIncrement()] = (char) c
        );
    }

    int getSafeLength(int x, CharSequence txt) {
        return x + txt.length() >= width
                ? width - x
                : txt.length();
    }

    @Override
    public void draw(int x, int y, char symbol) {
        buffer[bufPtr(x, y)] = symbol;
    }

    private void hline(int x1, int x2, int y) {
        int ptr = bufPtr(x1, y);
        while (x2-- >= x1) {
            buffer[ptr++] = '-';
        }
    }

    private void vline(int x, int y1, int y2) {
        int ptr = bufPtr(x, y1);
        while (y2-- >= y1) {
            buffer[ptr] = '|';
            ptr += width;
        }
    }

    @Override
    public void rect(int x1, int y1, int x2, int y2) {
        int tmp;
        if (x1 > x2) {
            tmp = x1;
            x1 = x2;
            x2 = tmp;
        }

        if (y1 > y2) {
            tmp = y1;
            y1 = y2;
            y2 = tmp;
        }

        vline(x1, y1 + 1, y2 - 1);
        vline(x2, y1 + 1, y2 - 1);

        hline(x1 + 1, x2 - 1, y1);
        hline(x1 + 1, x2 - 1, y2);

        draw(x1, y1, '+');
        draw(x2, y1, '+');
        draw(x1, y2, '+');
        draw(x2, y2, '+');
    }

    public void flush() {
        System.out.print(ANSI_CLEAR_SCREEN);
        System.out.print(ANSI_MOVE_TOP_LEFT);

        int ptr = 0;
        while (ptr < width * height) {
            System.out.print(buffer[ptr++]);
            if (ptr % width == 0)
                System.out.println();
        }
    }

    public void clear() {
        Arrays.fill(buffer, ' ');
    }
}
