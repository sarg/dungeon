package ru.org.sarg.dungeon.render;

import org.junit.Test;
import ru.org.sarg.dungeon.map.LevelMap;
import ru.org.sarg.dungeon.window.MapWindow;

import static org.junit.Assert.assertEquals;

public class CliDisplayTest {
    @org.junit.Test
    public void getSafeLength() throws Exception {
        CliDisplay cd = new CliDisplay(10, 10);

        assertEquals(5, cd.getSafeLength(0, "text1"));
        assertEquals(1, cd.getSafeLength(9, "text1"));

        assertEquals(3, cd.getSafeLength(7, "text1"));
    }

    @Test
    public void testAll() {
        CliDisplay ds = new CliDisplay(20, 20);

        ds.draw(3, 3, 'О', IDisplay.Color.WHITE);
        ds.draw(3, 5, 'О', IDisplay.Color.WHITE);
        ds.rect(0, 0, ds.getWidth() - 1, ds.getHeight() - 1, IDisplay.Color.WHITE);
        ds.flush();
    }

    @Test
    public void testMap() {
        CliDisplay ds = new CliDisplay(20, 20);
        LevelMap map = LevelMap.RANDOM();

        MapWindow mapWindow = new MapWindow(0, 0, 15, 15);
        mapWindow.setMap(map);

        mapWindow.draw(ds);
        ds.flush();
    }

}