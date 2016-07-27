package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.KeyHandler;
import ru.org.sarg.dungeon.render.IDisplay;

public abstract class Activity implements KeyHandler {
    final protected IDisplay display;

    public Activity(IDisplay display) {
        this.display = display;
    }

    @Override
    abstract public void onKeyDown(int key);

    abstract public void draw();

    public abstract void start();
}
