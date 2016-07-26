package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.render.IWindow;

public class TextWindow extends IWindow {
    public static final int PADDING = 1;
    public static final int BORDER = 1;

    StringBuffer text = new StringBuffer();

    public TextWindow(int windowX, int windowY, int width, int height) {
        super(windowX, windowY, width, height);
    }

    public void setText(String text) {
        this.text = new StringBuffer(text);
    }

    public void append(String text) {
        this.text.append(text);
    }

    public StringBuffer deleteLast(int i) {
        return text.delete(text.length() - i, text.length());
    }

    public void clear() {
        text = new StringBuffer();
    }

    @Override
    public void draw(IDisplay display) {
        super.draw(display);

        int x = BORDER + PADDING;
        int y = BORDER + PADDING;

        for (int i = 0; i<text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                y++;
                x = BORDER + PADDING;
            } else {
                display.draw(windowX + x++, windowY + y, c);
            }

            if (x >= width - BORDER - PADDING) {
                x = BORDER + PADDING;
                y++;
            }

            if (y >= height - BORDER - PADDING)
                break;
        }
    }
}
