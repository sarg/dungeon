package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.Dungeon;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.window.MenuWindow;

public class PauseMenuActivity extends Activity {
    MenuWindow menuWindow;

    /*
    New -> Character creation dialog -> Game
    Load -> List of saves -> Select -> Game
    Quit -> clean up and exit
     */

    public PauseMenuActivity(IDisplay display) {
        super(display);
    }

    public void onKeyDown(int key) {
        menuWindow.onKeyDown(key);
    }

    @Override
    public void start() {
        Menu current = new Menu(null, null);
        current.choices.add(new Menu.Option("Resume", () -> GameActivity.INSTANCE.setPaused(false)));
        current.choices.add(new Menu.Option("Quit", () -> Dungeon.INSTANCE.quit()));

        menuWindow = new MenuWindow();
        menuWindow.setMenu(current);
    }

    @Override
    public void draw() {
        menuWindow.draw(display);
    }
}
