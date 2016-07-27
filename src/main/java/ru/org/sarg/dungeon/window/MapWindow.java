package ru.org.sarg.dungeon.window;

import ru.org.sarg.dungeon.game.Direction;
import ru.org.sarg.dungeon.map.GameObject;
import ru.org.sarg.dungeon.map.LevelMap;
import ru.org.sarg.dungeon.render.IDisplay;

public class MapWindow extends AbstractWindow {
    public static final int SCROLL_THRESHOLD = 15;

    int viewPortX;
    int viewPortY;

    LevelMap map;
    LevelMap.MapView currentView;

    public MapWindow(int windowX, int windowY, int width, int height) {
        super(windowX, windowY, width, height);
        setBorder(1);
    }

    public void setMap(LevelMap map) {
        this.map = map;
    }

    private char tileConverter(byte t) {
        switch (t) {
            case 0:
                return ' ';
            case 1:
                return '\u253c'; // BOX
            case 2:
                return '\u2502'; // │
            case 3:
                return '\u2500'; // ─────
            default:
                return '?';
        }
    }


    public void draw(IDisplay display) {
        super.draw(display);

        LevelMap.MapView view = getView();
        for (int y = 0; y < view.height(); y++) {
            for (int x = 0; x < view.width(); x++) {
                display.draw(windowX + getBorder() + x, windowY + getBorder() + y, tileConverter(view.get(x, y)), IDisplay.Color.RED);
            }
        }

        for (GameObject object : view.getObjects()) {
            display.draw(view.viewX(object.getX()) + getBorder(), view.viewY(object.getY()) + getBorder(), object.getC(), IDisplay.Color.WHITE);
        }

        currentView = null; // invalidate
    }

    private LevelMap.MapView getView() {
        if (currentView == null)
            currentView = map.view(viewPortX, viewPortY, width - 2 * getBorder(), height - 2 * getBorder());

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
        int newViewPortX = adjust(viewPortX + s.dx, 0, map.getWidth() - (width - 2 * getBorder()));
        int newViewPortY = adjust(viewPortY + s.dy, 0, map.getHeight() - (height - 2 * getBorder()));

        if (newViewPortX != viewPortX || newViewPortY != viewPortY)
            currentView = null; // invalidate after scroll

        viewPortX = newViewPortX;
        viewPortY = newViewPortY;
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
