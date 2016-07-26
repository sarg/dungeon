package ru.org.sarg.dungeon;

import ru.org.sarg.dungeon.game.Activity;
import ru.org.sarg.dungeon.game.GameActivity;
import ru.org.sarg.dungeon.render.CliDisplay;
import ru.org.sarg.dungeon.render.IDisplay;

import java.io.IOException;

public class Dungeon {
    // stty to disable buffered input
    // not portable, consider using JLine
    private static boolean initTerm() {
        try {
            Runtime.getRuntime().exec(new String[]{"sh", "-c", "stty -icanon min 1 < /dev/tty"});
            Runtime.getRuntime().exec(new String[]{"sh", "-c", "stty -echo < /dev/tty"});

            return true;
        } catch (IOException e) {
            return false;
        }
    }


    private static int readKey() {
        int key;
        try {
            key = System.in.read();
        } catch (IOException e) {
            throw new RuntimeException("Unhandled exception", e);
        }
        return key;
    }

    public static void main(String[] args) throws IOException {
        initTerm();
        IDisplay display = new CliDisplay(80, 40);
        Activity gameActivity = new GameActivity(display);
        gameActivity.start();
        boolean quit = false;

        while (!quit) {
            gameActivity.draw();
            display.flush();

            int key = readKey();
            gameActivity.onKeyDown(key);
            display.clear();
        }
    }
}
