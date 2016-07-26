package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.render.IDisplay;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;

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
            System.out.println(key);

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
