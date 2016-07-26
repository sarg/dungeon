package ru.org.sarg.dungeon;

import ru.org.sarg.dungeon.game.Activity;
import ru.org.sarg.dungeon.game.GameActivity;
import ru.org.sarg.dungeon.render.CliDisplay;
import ru.org.sarg.dungeon.render.IDisplay;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

    public static void main(String[] args) throws IOException, InterruptedException {
        initTerm();
        IDisplay display = new CliDisplay(80, 40);
        Activity gameActivity = new GameActivity(display);
        gameActivity.start();
        boolean quit = false;

        long lastFrame;
        int FPS = 10;
        while (!quit) {
            gameActivity.draw();
            display.flush();
            lastFrame = System.nanoTime();

            int key = readKey();
            if (key > 0)
                gameActivity.onKeyDown(key);

            long sleep = TimeUnit.SECONDS.toNanos(1) / FPS - (System.nanoTime() - lastFrame);
            if (sleep > 0)
                Thread.sleep(TimeUnit.NANOSECONDS.toMillis(sleep));

            display.clear();
        }
    }
}
