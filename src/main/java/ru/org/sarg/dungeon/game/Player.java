package ru.org.sarg.dungeon.game;

public class Player {
    enum Race {
        BANANA_GUARD,
        DEMON,
        FLAME_PRINCE
    }

    public static class Builder {
        String name;
        Race race;

        public void name(String name) {
            this.name = name;
        }

        public void race(Race race) {
            this.race = race;
        }

        public Player build() {
            return new Player(name, race);
        }
    }

    final String name;
    final Race race;
    public int exp;

    public Player(String name, Race race) {
        this.name = name;
        this.race = race;
    }

}
