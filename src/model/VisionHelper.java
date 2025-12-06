package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides logic for Vision Potions to reveal nearby rooms.
 */
public final class VisionHelper {

    private VisionHelper() {
        // prevent instantiation
    }

    /**
     * Returns all valid room coordinates in a 3×3 area around the hero.
     *
     * @param theDungeon the dungeon model
     * @param theHero    the hero whose position is used
     * @return a List of Points representing visible rooms
     */
    public static List<Point> getVisibleRooms(final Dungeon theDungeon,
                                              final Hero theHero) {

        List<Point> visible = new ArrayList<>();

        Point heroPos = theDungeon.getHeroLocation();  // <-- we will add getter
        int hx = heroPos.x;
        int hy = heroPos.y;

        int rows = theDungeon.getRows();
        int cols = theDungeon.getCols();

        // Loop 3×3 area around hero
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {

                int nx = hx + dx;
                int ny = hy + dy;

                if (nx >= 0 && nx < rows &&
                    ny >= 0 && ny < cols) {

                    visible.add(new Point(nx, ny));
                }
            }
        }

        return visible;
    }
}
