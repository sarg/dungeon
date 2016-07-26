package ru.org.sarg.dungeon.map;

public class LevelMap {
    final int width;
    final int height;

    byte[] terrain;

    public LevelMap(int width, int height) {
        this.width = width;
        this.height = height;

        terrain = new byte[width * height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

        return map;
    }

    private static void t(LevelMap map, int row) {
        map.terrain[0 + row * map.width] = 1;
        map.terrain[1 + row * map.width] = 1;
        map.terrain[map.width - 2 + row * map.width] = 1;
        map.terrain[map.width - 1 + row * map.width] = 1;
    }

    public class MapView {
        final int vx, vy, vw, vh;

        private MapView(int vx, int vy, int vw, int vh) {
            this.vx = vx;
            this.vy = vy;
            this.vw = vw;
            this.vh = vh;
        }

        public int width() {
            return vw;
        }

        public int height() {
            return vh;
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
