package ru.org.sarg.dungeon.map;

public abstract class GameObject {
    protected int x;
    protected int y;
    private char c;
    private final int zOrder;

    public GameObject(char c, int zOrder) {
        this.c = c;
        this.zOrder = zOrder;
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

    public int getzOrder() {
        return zOrder;
    }
}
