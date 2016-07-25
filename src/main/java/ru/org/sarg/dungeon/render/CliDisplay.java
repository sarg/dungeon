package ru.org.sarg.dungeon.render;

public class CliDisplay implements IDisplay {

    public static final String ANSI_CLEAR_SCREEN = "\u001B[2J";
    public static final String ANSI_MOVE_TOP_LEFT = "\u001B[0;0H";

    private int height;
    private int width;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void draw(int x, int y) {
        throw new RuntimeException("Not implemented yet");
    }

    public void clear() {
        System.out.print(ANSI_CLEAR_SCREEN);
        System.out.print(ANSI_MOVE_TOP_LEFT);
    }
}
