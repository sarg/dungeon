package ru.org.sarg.dungeon.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Menu {
    private List<Menu.Option> choices = new ArrayList<>();

    public int size() {
        return choices.size();
    }

    public List<Option> getChoices() {
        return choices;
    }

    public Menu(List<Option> choices) {
        this.choices = choices;
    }

    public static class Option {
        public String title;
        public Runnable action;
        public Supplier<Menu> next;

        public Option(String title, Runnable action, Supplier<Menu> next) {
            this(title, action);
            this.next = next;
        }

        public Option(String title, Runnable action) {
            this.title = title;
            this.action = action;
        }
    }
}
