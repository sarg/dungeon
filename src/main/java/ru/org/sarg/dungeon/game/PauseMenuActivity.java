package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.Controls;
import ru.org.sarg.dungeon.Dungeon;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.window.Input;
import ru.org.sarg.dungeon.window.MenuWindow;
import ru.org.sarg.dungeon.window.TextWindow;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PauseMenuActivity extends Activity {
    private MenuWindow menuWindow;
    private SaveActivity saveActivity;

    public PauseMenuActivity(IDisplay display) {
        super(display);
    }

    @Override
    public void onKeyDown(int key) {
        if (isSaving()) {
            saveActivity.onKeyDown(key);
            return;
        }

        menuWindow.onKeyDown(key);
    }

    @Override
    public void start() {
        List<Menu.Option> choices = Arrays.asList(
            new Menu.Option("Resume", () -> GameActivity.INSTANCE.setPaused(false)),
            new Menu.Option("Save", () -> setSaving(true)),
            new Menu.Option("Quit", Dungeon.INSTANCE::quit)
        );

        menuWindow = new MenuWindow(new Menu(choices));
    }

    @Override
    public void draw() {
        if (isSaving()) {
            saveActivity.draw();
        } else {
            menuWindow.draw(display);
        }
    }

    private boolean isSaving() {
        return saveActivity != null;
    }

    private void setSaving(boolean s) {
        assert (isSaving() != s);

        if (s) {
            saveActivity = new SaveActivity(display);
            saveActivity.start();
        } else {
            saveActivity = null;
        }
    }

    public class SaveActivity extends Activity {
        private TextWindow dialog;
        private Input input;

        public SaveActivity(IDisplay display) {
            super(display);
        }

        @Override
        public void onKeyDown(int key) {
            if (input == null)
                return;

            if (key == Controls.ENTER) {
                input.onEnter();
            } else {
                dialog.deleteLast(input.length());
                input.onKeyDown(key);
                dialog.append(input.value());
            }
        }

        @Override
        public void start() {
            int x = display.getWidth() / 5;
            int y = display.getHeight() / 5;
            dialog = new TextWindow(x, y, display.getWidth() - x * 2, display.getHeight() - y * 2);

            Path savePath = FileSystems.getDefault().getPath(GameActivity.INSTANCE.player.getName() + ".sav");
            dialog.setText("Save your adventure in " + savePath.toString() + "? (y/n)");
            input = new Input(1, Pattern.compile("[yn]"), () -> {
                if (input.value().equals("y")) {
                    GameActivity.INSTANCE.saveToFile(savePath);
                }
                setSaving(false);
            });
        }

        @Override
        public void draw() {
            dialog.draw(display);
        }
    }
}
