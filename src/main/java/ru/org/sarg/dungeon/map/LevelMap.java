package ru.org.sarg.dungeon.map;

import ru.org.sarg.dungeon.game.objects.Penguin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LevelMap {
    final int width;
    final int height;

    byte[] terrain;
    List<GameObject> objects;

    public LevelMap(int width, int height) {
        this.width = width;
        this.height = height;

        terrain = new byte[width * height];
        objects = new ArrayList<>();
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

    public static LevelMap RANDOM() {
        LevelMap map = new LevelMap(100, 100);
        for (int i = 0; i < map.terrain.length; i++) {
            map.terrain[i] = (byte) (Math.random() * 10 > 5 ? 1 : 0);
        }

        t(map, 0);
        t(map, 1);
        t(map, map.height-1);
        t(map, map.height-2);

        for (int i = 0; i < 20; i++) {
            int x = (int) Math.round(Math.random() * (map.width-1));
            int y = (int) Math.round(Math.random() * (map.height-1));

            Penguin penguin = new Penguin();
            penguin.setX(x);
            penguin.setY(y);

            map.getObjects().add(penguin);
        }

        return map;
    }

    private static void t(LevelMap map, int row) {
        map.terrain[0 + row * map.width] = 1;
        map.terrain[1 + row * map.width] = 1;
        map.terrain[map.width - 2 + row * map.width] = 1;
        map.terrain[map.width - 1 + row * map.width] = 1;
    }

    public class MapView {
        final int vx, vy, viewWidth, viewHeight;

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

        private MapView(int vx, int vy, int viewWidth, int viewHeight) {
            this.vx = vx;
            this.vy = vy;
            this.viewWidth = viewWidth;
            this.viewHeight = viewHeight;

            objectsView = objects.stream().filter(this::inView)
                    .collect(Collectors.toList());
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

        private Collection<GameObject> objectsView;

        public Collection<GameObject> getObjects() {
            return objectsView;
        }

        public byte get(int x, int y) {
            return terrain[(y + vy) * width + vx + x];
        }
    }

    public MapView view(int x, int y, int w, int h) {
        return new MapView(x, y, w, h);
    }

/*    public byte[] view(int x, int y, int w, int h) {
        assert (x + w < width);
        assert (y + h < height);

        byte[] ret = new byte[w * h];
        int ptr = y * width + x;
        int retPtr = 0;
        while (h-- > 0) {
            System.arraycopy(terrain, ptr, ret, retPtr, w);
            retPtr += w;
            ptr += width;
        }

        return ret;
    }*/
}
