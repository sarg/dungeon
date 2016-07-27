package ru.org.sarg.dungeon.window;

import ru.org.sarg.dungeon.render.IDisplay;

public abstract class AbstractWindow {
    int windowX;
    int windowY;
    int width;
    int height;
    int border;
    int padding;

    AbstractWindow(int windowX, int windowY, int width, int height) {
        this.width = width;
        this.height = height;
        this.windowX = windowX;
        this.windowY = windowY;
    }

    void draw(IDisplay display) {
        display.rect(windowX, windowY, windowX + width - 1, windowY + height - 1, IDisplay.Color.WHITE);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    int getBorder() {
        return border;
    }

    void setBorder(int border) {
        this.border = border;
    }

    int getPadding() {
        return padding;
    }

    void setPadding(int padding) {
        this.padding = padding;
    }
}
