package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.map.LevelMap;
import ru.org.sarg.dungeon.render.IDisplay;

public class GameActivity extends Activity {
    private static final double MAP_SIZE_RATIO = 0.8;
    MapWindow mapWindow;
    TextWindow stats;

    public GameActivity(IDisplay display) {
        super(display);
    }

    @Override
    public void onKeyDown(int key) {
        switch (key) {
            case 'j':
                mapWindow.scroll(MapWindow.Scroll.DOWN);
                break;

            case 'k':
                mapWindow.scroll(MapWindow.Scroll.UP);
                break;

            case 'h':
                mapWindow.scroll(MapWindow.Scroll.LEFT);
                break;

            case 'l':
                mapWindow.scroll(MapWindow.Scroll.RIGHT);
                break;
        }
    }

    @Override
    public void draw() {
        mapWindow.draw(display);
        stats.draw(display);
    }

    public void start() {
        mapWindow = new MapWindow(0, 0, (int)(display.getWidth() * 0.8), (int)(Math.round(display.getHeight() * MAP_SIZE_RATIO)));
        mapWindow.setMap(LevelMap.RANDOM());

        stats = new TextWindow(0, mapWindow.getHeight(), display.getWidth(),
                display.getHeight() - mapWindow.getHeight() - 1);

        stats.setText("test");

    }

    public void quit() {
    }
}
