package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.Controls;
import ru.org.sarg.dungeon.Direction;
import ru.org.sarg.dungeon.Dungeon;
import ru.org.sarg.dungeon.game.objects.Player;
import ru.org.sarg.dungeon.utils.MathUtil;
import ru.org.sarg.dungeon.game.objects.DeadPenguin;
import ru.org.sarg.dungeon.game.objects.Penguin;
import ru.org.sarg.dungeon.game.objects.GameObject;
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
    private MapWindow mapWindow;
    private LevelMap map;
    private TextWindow stats;
    private PauseMenuActivity pauseMenuActivity;
    private FightActivity fightActivity;
    Player player;

    public GameActivity(IDisplay display) {
        super(display);
    }

    private void movePlayer(Direction d) {
        if (map.canMove(player.getX(), player.getY(), d)) {
            player.move(d);
            player.exp += 1;

            // FIXME: refactor interaction with other objects
            checkFight();
            mapWindow.scrollWithPlayer(player);
        }
    }

    private void checkFight() {
        List<GameObject> objects = map.getObjectsAt(player.getX(), player.getY());
        if (objects.size() > 1) {
            Optional<GameObject> penguin = objects.stream().filter(c -> c instanceof Penguin).findAny();
            if (penguin.isPresent()) {
                setFighting(penguin.get());
            }
        }
    }

    private boolean isPaused() {
        return pauseMenuActivity != null;
    }

    void setPaused(boolean p) {
        assert (isPaused() != p);

        if (p) {
            pauseMenuActivity = new PauseMenuActivity(display);
            pauseMenuActivity.start();
        } else {
            pauseMenuActivity = null;
        }
    }

    private boolean isFighting() {
        return fightActivity != null;
    }

    private void setFighting(GameObject f) {
        assert (isFighting() || f == null);

        if (f != null) {
            fightActivity = new FightActivity(display, (win) -> {
                if (win) {
                    DeadPenguin dead = new DeadPenguin();
                    dead.setX(f.getX());
                    dead.setY(f.getY());

                    map.getObjects().add(dead);

                    int dx = MathUtil.rand(10) * (MathUtil.rand(10) > 5 ? 1 : -1);
                    int dy = MathUtil.rand(10) * (MathUtil.rand(10) > 5 ? 1 : -1);

                    f.setX(MathUtil.adjust(f.getX() + dx, 0, map.getWidth() - 1));
                    f.setY(MathUtil.adjust(f.getY() + dy, 0, map.getHeight() - 1));

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

        if (key == Controls.QUIT) {
            setPaused(true);
            return;
        }

        if (isFighting()) {
            fightActivity.onKeyDown(key);
            return;
        }

        switch (key) {
            case Controls.DOWN:
                movePlayer(Direction.DOWN);
                break;

            case Controls.UP:
                movePlayer(Direction.UP);
                break;

            case Controls.LEFT:
                movePlayer(Direction.LEFT);
                break;

            case Controls.RIGHT:
                movePlayer(Direction.RIGHT);
                break;
        }
    }

    @Override
    public void draw() {
        stats.setText(String.format("%s %s\nExperience: %07d", player.getRace().name(), player.getName(), player.exp));

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

    void newGame(Player p) {
        Dungeon.INSTANCE.setActivity(GameActivity.INSTANCE);

        this.player = p;

        map = LevelMap.RANDOM();
        map.getObjects().add(p);
        mapWindow.setMap(map);
    }

    void loadSaveFile(Path path) {
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
            mapWindow.centerAt(player);
        } catch (IOException | ClassNotFoundException e) {
            // FIXME: show alert
            throw new RuntimeException("Unhandled exception", e);
        }
    }

    void saveToFile(Path path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeLong(1); // VERSION
            oos.writeObject(map);
        } catch (IOException e) {
            // FIXME: show alert
            throw new RuntimeException("Unhandled exception", e);
        }
    }
}
