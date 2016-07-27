package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.Dungeon;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.window.TextWindow;

import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CharacterCreateActivity extends Activity {
    List<String> phrases = Arrays.asList(
            "Hi, Finn. Let's go explore ice kings chambers? Maybe we'll find something secret there.",
            "But Jake, what if he sees us. He will be furious. He won't us play his drumset for a week!",
            "Yeah, bro, you are right. I have an idea. Let's disguise and change our names. He'll never recognize us.",

            "I'll be Hund, and you?",
            "%name",

            "Ha-ha-ha, [name], that's funny.",
            "And what will be your disguise?",
            "I don't know Jake, sorry, Hund. Maybe you'll suggest me something.",

            "Yeah, sure. I have some costumes since last party. Take your choice:",
            Arrays.asList(Player.Race.values()).stream()
                    .map(r -> " - " + r.name() + "(" + r.name().substring(0,1).toLowerCase() + ")")
                    .collect(Collectors.joining("\n")),
            "%race",

            "Okay, suit yourself. It's adventure time!",
            "PS, I heard there will be a lot of penguins, and they like to tickle intruders until they retreat. Be aware and prepare to tickle back!",
            "%enter"
    );

    int idx;

    private TextWindow dialog;
    private FpsCounter fpsCounter;
    private Input input;

    private Player.Builder player;

    private static class Input {
        final int maxLength;
        StringBuilder sb;
        Pattern p;
        Runnable callback;

        private Input(int maxLength, Pattern p, Runnable callback) {
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
    }

    public CharacterCreateActivity(IDisplay display) {
        super(display);
    }

    public void onKeyDown(int key) {
        if (input == null)
            return;

        if (key == KeyEvent.VK_ENTER) {
            input.callback.run();
        } else {
            dialog.deleteLast(input.length());
            input.onKeyDown(key);
            dialog.append(input.value());
        }
    }

    @Override
    public void start() {
        dialog = new TextWindow(0, 0, display.getWidth(), display.getHeight());
        fpsCounter = new FpsCounter(0.5);
        player = new Player.Builder();
        idx = 0;

        if (Dungeon.DEBUG) {
            player.name("test");
            player.race(Player.Race.BANANA_GUARD);
            gogogo();
        }
    }

    private void name() {
        input = new Input(10, Pattern.compile("[a-z]"), () -> {
            if (input.value().isEmpty())
                return;

            player.name(input.value());
            dialog.append("\n");
            input = null;
        });
    }

    private void race() {
        input = new Input(1, Pattern.compile("[a-z]"), () -> {
            if (input.value().isEmpty())
                return;

            for (Player.Race race : Player.Race.values()) {
                if (race.name().startsWith(input.value().toUpperCase())) {
                    player.race(race);
                }
            }

            dialog.append("\n");
            if (player.race != null) {
                input = null;
            } else {
                dialog.append("what?\n");
                input.clear();
            }
        });
    }

    private void gogogo() {
        GameActivity.INSTANCE.setPlayer(player.build());
        Dungeon.INSTANCE.setActivity(GameActivity.INSTANCE);
    }

    private void enter() {
        input = new Input(0, Pattern.compile(""), this::gogogo);
    }

    @Override
    public void draw() {
        if (input == null && fpsCounter.isReadyForNextFrame()) {
            String text = phrases.get(idx++);
            if (text.startsWith("%")) {
                try {
                    this.getClass().getDeclaredMethod(text.substring(1)).invoke(this);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException("Unhandled exception", e);
                }
            } else {
                if (player.name != null)
                    text = text.replace("[name]", player.name);
                dialog.append(text);
                dialog.append("\n");
            }
        }

        dialog.draw(display);
    }
}
