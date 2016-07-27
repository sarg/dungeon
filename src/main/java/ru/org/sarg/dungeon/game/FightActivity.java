package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.MathUtil;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.window.Input;
import ru.org.sarg.dungeon.window.TextWindow;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class FightActivity extends Activity {

    private final Consumer<Boolean> callback;
    private TextWindow dialog;
    private Input input;
    public FightActivity(IDisplay display, Consumer<Boolean> callback) {
        super(display);
        this.callback = callback;
    }

    public void onKeyDown(int key) {
        if (input == null)
            return;

        input.onKeyDown(key);
        if (input.length() > 0)
            input.onEnter();
    }

    @Override
    public void draw() {
        dialog.draw(display);
    }

    private void reset() {
        dialog.setText("Fight! Fight! Fight!\n" +
                "Rock Scissors Paper [r/s/p]: ");
        input.clear();
    }

    private Input enter(Input prev) {
        return new Input(0, Pattern.compile(""), () -> {
            input = prev;
            reset();
        });
    }

    @Override
    public void start() {
        int x = display.getWidth() / 3;
        int y = display.getHeight() / 3;
        dialog = new TextWindow(x, y, display.getWidth() - 1 - x, display.getHeight() - 1 - x);

        input = new Input(2, Pattern.compile("[rsp]"), () -> {
            if (input.length() == 0)
                return;

            RockPaper pen = RockPaper.roll();
            RockPaper you = RockPaper.fromChar(input.value());

            dialog.append(you + " --- " + pen + "\n");
            int cmp = you.compare(pen);
            if (cmp == 0) {
                dialog.append("Draw! Press enter to fight again!");
                input = enter(input);
            } else if (cmp > 0) {
                dialog.append("\nWin!");
                input = finish(true);
            } else {
                dialog.append("\nLose!");
                input = finish(false);
            }
        });
        reset();
    }

    private Input finish(boolean b) {
        return new Input(0, Pattern.compile(""), () -> callback.accept(b));
    }

    protected enum RockPaper {
        ROCK, PAPER, SCISSORS;

        public static RockPaper roll() {
            return RockPaper.values()[MathUtil.rand(RockPaper.values().length - 1)];
        }

        public static RockPaper fromChar(String s) {
            for (int i = 0; i < values().length; i++) {
                RockPaper rockPaper = values()[i];

                if (rockPaper.name().startsWith(s.toUpperCase()))
                    return rockPaper;
            }

            throw new RuntimeException("wat?");
        }

        public int compare(RockPaper other) {
            if (this == other)
                return 0;
            else if (this == ROCK)
                return other == PAPER ? -1 : 1;
            else if (this == PAPER)
                return other == SCISSORS ? -1 : 1;
            else if (this == SCISSORS)
                return other == ROCK ? -1 : 1;
            else
                throw new RuntimeException("wat?");
        }
    }
}
