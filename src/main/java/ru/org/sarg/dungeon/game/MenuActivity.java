package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.Dungeon;
import ru.org.sarg.dungeon.render.IDisplay;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;

public class MenuActivity extends Activity {
    private static final int MENU_WIDTH = 20;
    Menu current;
    int index;

    /*
    New -> Character creation dialog -> Game
    Load -> List of saves -> Select -> Game
    Quit -> clean up and exit
     */

    public MenuActivity(IDisplay display) {
        super(display);

        current = new Menu(null) {
            @Override
            public Menu next() {
                return null;
            }
        };

        current.choices.add(new Menu.Option("New", () -> Dungeon.INSTANCE.setActivity(new CharacterCreateActivity(display))));
        current.choices.add(new Menu.Option("Quit", () -> Dungeon.INSTANCE.quit()));
        index = 0;
    }

    public void onKeyDown(int key) {
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

    @Override
    public void start() {
    }

    @Override
    public void draw() {
        int idx = 0;
        int x = (display.getWidth() - 2 - MENU_WIDTH) / 2;
        int y = (display.getHeight() - 2 - current.choices.size()) / 2;
        display.rect(x, y, x + MENU_WIDTH + 1, y + 1 + current.choices.size());

        x++;

        Iterator<Menu.Option> menuIterator = current.choices.iterator();
        while (menuIterator.hasNext()) {
            Menu.Option option = menuIterator.next();
            boolean selected = idx++ == index;

            display.text(x, ++y, null, String.format("%3s%s", selected ? "* " : "", option.title));
        }
    }
}
