package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.map.LevelMap;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.window.MapWindow;
import ru.org.sarg.dungeon.window.TextWindow;

public class GameActivity extends Activity {
    public static GameActivity INSTANCE;

    private static final double MAP_SIZE_RATIO = 0.8;
    MapWindow mapWindow;
    TextWindow stats;
    PauseMenuActivity pauseMenuActivity;
    Player player;

    public GameActivity(IDisplay display) {
        super(display);
    }

    private void move(Direction d) {
        player.move(d);
        mapWindow.adjustForPlayer(player);
    }

    public void setPaused(boolean p) {
        assert(isPaused() != p);

        if (p) {
            pauseMenuActivity = new PauseMenuActivity(display);
            pauseMenuActivity.start();
        } else {
            pauseMenuActivity = null;
        }
    }

    public boolean isPaused() {
        return pauseMenuActivity != null;
    }


    @Override
    public void onKeyDown(int key) {
        if (isPaused()) {
            pauseMenuActivity.onKeyDown(key);
            return;
        }

        switch (key) {
            case 'j':
                move(Direction.DOWN);
                break;

            case 'k':
                move(Direction.UP);
                break;

            case 'h':
                move(Direction.LEFT);
                break;

            case 'l':
                move(Direction.RIGHT);
                break;

            case 'q':
                setPaused(true);
                break;
        }
    }

    @Override
    public void draw() {
        mapWindow.draw(display);
        stats.draw(display);

        if (isPaused())
            pauseMenuActivity.draw();
    }

    public void start() {
        mapWindow = new MapWindow(0, 0, (int)(display.getWidth() * 0.8), (int)(Math.round(display.getHeight() * MAP_SIZE_RATIO)));
        LevelMap map = LevelMap.RANDOM();
        map.getObjects().add(player);
        mapWindow.setMap(map);

        stats = new TextWindow(0, mapWindow.getHeight(), display.getWidth(),
                display.getHeight() - mapWindow.getHeight() - 1);

        stats.setText(String.format("%s %s\nExperience: %07d", player.race.name(), player.name, player.exp));
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
