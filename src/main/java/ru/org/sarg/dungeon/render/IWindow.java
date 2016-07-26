package ru.org.sarg.dungeon.render;

public abstract class IWindow {
    protected int width;
    protected int height;
    public int windowX;
    public int windowY;

    public IWindow(int width, int height, int windowX, int windowY) {
        this.width = width;
        this.height = height;
        this.windowX = windowX;
        this.windowY = windowY;
    }

    public void draw(IDisplay display) {
        display.rect(windowX, windowY, windowX + width - 1, windowY + height - 1);
    }
}
