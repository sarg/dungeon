package ru.org.sarg.dungeon.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Menu {
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
    public Supplier<Menu> next;

    public int size() {
        return choices.size();
    }

    public Menu(Menu parent, Supplier<Menu> next) {
        this.parent = parent;
        this.next = next;
    }
}
