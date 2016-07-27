package ru.org.sarg.dungeon.window;

import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

public class Input {
    final int maxLength;
    StringBuilder sb;
    Pattern p;
    Runnable callback;

    public Input(int maxLength, Pattern p, Runnable callback) {
        this.maxLength = maxLength;
        this.sb = new StringBuilder();
        this.p = p;
        this.callback = callback;
    }

    public int length() {
        return sb.length();
    }

    private boolean handleSpecial(int key) {
        switch (key) {
            case KeyEvent.VK_DELETE:
                if (sb.length() > 0)
                    sb.delete(sb.length() - 1, sb.length());
                break;

            case KeyEvent.VK_ENTER:
                callback.run();
                break;

            default:
                return false;
        }

        return true;
    }

    public void onKeyDown(int key) {
        if (handleSpecial(key))
            return;

        char c = (char) key;
        if (p.matcher(String.valueOf(c)).matches() && sb.length() < maxLength)
            sb.append((char) key);
    }

    public String value() {
        return sb.toString();
    }

    public void clear() {
        sb = new StringBuilder();
    }

    public void onEnter() {
        callback.run();
    }
}
