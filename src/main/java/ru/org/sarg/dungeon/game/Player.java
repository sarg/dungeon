package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.map.GameObject;

public class Player extends GameObject {
    final String name;
    final Race race;
    public int exp;

    public Player(String name, Race race) {
        super('P', Integer.MAX_VALUE);
        this.name = name;
        this.race = race;
    }

    public void move(Direction dir) {
        x += dir.dx;
        y += dir.dy;
    }
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

}
