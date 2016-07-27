package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.window.Input;
import ru.org.sarg.dungeon.window.TextWindow;

import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

public class FightActivity extends Activity {
    private TextWindow dialog;
    private Input input;

    public FightActivity(IDisplay display) {
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
    public void draw() {
        dialog.draw(display);
    }

    @Override
    public void start() {
        int x = display.getWidth() / 3;
        int y = display.getHeight() / 3;
        dialog = new TextWindow(x, y, display.getWidth() - 1 - x, display.getHeight() - 1 - x);

        dialog.setText("Fight! Fight! Fight!\nSelect your number 0-10: ");
        input = new Input(2, Pattern.compile("\\d"), () -> {
            int peng = (int) Math.round(Math.random() * 10);
            int you = input.length();

            if (you == peng) {
                input.clear();
            } else if (you > peng) {
                dialog.append("\nWin!");
            } else {
                dialog.setText("\nLose!");
            }
        });
    }
}
