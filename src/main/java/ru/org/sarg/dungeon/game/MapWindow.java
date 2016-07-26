package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.map.GameObject;
import ru.org.sarg.dungeon.map.LevelMap;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.render.IWindow;

public class MapWindow extends IWindow {
    public static final int BORDER = 1;
    public static final int SCROLL_THRESHOLD = 15;

    int viewPortX;
    int viewPortY;

    LevelMap map;
    LevelMap.MapView currentView;

    public MapWindow(int windowX, int windowY, int width, int height) {
        super(windowX, windowY, width, height);
    }

    public void setMap(LevelMap map) {
        this.map = map;
    }

    private char tileConverter(byte t) {
        switch (t) {
            case 0:
                return '.';
            case 1:
                return '\u25a0'; // BOX
            default:
                return '?';
        }
    }


    public void draw(IDisplay display) {
        super.draw(display);

        LevelMap.MapView view = getView();
        for (int y = 0; y < view.height(); y++) {
            for (int x = 0; x < view.width(); x++) {
                display.draw(windowX + BORDER + x, windowY + BORDER + y, tileConverter(view.get(x, y)));
            }
        }

        for (GameObject object : view.getObjects()) {
            display.draw(view.viewX(object.getX()) + BORDER, view.viewY(object.getY()) + BORDER, object.getC());
        }

        currentView = null; // invalidate
    }

    private LevelMap.MapView getView() {
        if (currentView == null)
            currentView = map.view(viewPortX, viewPortY, width - 2 * BORDER, height - 2 * BORDER);

        return currentView;
    }

    private int adjust(int x, int min, int max) {
        if (x < min)
            return min;

        if (x > max)
            return max;

        return x;
    }

    public void scroll(Direction s) {
        viewPortX = adjust(viewPortX + s.dx, 0, map.getWidth() - (width - 2 * BORDER));
        viewPortY = adjust(viewPortY + s.dy, 0, map.getHeight() - (height - 2 * BORDER));
    }

    public void adjustForPlayer(GameObject player) {
        LevelMap.MapView view = getView();

        int viewX = view.viewX(player.getX());
        int viewY = view.viewY(player.getY());

        if (viewX < SCROLL_THRESHOLD)
            scroll(Direction.LEFT);

        if (viewX > width - SCROLL_THRESHOLD - 1)
            scroll(Direction.RIGHT);

        if (viewY < SCROLL_THRESHOLD)
            scroll(Direction.UP);

        if (viewY > height - SCROLL_THRESHOLD - 1)
            scroll(Direction.DOWN);
    }
}
