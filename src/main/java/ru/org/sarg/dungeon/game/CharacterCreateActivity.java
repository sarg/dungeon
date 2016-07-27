package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.Controls;
import ru.org.sarg.dungeon.Dungeon;
import ru.org.sarg.dungeon.game.objects.Player;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.utils.FpsCounter;
import ru.org.sarg.dungeon.window.Input;
import ru.org.sarg.dungeon.window.TextWindow;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CharacterCreateActivity extends Activity {
    final List<String> phrases = Arrays.asList(
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
                    .map(r -> " - " + r.name() + "(" + r.name().substring(0, 1).toLowerCase() + ")")
                    .collect(Collectors.joining("\n")),
            "%race",

            "Okay, suit yourself. It's adventure time!",
            "PS, I heard there will be a lot of penguins, and they like to play rock-scissors-paper. Be prepared!",
            "%enter"
    );

    int idx;

    private TextWindow dialog;
    private FpsCounter fpsCounter;
    private Input input;

    private Player.Builder player;

    public CharacterCreateActivity(IDisplay display) {
        super(display);
    }

    @Override
    public void onKeyDown(int key) {
        if (input == null)
            return;

        if (key == Controls.ENTER) {
            input.onEnter();
        } else {
            dialog.deleteLast(input.length());
            input.onKeyDown(key);
            dialog.append(input.value());
        }
    }

    @Override
    public void start() {
        dialog = new TextWindow(0, 0, display.getWidth() - 1, display.getHeight() - 1);
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
        GameActivity.INSTANCE.newGame(player.build());
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
                fpsCounter.onFrame();
            }
        }

        dialog.draw(display);
    }
}
