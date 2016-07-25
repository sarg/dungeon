package ru.org.sarg.dungeon;

import ru.org.sarg.dungeon.render.CliDisplay;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.render.IWindow;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dungeon {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Adventure in Dungeon");

        // stty to disable buffered input
        // not portable, consider using JLine
        Runtime.getRuntime().exec(new String[] { "sh",  "-c", "stty -icanon min 1 < /dev/tty" });
        Runtime.getRuntime().exec(new String[] { "sh",  "-c", "stty -echo < /dev/tty" });

        IDisplay display = new CliDisplay();
        new MenuHandler(display).execute();
    }
}

abstract class Menu {
    public static class Option {
        String title;
        Runnable action;

        public Option(String title, Runnable action) {
            this.title = title;
            this.action = action;
        }
    }

    List<Option> choices = new ArrayList<>();
    Menu parent;

    public int size() {
        return choices.size();
    }

    public Menu(Menu parent) {
        this.parent = parent;
    }

    abstract public Menu next();
}

class MenuHandler {
    Menu current;
    IDisplay display;
    int index;

    public MenuHandler(IDisplay display) {
        this.display = display;
        current = new Menu(null) {
            @Override
            public Menu next() {
                return null;
            }
        };

        current.choices.add(new Menu.Option("New", () -> System.out.println("hji")));
        current.choices.add(new Menu.Option("Quit", () -> System.out.println("hji")));
        index = 0;
    }

    public void execute() {
        boolean selected = false;
        while (!selected) {
            draw();
            int key = readKey();

            switch (key) {
                case 'k':
                    index = (index + current.size() - 1) % current.size();
                    break;

                case 'j':
                    index = (index + 1) % current.size();
                    break;

                case 'q':
                    if (current.parent == null)
                        return;
                    break;

                case KeyEvent.VK_ENTER:
                    Menu next = current.next();
                    if (next != null)
                        current = next;
                    else
                        current.choices.get(index).action.run();
                    break;
            }
        }
    }

    private void draw() {
        display.clear();

        int idx = 0;
        Iterator<Menu.Option> menuIterator = current.choices.iterator();
        while (menuIterator.hasNext()) {
            Menu.Option option = menuIterator.next();
            boolean selected = idx++ == index;

            if (selected)
                System.out.print("* ");

            System.out.println(option.title);
        }
    }

    private int readKey() {
        int key;
        try {
            key = System.in.read();
        } catch (IOException e) {
            throw new RuntimeException("Unhandled exception", e);
        }
        return key;
    }
}
