package ru.org.sarg.dungeon.map;

import ru.org.sarg.dungeon.render.IDisplay;

public class GameObject {
    protected int x;
    protected int y;
    private char c;

    public GameObject(char c) {
        this.c = c;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }
}
