package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.Dungeon;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.window.Input;
import ru.org.sarg.dungeon.window.MenuWindow;
import ru.org.sarg.dungeon.window.TextWindow;

import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

public class PauseMenuActivity extends Activity {
    public class SaveActivity extends Activity {
        private TextWindow dialog;
        private Input input;

        public SaveActivity(IDisplay display) {
            super(display);
        }

        public void onKeyDown(int key) {
            if (input == null)
                return;

            if (key == KeyEvent.VK_ENTER) {
                input.onEnter();
            } else {
                dialog.deleteLast(input.length());
                input.onKeyDown(key);
                dialog.append(input.value());
            }
        }

        @Override
        public void start() {
            int x = display.getWidth() / 4;
            int y = display.getHeight() / 4;
            dialog = new TextWindow(x, y, display.getWidth() - 1 - x, display.getHeight() - 1 - x);

            dialog.setText("Save your adventure in " + GameActivity.INSTANCE.player.name + ".sav? (y/n)\n");
            input = new Input(5, Pattern.compile("[yn]"), () -> {
                setSaving(false);
            });
        }

        @Override
        public void draw() {
            dialog.draw(display);
        }
    }

    MenuWindow menuWindow;
    SaveActivity saveActivity;

    public PauseMenuActivity(IDisplay display) {
        super(display);
    }

    public void onKeyDown(int key) {
        if (isSaving()) {
            saveActivity.onKeyDown(key);
            return;
        }

        menuWindow.onKeyDown(key);
    }

    @Override
    public void start() {
        Menu current = new Menu(null, null);
        current.choices.add(new Menu.Option("Resume", () -> GameActivity.INSTANCE.setPaused(false)));
        current.choices.add(new Menu.Option("Save", () -> setSaving(true)));
        current.choices.add(new Menu.Option("Quit", () -> Dungeon.INSTANCE.quit()));

        menuWindow = new MenuWindow();
        menuWindow.setMenu(current);
    }

    @Override
    public void draw() {
        if (isSaving())  {
            saveActivity.draw();
        } else {
            menuWindow.draw(display);
        }
    }

    public boolean isSaving() {
        return saveActivity != null;
    }

    public void setSaving(boolean s) {
        assert(isSaving() != s);

        if (s) {
            saveActivity = new SaveActivity(display);
            saveActivity.start();
        } else {
            saveActivity = null;
        }
    }
}
