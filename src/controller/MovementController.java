package controller;

import model.Direction;
import model.Dungeon;
import model.DungeonTile;
import model.Room;

/**
 * Controls hero movement at the {@link Room} level within a single {@link Dungeon}.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Track the hero's current grid position (row/column) inside a dungeon.</li>
 *     <li>Locate the dungeon entrance and respawn the hero there when needed.</li>
 *     <li>Apply movement in cardinal {@link Direction}s, respecting available doors.</li>
 *     <li>Optionally remove doors to prevent backtracking (maze-like behavior).</li>
 * </ul>
 * 
 * @author(s): Cristian, Skyler, Hiba
 * @version 23.0.1
 */
public class MovementController {

    /**
     * The dungeon this movement controller operates on.
     */
    private final Dungeon myDungeon;

    /**
     * Current hero row index in the dungeon grid.
     */
    private int myRow;

    /**
     * Current hero column index in the dungeon grid.
     */
    private int myCol;

    /* ------------------------------------------------------------------
     * Construction
     * ------------------------------------------------------------------ */

    /**
     * Creates a new {@code MovementController} bound to the given dungeon.
     * <p>
     * The hero's initial position is automatically set to the dungeon's
     * entrance room (the tile with {@link DungeonTile#ENTRANCE}). If no
     * entrance is found, the position falls back to (0, 0).
     * </p>
     *
     * @param theDungeon the dungeon to control movement in
     * @throws NullPointerException if {@code theDungeon} is {@code null}
     */
    public MovementController(final Dungeon theDungeon) {
        if (theDungeon == null) {
            throw new NullPointerException("Dungeon must not be null.");
        }
        myDungeon = theDungeon;
        respawnAtEntrance();
    }

    /* ------------------------------------------------------------------
     * Public API
     * ------------------------------------------------------------------ */

    /**
     * Returns the current {@link Room} the hero is standing in.
     *
     * @return the current room at ({@link #myRow}, {@link #myCol})
     */
    public Room getCurrentRoom() {
        return myDungeon.getRoom(myRow, myCol);
    }

    /**
     * Attempts to move the hero one room in the specified direction.
     * <p>
     * Movement only succeeds if the current room has a door in the given
     * {@link Direction}. When the move succeeds:
     * <ul>
     *     <li>The internal grid position ({@link #myRow}, {@link #myCol}) is updated.</li>
     *     <li>The door in the direction we came from is removed from the new room,</li>
     *     <li>and the door used in the old room is also removed.</li>
     * </ul>
     * This creates a one-way, maze-like structure where the hero cannot
     * backtrack through the same doors.
     * </p>
     *
     * @param dir the direction to move in (NORTH, SOUTH, EAST, WEST)
     * @return {@code true} if the move succeeded; {@code false} if there
     *         is no door in that direction from the current room
     */
    public boolean move(final Direction dir) {

        Room current = getCurrentRoom();

        // Only move if there's a door that way from this room
        if (!current.getDirections().contains(dir)) {
            return false;
        }

        // Update position
        switch (dir) {
            case NORTH -> myRow--;
            case SOUTH -> myRow++;
            case EAST  -> myCol++;
            case WEST  -> myCol--;
            default    -> { /* no-op; all enum values handled above */ }
        }

        // Remove door behind us to prevent backtracking
        Room newRoom = getCurrentRoom();
        newRoom.getDirections().remove(dir.opposite());
        current.getDirections().remove(dir);

        return true;
    }

    /**
     * Resets the hero position back to the entrance room of the dungeon.
     * <p>
     * This simply re-runs the entrance search logic. If no entrance room
     * is found, the position is set to (0, 0) as a fallback.
     * </p>
     */
    public void respawnAtEntrance() {
        findEntrance();
    }

    /**
     * Returns the current row index of the hero in the dungeon grid.
     *
     * @return the current row index (0-based)
     */
    public int getRow() {
        return myRow;
    }

    /**
     * Returns the current column index of the hero in the dungeon grid.
     *
     * @return the current column index (0-based)
     */
    public int getCol() {
        return myCol;
    }

    /* ------------------------------------------------------------------
     * Internal helpers
     * ------------------------------------------------------------------ */

    /**
     * Locates the {@link DungeonTile#ENTRANCE} in the dungeon grid and
     * updates {@link #myRow} and {@link #myCol} to that position.
     * <p>
     * If no entrance is found, the position falls back to (0, 0).
     * </p>
     */
    private void findEntrance() {
        for (int r = 0; r < myDungeon.getRows(); r++) {
            for (int c = 0; c < myDungeon.getCols(); c++) {
                Room room = myDungeon.getRoom(r, c);
                if (room.getTile() == DungeonTile.ENTRANCE) {
                    myRow = r;
                    myCol = c;
                    return;
                }
            }
        }
        // Fallback if no entrance tile is present
        myRow = 0;
        myCol = 0;
    }
}
