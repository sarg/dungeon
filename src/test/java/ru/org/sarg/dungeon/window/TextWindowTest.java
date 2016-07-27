package ru.org.sarg.dungeon.window;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextWindowTest {
    @Test
    public void deleteLast() throws Exception {
        TextWindow wnd = new TextWindow(0, 0, 5, 5);
        wnd.setText("testtest");
        wnd.deleteLast(4);

        assertEquals(wnd.text.toString(), "test");

        wnd.deleteLast(10);
        assertEquals(wnd.text.toString(), "testtes");
    }

}