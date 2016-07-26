package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.render.IDisplay;

import java.awt.event.KeyEvent;
import java.util.Iterator;

public class CharacterCreateActivity extends Activity {
    private TextWindow dialog;

    public CharacterCreateActivity(IDisplay display) {
        super(display);
    }

    public void onKeyDown(int key) {
        switch (key) {
        }
    }

    @Override
    public void start() {
        dialog = new TextWindow(0, 0, display.getWidth(), display.getHeight());
    }

    @Override
    public void draw() {
        dialog.draw(display);
    }
}
