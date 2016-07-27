package ru.org.sarg.dungeon.render;

import java.io.IOException;
import java.util.Arrays;

public class CliDisplay implements IDisplay {

    private static final String ANSI_CLEAR_SCREEN = "\u001B[2J";
    private static final String ANSI_MOVE_TOP_LEFT = "\u001B[0;0H";

    private static boolean isWindows = System.getProperty("os.name").startsWith("Windows");

    private final int height;
    private final int width;

    private final char[] buffer;
    private long lastCrc;

    public CliDisplay(int width, int height) {
        this.height = height;
        this.width = width;
        this.buffer = new char[width * height];

        clear();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private int bufPtr(int x, int y) {
        return y * width + x;
    }

    int getSafeLength(int x, CharSequence txt) {
        return x + txt.length() >= width
                ? width - x
                : txt.length();
    }

    @Override
    public void draw(int x, int y, char symbol, Color color) {
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
    public void rect(int x1, int y1, int x2, int y2, Color color) {
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

        draw(x1, y1, '+', color);
        draw(x2, y1, '+', color);
        draw(x1, y2, '+', color);
        draw(x2, y2, '+', color);
    }

    @Override
    public void fill(int x1, int y1, int x2, int y2, Color color) {
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

        while (x1 <= x2) {
            int y = y1;
            while (y <= y2) {
                draw(x1, y++, ' ', color);
            }
            x1++;
        }
    }

    public void flush() {
        int newCrc = Arrays.hashCode(buffer);
        if (newCrc == lastCrc)
            return; // optimization, no need to redraw as nothing changed

        lastCrc = newCrc;

        if (isWindows) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException("Unhandled exception", e);
            }
        } else {
            System.out.print(ANSI_CLEAR_SCREEN);
            System.out.print(ANSI_MOVE_TOP_LEFT);
        }

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
