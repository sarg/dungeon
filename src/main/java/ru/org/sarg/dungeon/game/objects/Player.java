package ru.org.sarg.dungeon.game.objects;

import ru.org.sarg.dungeon.Direction;

public class Player extends GameObject {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final Race race;
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

    public String getName() {
        return name;
    }

    public Race getRace() {
        return race;
    }

    public enum Race {
        BANANA_GUARD,
        DEMON,
        FLAME_PRINCE
    }

    public static class Builder {
        public String name;
        public Race race;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder race(Race race) {
            this.race = race;
            return this;
        }

        public Player build() {
            return new Player(name, race);
        }
    }

}
