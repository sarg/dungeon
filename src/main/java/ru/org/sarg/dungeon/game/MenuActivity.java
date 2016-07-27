package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.Dungeon;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.window.Input;
import ru.org.sarg.dungeon.window.MenuWindow;
import ru.org.sarg.dungeon.window.TextWindow;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

public class MenuActivity extends Activity {
    public class LoadActivity extends Activity {
        private MenuWindow loadMenuWindow;

        public LoadActivity(IDisplay display) {
            super(display);
        }

        public void onKeyDown(int key) {
            loadMenuWindow.onKeyDown(key);
        }

        @Override
        public void start() {
            Menu menu = new Menu(null, null);
            menu.choices.add(new Menu.Option("asd", () -> setLoading(false)));

            loadMenuWindow = new MenuWindow();
            loadMenuWindow.setMenu(menu);
        }

        @Override
        public void draw() {
            loadMenuWindow.draw(display);
        }
    }

    MenuWindow menuWindow;
    LoadActivity loadActivity;

    /*
    New -> Character creation dialog -> Game
    Load -> List of saves -> Select -> Game
    Quit -> clean up and exit
     */

    public MenuActivity(IDisplay display) {
        super(display);
    }

    public void onKeyDown(int key) {
        if (isLoading())
            loadActivity.onKeyDown(key);
        else
            menuWindow.onKeyDown(key);
    }

    @Override
    public void start() {
        Menu current = new Menu(null, null);
        current.choices.add(new Menu.Option("New", () -> Dungeon.INSTANCE.setActivity(new CharacterCreateActivity(display))));
        current.choices.add(new Menu.Option("Load", () -> setLoading(true)));
        current.choices.add(new Menu.Option("Quit", () -> Dungeon.INSTANCE.quit()));

        menuWindow = new MenuWindow();
        menuWindow.setMenu(current);
    }

    private boolean isLoading() {
        return loadActivity != null;
    }

    private void setLoading(boolean l) {
        assert(isLoading() != l);

        if (l) {
            loadActivity = new LoadActivity(display);
            loadActivity.start();
        } else {
            loadActivity = null;
        }
    }

    @Override
    public void draw() {
        if (isLoading())
            loadActivity.draw();
        else
            menuWindow.draw(display);
    }
}
