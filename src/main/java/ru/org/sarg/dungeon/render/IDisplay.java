package ru.org.sarg.dungeon.render;

public interface IDisplay {
    int getWidth();
    int getHeight();

    void draw(int x, int y);
    void clear();
}
