package ru.org.sarg.dungeon.render;

import org.junit.Test;

import static org.junit.Assert.*;

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

        ds.draw(3, 5, 'О');
        ds.rect(0, 0, ds.getWidth() - 1, ds.getHeight() - 1);
        ds.flush();
    }

}