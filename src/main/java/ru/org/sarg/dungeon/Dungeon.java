package ru.org.sarg.dungeon;

import ru.org.sarg.dungeon.game.Game;

import java.io.IOException;

public class Dungeon {
    public static void main(String[] args) throws IOException {
        Game game = new Game();
        game.init();
        game.start();
    }
}
