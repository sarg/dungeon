package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.Dungeon;
import ru.org.sarg.dungeon.MathUtil;
import ru.org.sarg.dungeon.game.objects.DeadPenguin;
import ru.org.sarg.dungeon.game.objects.Penguin;
import ru.org.sarg.dungeon.map.GameObject;
import ru.org.sarg.dungeon.map.LevelMap;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.window.MapWindow;
import ru.org.sarg.dungeon.window.TextWindow;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class GameActivity extends Activity {
    private static final long SAVE_VERSION = 1L;
    public static GameActivity INSTANCE;
    MapWindow mapWindow;
    LevelMap map;
    TextWindow stats;
    PauseMenuActivity pauseMenuActivity;
    FightActivity fightActivity;
    Player player;

    public GameActivity(IDisplay display) {
        super(display);
    }

    private void move(Direction d) {
        if (map.canMove(player.getX(), player.getY(), d)) {
            player.move(d);
            player.exp += 1;

            // FIXME: refactor interaction with other objects
            List<GameObject> objects = map.getObjectsAt(player.getX(), player.getY());
            if (objects.size() > 1) {
                Optional<GameObject> penguin = objects.stream().filter(c -> c instanceof Penguin).findAny();
                if (penguin.isPresent()) {
                    setFighting(penguin.get());
                }
            }

            mapWindow.adjustForPlayer(player);
        }
    }

    public boolean isPaused() {
        return pauseMenuActivity != null;
    }

    public void setPaused(boolean p) {
        assert (isPaused() != p);

        if (p) {
            pauseMenuActivity = new PauseMenuActivity(display);
            pauseMenuActivity.start();
        } else {
            pauseMenuActivity = null;
        }
    }

    public boolean isFighting() {
        return fightActivity != null;
    }

    public void setFighting(GameObject f) {
        assert (isFighting() || f == null);

        if (f != null) {
            fightActivity = new FightActivity(display, (win) -> {
                if (win) {
                    DeadPenguin dead = new DeadPenguin();
                    dead.setX(f.getX());
                    dead.setY(f.getY());

                    map.getObjects().remove(f);
                    map.getObjects().add(dead);

                    Penguin alive = new Penguin();
                    alive.setX(MathUtil.rand(map.getWidth() - 1));
                    alive.setY(MathUtil.rand(map.getHeight() - 1));
                    map.getObjects().add(alive);

                    player.exp += 10;
                } else {
                    player.exp = Math.max(0, player.exp - 10);
                }

                fightActivity = null;
            });
            fightActivity.start();
        }
    }

    @Override
    public void onKeyDown(int key) {
        if (isPaused()) {
            pauseMenuActivity.onKeyDown(key);
            return;
        }

        if (key == 'q') {
            setPaused(true);
            return;
        }

        if (isFighting()) {
            fightActivity.onKeyDown(key);
            return;
        }

        switch (key) {
            case 'j':
                move(Direction.DOWN);
                break;

            case 'k':
                move(Direction.UP);
                break;

            case 'h':
                move(Direction.LEFT);
                break;

            case 'l':
                move(Direction.RIGHT);
                break;
        }
    }

    @Override
    public void draw() {
        stats.setText(String.format("%s %s\nExperience: %07d", player.race.name(), player.name, player.exp));

        mapWindow.draw(display);
        stats.draw(display);

        if (isFighting())
            fightActivity.draw();

        if (isPaused())
            pauseMenuActivity.draw();
    }

    @Override
    public void start() {
        stats = new TextWindow(0, display.getHeight() - 1 - 6, display.getWidth(), 6);
        mapWindow = new MapWindow(0, 0, display.getWidth(), display.getHeight() - stats.getHeight());
    }

    public void newGame(Player p) {
        Dungeon.INSTANCE.setActivity(GameActivity.INSTANCE);

        this.player = p;

        map = LevelMap.RANDOM();
        map.getObjects().add(p);
        mapWindow.setMap(map);
    }

    public void loadSaveFile(Path path) {
        Dungeon.INSTANCE.setActivity(this);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            long version = ois.readLong();

            if (version != SAVE_VERSION) {
                // FIXME: show alert
                throw new RuntimeException("Unknown save format");
            }

            map = (LevelMap) ois.readObject();
            mapWindow.setMap(map);
            player = (Player) map.getObjects().stream().filter(c -> c.getClass().equals(Player.class)).findFirst().get();
        } catch (IOException | ClassNotFoundException e) {
            // FIXME: show alert
            throw new RuntimeException("Unhandled exception", e);
        }
    }

    public void saveToFile(Path path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeLong(1); // VERSION
            oos.writeObject(map);
        } catch (IOException e) {
            // FIXME: show alert
            throw new RuntimeException("Unhandled exception", e);
        }
    }
}
