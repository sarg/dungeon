package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.map.LevelMap;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.render.IWindow;

public class MapWindow extends IWindow {
    int viewPortX;
    int viewPortY;

    LevelMap map;

    public MapWindow(int windowX, int windowY, int width, int height) {
        super(width, height, windowX, windowY);
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

        LevelMap.MapView view = map.view(viewPortX, viewPortY, width-2, height-2);
        for (int y = 0; y < view.height(); y++) {
            for (int x = 0; x < view.width(); x++) {
                display.draw(windowX + 1 + x, windowY + 1 + y, tileConverter(view.get(x, y)));
            }
        }
    }
}
