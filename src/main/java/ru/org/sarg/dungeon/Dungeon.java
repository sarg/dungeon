package ru.org.sarg.dungeon;

import ru.org.sarg.dungeon.game.Activity;
import ru.org.sarg.dungeon.game.FpsCounter;
import ru.org.sarg.dungeon.game.MenuActivity;
import ru.org.sarg.dungeon.render.CliDisplay;
import ru.org.sarg.dungeon.render.IDisplay;

import java.io.IOException;

public class Dungeon {
    public static Dungeon INSTANCE = new Dungeon();

    public Activity activity;
    private static boolean quit = false;

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

    public void setActivity(Activity a) {
        this.activity = a;
        a.start();
    }

    public void quit() {
        quit = true;
    }

    public static void main(String[] args) throws IOException {
        initTerm();
        IDisplay display = new CliDisplay(80, 40);

        Dungeon.INSTANCE.setActivity(new MenuActivity(display));

        FpsCounter fpsCounter = new FpsCounter(10);
        while (!quit) {
            Activity a = Dungeon.INSTANCE.activity;
            a.draw();

            display.flush();
            fpsCounter.onFrame();

            int key = readKey();
            if (key > 0)
                a.onKeyDown(key);

            fpsCounter.sleepUntilNextFrame();
            display.clear();
        }
    }
}
