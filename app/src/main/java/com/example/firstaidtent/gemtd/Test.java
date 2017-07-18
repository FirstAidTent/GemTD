package com.example.firstaidtent.gemtd;

public class Test {
    static void drawGrid(SquareGrid grid, AStar astar) {
        int i = -1;
        int j = -1;
        boolean onPath = false;
        Node id;
        Node ptr;

        for (int y = 0; y <= grid.getHeight(); y += 22) {
            if (j % 2 != 0 && j >= 0) {
                y += 1;
            }
            j++;

            for (int x = 0; x <= grid.getWidth(); x += 22) {
                if (i % 2 != 0 && i >= 0) {
                    x += 1;
                }
                i++;

                id = grid.getClosestNode(x, y);
                ptr = id;
                if (astar.getCameFrom().containsKey(id)) {
                    ptr = astar.getCameFrom().get(id);
                }
                onPath = astar.getCameFrom().containsValue(id);
                if (!id.isPassable()) {
                    System.out.print("##\t");
                } else if (id.equals(astar.getStart())) {
                    System.out.print("S\t");
                } else if (id.equals(astar.getGoal())) {
                    System.out.print("G\t");
                } else if ((ptr.x == x + 22 || ptr.x == x + 23) && onPath) {
                    if ((ptr.y == y + 22 || ptr.y == y + 23)) {
                        System.out.print("\u2198\t"); // Down-Right arrow
                    } else if ((ptr.y == y - 22 || ptr.y == y - 23)) {
                        System.out.print("\u2197\t"); // Up-Right arrow
                    } else {
                        System.out.print("\u2192\t"); // Right arrow
                    }
                } else if ((ptr.x == x - 22 || ptr.x == x - 23) && onPath) {
                    if ((ptr.y == y + 22 || ptr.y == y + 23)) {
                        System.out.print("\u2199\t"); // Down-Left arrow
                    } else if ((ptr.y == y - 22 || ptr.y == y - 23)) {
                        System.out.print("\u2196\t"); // Up-Left arrow
                    } else {
                        System.out.print("\u2190\t"); // Left arrow
                    }
                } else if ((ptr.y == y + 22 || ptr.y == y + 23) && onPath) {
                    System.out.print("\u2193\t"); // Down arrow
                } else if ((ptr.y == y - 22 || ptr.y == y - 23) && onPath) {
                    System.out.print("\u2191\t"); // Up arrow
                } else {
                    System.out.print("*\t");
                }
            }
            i = -1;
            System.out.println(y);
        }
        for (double k = 0; k < grid.getWidth() / 45 + 0.5; k += 0.5) {
            System.out.print((int) (45 * k) + "\t");
        }
    }

    public static void main(String[] args) {
        SquareGrid grid = new SquareGrid(1280, 720, 45, 2);
        int j = -1;

//        for (int y = 22; y <= 450; y += 22) {
//            if (j % 2 != 0 && j >= 0) {
//                y += 1;
//            }
//
//            grid.getClosestNode(180, y).setPassable(false);
//            j++;
//        }
//        j = -1;
//        for (int y = 0; y <= 427; y += 22) {
//            if (j % 2 != 0 && j >= 0) {
//                y += 1;
//            }
//
//            grid.getClosestNode(225, y).setPassable(false);
//            j++;
//        }

        for (int y = 22; y <= 450; y += 22) {
            if (j % 2 != 0 && j >= 0) {
                y += 1;
            }

            grid.getClosestNode(450 - y, y).setPassable(false);
            j++;
        }

        j = 0;
        for (int y = 0; y <= 495; y += 22) {
            if (j % 2 != 0 && j >= 0) {
                y += 1;
            }

            grid.getClosestNode(517 - y, y).setPassable(false);
            j++;
        }


        // Run A*
        AStar astar = new AStar(grid, 0, 0, 720, 360);

        drawGrid(grid, astar);
    }
}
