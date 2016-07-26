package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.map.LevelMap;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.render.IWindow;

public class MapWindow extends IWindow {
    public static final int BORDER_WIDTH = 1;

    enum Scroll {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        final int dx, dy;

        Scroll(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    int viewPortX;
    int viewPortY;

    LevelMap map;

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

        LevelMap.MapView view = map.view(viewPortX, viewPortY, width - 2 * BORDER_WIDTH, height - 2 * BORDER_WIDTH);
        for (int y = 0; y < view.height(); y++) {
            for (int x = 0; x < view.width(); x++) {
                display.draw(windowX + 1 + x, windowY + 1 + y, tileConverter(view.get(x, y)));
            }
        }
    }

    private int adjust(int x, int min, int max) {
        if (x < min)
            return min;

        if (x > max)
            return max;

        return x;
    }

    public void scroll(Scroll s) {
        viewPortX = adjust(viewPortX + s.dx, 0, map.getWidth() - (width - 2 * BORDER_WIDTH));
        viewPortY = adjust(viewPortY + s.dy, 0, map.getHeight() - (height - 2 * BORDER_WIDTH));
    }
}
