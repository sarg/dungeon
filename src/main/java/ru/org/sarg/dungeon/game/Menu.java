package ru.org.sarg.dungeon.game;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu {
    public static class Option {
        public String title;
        public Runnable action;

        public Option(String title, Runnable action) {
            this.title = title;
            this.action = action;
        }
    }

    public List<Menu.Option> choices = new ArrayList<>();
    public Menu parent;

    public int size() {
        return choices.size();
    }

    public Menu(Menu parent) {
        this.parent = parent;
    }

    abstract public Menu next();
}
