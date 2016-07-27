package ru.org.sarg.dungeon.map;

import ru.org.sarg.dungeon.game.objects.GameObject;
import ru.org.sarg.dungeon.utils.MathUtil;
import ru.org.sarg.dungeon.Direction;
import ru.org.sarg.dungeon.game.objects.Penguin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LevelMap implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int width;
    private final int height;

    private byte[] terrain;
    private List<GameObject> objects;

    private LevelMap(int width, int height) {
        this.width = width;
        this.height = height;

        terrain = new byte[width * height];
        objects = new ArrayList<>();
    }

    private static void vline(LevelMap map, int x, int y, int l, Direction d) {
        while (l-- >= 0 && y > 0 && y < map.height) {
            map.terrain[y * map.width + x] = 2;
            y += d.dy;
        }
    }

    private static void hline(LevelMap map, int x, int y, int l, Direction d) {
        while (l-- >= 0 && x > 0 && x < map.width) {
            map.terrain[y * map.width + x] = 3;
            x += d.dx;
        }
    }

    private static void cross(LevelMap map) {
        int x = MathUtil.rand(map.width - 1);
        int y = MathUtil.rand(map.height - 1);
        int l = MathUtil.rand(10);

        Direction vertDir = MathUtil.rand(10) > 5 ? Direction.UP : Direction.DOWN;
        Direction horDir = MathUtil.rand(10) > 5 ? Direction.LEFT : Direction.RIGHT;

        hline(map, x, y, l, horDir);
        vline(map, x, y, l, vertDir);
    }

    public static LevelMap RANDOM() {
        LevelMap map = new LevelMap(110, 100);
        for (int i = 0; i < 50; i++) {
            cross(map);
        }

        for (int i = 0; i < 20; i++) {
            int x = MathUtil.rand(map.width - 1);
            int y = MathUtil.rand(map.height - 1);

            Penguin penguin = new Penguin();
            penguin.setX(x);
            penguin.setY(y);

            map.getObjects().add(penguin);
        }

        return map;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public boolean canMove(int x, int y, Direction d) {
        x += d.dx;
        y += d.dy;

        if (x >= width || y >= height || x < 0 || y < 0)
            return false;

        return get(x, y) == 0;
    }

    // FIXME: slow, use better data structure
    public List<GameObject> getObjectsAt(int x, int y) {
        return objects.stream()
                .filter(c -> c.getY() == y && c.getX() == x)
                .collect(Collectors.toList());
    }

    private byte get(int x, int y) {
        assert(x < width && x >= 0);
        assert(y < height && y >= 0);
        return terrain[y * width + x];
    }

    public MapView view(int x, int y, int w, int h) {
        return new MapView(x, y, w, h);
    }

    public class MapView {
        final int vx, vy, viewWidth, viewHeight;
        private Collection<GameObject> objectsView;

        private MapView(int vx, int vy, int viewWidth, int viewHeight) {
            this.vx = vx;
            this.vy = vy;
            this.viewWidth = viewWidth;
            this.viewHeight = viewHeight;

            objectsView = objects.stream().filter(this::inView)
                    .sorted((a, b) -> Integer.compare(a.getzOrder(), b.getzOrder()))
                    .collect(Collectors.toList());
        }

        private boolean inView(GameObject obj) {
            int x = obj.getX();
            int y = obj.getY();

            if (x >= vx && x < vx + viewWidth) {
                if (y >= vy && y < vy + viewHeight) {
                    return true;
                }
            }
            return false;
        }

        public int viewX(int realX) {
            return realX - vx;
        }

        public int viewY(int realY) {
            return realY - vy;
        }

        public int width() {
            return viewWidth;
        }

        public int height() {
            return viewHeight;
        }

        public Collection<GameObject> getObjects() {
            return objectsView;
        }

        public byte get(int x, int y) {
            return LevelMap.this.get(x + vx, y + vy);
        }
    }
}
