package turing;

import java.util.Random;

public class Tape {
    public final int width;
    public final int height;
    private final byte[] tape;

    public Tape(int width, int height) {
        this.width = width;
        this.height = height;

        this.tape = new byte[width * height];
    }

    public byte readAt(int x, int y) {
        return tape[y * width + x];
    }

    public void writeAt(int x, int y, byte val) {
        tape[y * width + x] = val;
    }

    public void move(Coord coord, Direction direction) {
        switch (direction) {
            case Up -> {
                coord.y = wrapCoord(coord.y + 1, height);
//                new Coord(x, wrapCoord(y + 1, height));
            }
            case Down -> {
                coord.y = wrapCoord(coord.y - 1, height);
//                new Coord(x, wrapCoord(y - 1, height));
            }
            case Left -> {
                coord.x = wrapCoord(coord.x - 1, width);
//                new Coord(wrapCoord(x - 1, width), y);
            }
            case Right -> {
                coord.x = wrapCoord(coord.x + 1, width);
//                new Coord(wrapCoord(x + 1, width), y);
            }
        };
//        return new Coord(x, y);
    }

    private int wrapCoord(int n, int bound) {
        return n >= bound ? 0 : (n < 0 ? bound - 1 : n);
    }

    public static final class Coord {
        public int x;
        public int y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Coord[" +
                    "x=" + x + ", " +
                    "y=" + y + ']';
        }
    }
    public enum Direction {
        Up,
        Down,
        Left,
        Right;

        public static Direction randomDir(Random random) {
            return values()[random.nextInt(4)];
        }
    }
}
