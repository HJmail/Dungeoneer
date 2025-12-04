package model;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * DungeonGenerator – DungeonTile-only version
 * -------------------------------------------
 * This generator:
 *  • builds a maze layout using DFS
 *  • sets START and EXIT tiles
 *  • randomly places pillars, pits, items, and monsters
 *  • uses DungeonTile exclusively
 *  • sets Room fields using the REAL Room API
 */
public class DungeonGenerator {

    /** Main entry point for generating a dungeon. */
    public static Dungeon generate(final Random rng, final int difficulty, final Hero hero) {

        Dungeon dungeon = new Dungeon(hero, difficulty);

        int maxDepth = createLayout(dungeon, rng);
        createEvents(dungeon, rng, maxDepth);

        return dungeon;
    }

    // ----------------------------------------------------------------------
    // STEP 1: CREATE SPANNING TREE MAZE & DETERMINE START + EXIT
    // ----------------------------------------------------------------------

    private static int createLayout(final Dungeon dungeon, final Random rng) {

        int rows = dungeon.getRows();
        int cols = dungeon.getCols();

        boolean[][] visited = new boolean[rows][cols];

        // Pick random start room
        int startRow = rng.nextInt(rows);
        int startCol = rng.nextInt(cols);

        visited[startRow][startCol] = true;

        Room startRoom = dungeon.getRoom(startRow, startCol);
        startRoom.setTile(DungeonTile.ENTRANCE);
        startRoom.setDepth(0);

        // DFS stack
        Deque<Point> stack = new ArrayDeque<>();
        stack.push(new Point(startRow, startCol));

        int maxDepth = 0;
        Point deepestRoom = new Point(startRow, startCol);

        while (!stack.isEmpty()) {

            Point current = stack.peek();
            int r = current.x;
            int c = current.y;

            List<Point> neighbors = getUnvisitedNeighbors(r, c, rows, cols, visited);

            if (!neighbors.isEmpty()) {

                Point next = neighbors.get(rng.nextInt(neighbors.size()));
                int nr = next.x;
                int nc = next.y;

                visited[nr][nc] = true;

                Room currRoom = dungeon.getRoom(r, c);
                Room nextRoom = dungeon.getRoom(nr, nc);

                // Connect rooms
                Direction d = getDirection(new int[]{r, c}, new int[]{nr, nc});
                currRoom.getDirections().add(d);
                nextRoom.getDirections().add(d.opposite());

                // Depth logic
                int nextDepth = currRoom.getDepth() + 1;
                nextRoom.setDepth(nextDepth);

                if (nextDepth > maxDepth) {
                    maxDepth = nextDepth;
                    deepestRoom = next;
                }

                stack.push(next);

            } else {
                stack.pop();
            }
        }

        // Set EXIT tile at deepest room
        Room exitRoom = dungeon.getRoom(deepestRoom.x, deepestRoom.y);
        exitRoom.setTile(DungeonTile.EXIT);

        return maxDepth;
    }

    // ----------------------------------------------------------------------
    // STEP 2: PLACE EVENTS BASED ON DEPTH & RNG
    // ----------------------------------------------------------------------

    private static void createEvents(final Dungeon dungeon, final Random rng, final int maxDepth) {

        int rows = dungeon.getRows();
        int cols = dungeon.getCols();

        // Collect all possible pillar rooms (excluding START & EXIT)
        List<Point> candidates = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                Room room = dungeon.getRoom(r, c);

                if (room.getTile() == DungeonTile.ENTRANCE || room.getTile() == DungeonTile.EXIT) {
                    continue;
                }

                candidates.add(new Point(r, c));
            }
        }

        // Shuffle for randomness
        Collections.shuffle(candidates, rng);

        // --- Place 4 pillars in random rooms ---
        DungeonTile[] pillarTiles = {
            DungeonTile.ABSTRACTION_PILLAR,
            DungeonTile.ENCAPSULATION_PILLAR,
            DungeonTile.INHERITANCE_PILLAR,
            DungeonTile.POLYMORPHISM_PILLAR
        };

        for (int i = 0; i < 4; i++) {
            Point p = candidates.get(i);
            Room room = dungeon.getRoom(p.x, p.y);

            room.setTile(pillarTiles[i]);
            room.addItem(new Pillar(pillarTiles[i].name().charAt(0))); // A/E/I/P
        }

        // Fill in the rest of the dungeon
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                Room room = dungeon.getRoom(r, c);

                if (room.getTile() == DungeonTile.ENTRANCE ||
                    room.getTile() == DungeonTile.EXIT ||
                    isPillarTile(room.getTile()))
                    continue;

                int depth = room.getDepth();
                int roll = rng.nextInt(100);

                // ------------------------------------------------------------------
                // VERY SHALLOW AREAS (depth <= 1)
                // ------------------------------------------------------------------
                if (depth <= 1) {
                    if (roll < 60) {
                        room.setTile(DungeonTile.FLOOR);
                    } else {
                        generateTreasure(room, rng);
                    }
                    continue;
                }

                // ------------------------------------------------------------------
                // MID-DEPTH AREAS
                // ------------------------------------------------------------------
                if (depth < maxDepth * 0.5) {

                    if (roll <= 20) {                      // ENCOUNTER
                        generateEncounter(room, rng);

                    } else if (roll <= 60) {               // TREASURE
                        generateTreasure(room, rng);

                    } else if (roll <= 75) {               // PIT
                        room.setTile(DungeonTile.PIT);
                        room.setPit(true);

                    } else {                               // EMPTY
                        room.setTile(DungeonTile.FLOOR);
                    }

                    continue;
                }

                // ------------------------------------------------------------------
                // DEEP AREAS
                // ------------------------------------------------------------------
                if (roll <= 50) {                           // ENCOUNTER + WEAPON
                    generateEncounter(room, rng);
                    generateWeapon(room, rng);

                } else if (roll <= 80) {                    // TREASURE
                    generateTreasure(room, rng);

                } else if (roll <= 90) {                    // PIT
                    room.setTile(DungeonTile.PIT);
                    room.setPit(true);

                } else {                                    // EMPTY
                    room.setTile(DungeonTile.FLOOR);
                }
            }
        }
    }

    // ----------------------------------------------------------------------
    // HELPER: ENCOUNTER
    // ----------------------------------------------------------------------

    private static void generateEncounter(Room room, Random rng) {

    	room.setTile(DungeonTile.FLOOR);

        int depth = room.getDepth();
        int count = (depth <= 3) ? 1 : (depth <= 5 ? 2 : 3);

        for (int i = 0; i < count; i++) {
            room.addMonster(getMonsterForDepth(depth, rng));
        }
    }

    // ----------------------------------------------------------------------
    // HELPER: TREASURE (GOLD / POTIONS)
    // ----------------------------------------------------------------------

    private static void generateTreasure(Room room, Random rng) {

        int roll = rng.nextInt(3);

        switch (roll) {
        case 0 -> {
            room.setTile(DungeonTile.GOLD);
        }

        case 1 -> {
            room.setTile(DungeonTile.HEALING_POTION);
            room.addItem(new HealingPotion(25, Rarity.COMMON));
        }
        case 2 -> {
            room.setTile(DungeonTile.VISION_POTION);
            room.addItem(new VisionPotion(3, Rarity.COMMON));
        }
        }
    }

    // ----------------------------------------------------------------------
    // HELPER: WEAPON
    // ----------------------------------------------------------------------

    private static void generateWeapon(Room room, Random rng) {

        DungeonTile[] weapons = {
            DungeonTile.STICK,
            DungeonTile.SPEAR,
            DungeonTile.FALCHION,
            DungeonTile.FLAIL,
            DungeonTile.MORNING_STAR
        };

        DungeonTile tile = weapons[rng.nextInt(weapons.length)];
        room.setTile(tile);

        Weapon w = switch (tile) {
            case STICK        -> new Weapon("Stick", 8, Rarity.COMMON);
            case SPEAR        -> new Weapon("Spear", 10, Rarity.UNCOMMON);
            case FALCHION     -> new Weapon("Falchion", 12, Rarity.UNCOMMON);
            case FLAIL        -> new Weapon("Flail", 14, Rarity.RARE);
            case MORNING_STAR -> new Weapon("Morning Star", 16, Rarity.RARE);
            default           -> new Weapon("Sword", 10, Rarity.COMMON);
        };

        room.addItem(w);
    }

    // ----------------------------------------------------------------------
    // SUPPORT CODE
    // ----------------------------------------------------------------------

    private static boolean isPillarTile(DungeonTile t) {
        return switch (t) {
            case ABSTRACTION_PILLAR,
                 ENCAPSULATION_PILLAR,
                 INHERITANCE_PILLAR,
                 POLYMORPHISM_PILLAR -> true;
            default -> false;
        };
    }

    private static List<Point> getUnvisitedNeighbors(int r, int c,
                                                     int rows, int cols,
                                                     boolean[][] visited) {
        List<Point> out = new ArrayList<>();
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];

            if (nr >= 0 && nr < rows &&
                nc >= 0 && nc < cols &&
                !visited[nr][nc]) {

                out.add(new Point(nr, nc));
            }
        }

        return out;
    }

    private static Direction getDirection(int[] curr, int[] next) {

        int dr = next[0] - curr[0];
        int dc = next[1] - curr[1];

        if (dr == 1) return Direction.SOUTH;
        if (dr == -1) return Direction.NORTH;
        if (dc == 1) return Direction.EAST;
        return Direction.WEST;
    }

    private static Monster getMonsterForDepth(int depth, Random rng) {

        if (depth <= 2) return MonsterFactory.createMonster("Gremlin");
        if (depth <= 4) return MonsterFactory.createMonster("Skeleton");

        return (rng.nextDouble() < 0.7)
                ? MonsterFactory.createMonster("Skeleton")
                : MonsterFactory.createMonster("Ogre");
    }
}