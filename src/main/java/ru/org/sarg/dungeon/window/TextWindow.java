package ru.org.sarg.dungeon.window;

import ru.org.sarg.dungeon.render.IDisplay;

public class TextWindow extends AbstractWindow {
    StringBuffer text = new StringBuffer();

    public TextWindow(int windowX, int windowY, int width, int height) {
        super(windowX, windowY, width, height);
        setPadding(1);
        setBorder(1);
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

        int delta = getBorder() + getPadding();
        int x = delta;
        int y = delta;

        for (int i = 0; i<text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                y++;
                x = delta;
            } else {
                display.draw(windowX + x++, windowY + y, c);
            }

            if (x >= width - delta) {
                x = delta;
                y++;
            }

            if (y >= height - delta)
                break;
        }
    }
}
