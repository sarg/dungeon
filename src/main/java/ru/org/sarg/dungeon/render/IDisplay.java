package ru.org.sarg.dungeon.render;

/**
 * Coordinate system
 * <p>
 * +----> X
 * |
 * |
 * |
 * V Y
 */
public interface IDisplay {
    void flush();

    int getWidth();

    int getHeight();

    void draw(int x, int y, char symbol, Color color);

    void rect(int x1, int y1, int x2, int y2, Color color);

    void fill(int x1, int y1, int x2, int y2, Color color);

    void clear();

    enum Color {
        RED, WHITE, BLUE
    }
}
