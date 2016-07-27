package ru.org.sarg.dungeon.window;

import ru.org.sarg.dungeon.Controls;
import ru.org.sarg.dungeon.KeyHandler;
import ru.org.sarg.dungeon.game.Menu;
import ru.org.sarg.dungeon.render.IDisplay;

public class MenuWindow extends TextWindow implements KeyHandler {
    private static final String SELECTED_PREFIX = "* ";
    Menu menu;
    int index;

    public MenuWindow(int windowX, int windowY, int width, int height) {
        super(windowX, windowY, width, height);
    }

    public MenuWindow(Menu initial) {
        this(0, 0, 5, 5);
        setBorder(1);
        setMenu(initial);
    }

    @Override
    public void onKeyDown(int key) {
        switch (key) {
            case Controls.UP:
                index = (index + menu.size() - 1) % menu.size();
                break;

            case Controls.DOWN:
                index = (index + 1) % menu.size();
                break;

            case Controls.ENTER:
                Menu.Option option = menu.getChoices().get(index);
                if (option.next != null)
                    setMenu(option.next.get());
                else
                    option.action.run();
                break;
        }
    }

    public void setMenu(Menu m) {
        this.menu = m;
        this.width = m.getChoices().stream().mapToInt(o -> o.title.length()).max().getAsInt() * 2
                + getBorder() * 2
                + getPadding() * 2
                + SELECTED_PREFIX.length();

        this.height = m.getChoices().size()
                + getBorder() * 2
                + getPadding() * 2;

        this.index = 0;
    }

    @Override
    public void draw(IDisplay display) {
        this.windowX = (display.getWidth() - width) / 2;
        this.windowY = (display.getHeight() - height) / 2;

        super.clear();
        for (int i = 0; i < menu.getChoices().size(); i++) {
            Menu.Option option = menu.getChoices().get(i);
            boolean selected = i == index;
            super.append(String.format("%3s%s\n", selected ? SELECTED_PREFIX : "", option.title));
        }

        super.draw(display);
    }
}
