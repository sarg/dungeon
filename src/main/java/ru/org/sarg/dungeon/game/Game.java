package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.render.CliDisplay;
import ru.org.sarg.dungeon.render.IDisplay;

import java.io.IOException;

public class Game {
    public void init() {
        // stty to disable buffered input
        // not portable, consider using JLine
        if (initTerm()) {
            IDisplay display = new CliDisplay(80, 25);
            new MenuActivity(display).execute();
        }
    }

    private boolean initTerm() {
        try {
            Runtime.getRuntime().exec(new String[]{"sh", "-c", "stty -icanon min 1 < /dev/tty"});
            Runtime.getRuntime().exec(new String[]{"sh", "-c", "stty -echo < /dev/tty"});

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void start() {

    }

    public void quit() {
    }
}
