package ru.org.sarg.dungeon;

import ru.org.sarg.dungeon.game.Activity;
import ru.org.sarg.dungeon.game.GameActivity;
import ru.org.sarg.dungeon.game.MenuActivity;
import ru.org.sarg.dungeon.render.CliDisplay;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.utils.FpsCounter;

import java.io.IOException;

public class Dungeon {
    public static final boolean DEBUG = true;
    public static final Dungeon INSTANCE = new Dungeon();
    private static boolean quit = false;
    public Activity activity;

    private Dungeon() {
    }

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
        int key = 0;
        try {
            int cnt = System.in.available();
            if (cnt > 0)
                key = System.in.read();
        } catch (IOException e) {
            throw new RuntimeException("Unhandled exception", e);
        }
        return key;
    }

    public static void main(String[] args) throws IOException {
        initTerm();
        IDisplay display = new CliDisplay(79, 25);

        GameActivity.INSTANCE = new GameActivity(display);
        Dungeon.INSTANCE.setActivity(new MenuActivity(display));

        FpsCounter fpsCounter = new FpsCounter(10);
        while (!quit) {
            Activity a = Dungeon.INSTANCE.activity;
            processKey(a);

            if (fpsCounter.isReadyForNextFrame()) {
                nextFrame(display, a);
                fpsCounter.onFrame();
            }
        }
    }

    private static void nextFrame(IDisplay display, Activity a) {
        display.clear();
        a.draw();
        display.flush();
    }

    private static void processKey(Activity a) {
        int key = readKey();
        if (key > 0)
            a.onKeyDown(key);
    }

    public void setActivity(Activity a) {
        this.activity = a;
        a.start();
    }

    public void quit() {
        quit = true;
    }
}
