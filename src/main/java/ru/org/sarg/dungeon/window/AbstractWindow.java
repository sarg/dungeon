package ru.org.sarg.dungeon.window;

import ru.org.sarg.dungeon.render.IDisplay;

public abstract class AbstractWindow {
    protected int width;
    protected int height;
    public int windowX;
    public int windowY;
    private int border;
    private int padding;

    public AbstractWindow(int windowX, int windowY, int width, int height) {
        this.width = width;
        this.height = height;
        this.windowX = windowX;
        this.windowY = windowY;
    }

    public void draw(IDisplay display) {
        display.rect(windowX, windowY, windowX + width - 1, windowY + height - 1, IDisplay.Color.WHITE);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }
}
