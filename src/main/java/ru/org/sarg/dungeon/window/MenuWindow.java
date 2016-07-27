package ru.org.sarg.dungeon.window;

import ru.org.sarg.dungeon.game.Menu;
import ru.org.sarg.dungeon.render.IDisplay;

import java.awt.event.KeyEvent;

public class MenuWindow extends TextWindow {
    private static final String SELECTED_PREFIX = "* ";
    Menu menu;
    int index;

    public MenuWindow(int windowX, int windowY, int width, int height) {
        super(windowX, windowY, width, height);
    }

    public MenuWindow() {
        this(0, 0, 5, 5);
        setBorder(1);
    }

    public void onKeyDown(int key) {
        switch (key) {
            case 'k':
                index = (index + menu.size() - 1) % menu.size();
                break;

            case 'j':
                index = (index + 1) % menu.size();
                break;

            case KeyEvent.VK_ENTER:
                Menu.Option option = menu.choices.get(index);
                if (option.next != null)
                    setMenu(option.next.get());
                else
                    option.action.run();
                break;
        }
    }

    public void setMenu(Menu m) {
        this.menu = m;
        this.width = m.choices.stream().mapToInt(o -> o.title.length()).max().getAsInt() * 2
                + getBorder() * 2
                + getPadding() * 2
                + SELECTED_PREFIX.length();

        this.height = m.choices.size()
                + getBorder() * 2
                + getPadding() * 2;

        this.index = 0;
    }

    @Override
    public void draw(IDisplay display) {
        this.windowX = (display.getWidth() - width) / 2;
        this.windowY = (display.getHeight() - height) / 2;

        super.clear();
        for (int i = 0; i < menu.choices.size(); i++) {
            Menu.Option option = menu.choices.get(i);
            boolean selected = i == index;
            super.append(String.format("%3s%s\n", selected ? SELECTED_PREFIX : "", option.title));
        }

        super.draw(display);
    }
}
