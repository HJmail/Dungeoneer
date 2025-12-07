package model;

import java.awt.Point;
import java.util.EnumSet;

/**
 * This class represents the dungeon the player must traverse.
 */
public class Dungeon {

    /**
     * This is a 2d array that holds a grid of rooms that represents a Maze.
     */
    private Room[][] myMaze;

    /**
     * This field is the number of rows.
     */
    private int myRows;

    /**
     * This field is the number of cols.
     */
    private int myCols;

    /**
     *  This holds the Hero's location as (row, col).
     */
    private Point myHeroLocation;

    /**
     * This is the starting point used in some logic.
     */
    private Point myStartLocation;

    /**
     *  Basic constructor.
     *
     *  Difficulty d => (d + 4) x (d + 4) dungeon:
     *  1 -> 5x5, 2 -> 6x6, ... 9 -> 13x13.
     */
    public Dungeon(final Hero theHero, final int theDifficulty) {
        myRows = theDifficulty + 4;
        myCols = theDifficulty + 4;
        myMaze = new Room[myRows][myCols];
        myHeroLocation = new Point();
        myStartLocation = new Point();

        generateDimensions();
    }

    /**
     * This method generates an empty dungeon (rooms will be configured elsewhere).
     */
    private void generateDimensions() {
        for (int i = 0; i < myRows; i++) {          // outer is row
            for (int j = 0; j < myCols; j++) {      // inner is col
                Room newRoom = new Room();
                myMaze[i][j] = newRoom;
            }
        }
    }

    /**
     * Checks which directions are traversable from the hero's current room.
     */
    public EnumSet<Direction> getTraversable() {
        return myMaze[(int) myHeroLocation.getX()]
                     [(int) myHeroLocation.getY()]
                     .getDirections();
    }

    /**
     * Changes the hero location.
     *
     * @param theRows The new row the Hero will be.
     * @param theCols The new col the Hero will be.
     */
    private void setHeroLocation(final int theRows, final int theCols) {
        myHeroLocation.setLocation(theRows, theCols);
    }

    /**
     * Public helper if you ever need to sync hero location
     * from another system (e.g., GUI board).
     */
    public void setHeroLocationPublic(final int theRow, final int theCol) {
        setHeroLocation(theRow, theCol);
    }

    /**
     * This method moves the hero if possible.
     *
     * @param theDirection The direction to move.
     * @return Boolean representing if the move was successful.
     */
    public boolean move(final Direction theDirection) {
        boolean moved = true;
        int x = (int) myHeroLocation.getX();
        int y = (int) myHeroLocation.getY();

        if (getTraversable().contains(theDirection) && theDirection == Direction.NORTH) {
            moveHero(x - 1, y, Direction.NORTH);
        } else if (getTraversable().contains(theDirection) && theDirection == Direction.EAST) {
            moveHero(x, y + 1, Direction.EAST);
        } else if (getTraversable().contains(theDirection) && theDirection == Direction.SOUTH) {
            moveHero(x + 1, y, Direction.SOUTH);
        } else if (getTraversable().contains(theDirection) && theDirection == Direction.WEST) {
            moveHero(x, y - 1, Direction.WEST);
        } else {
            moved = false;
        }
        return moved;
    }

    /**
     * Logic for moving a hero to a new room and starting new room logic.
     *
     * @param theRow Row of new room.
     * @param theCol Col of new room.
     * @param theDirection Direction we exited in (for room logic / symbols).
     */
    private void moveHero(final int theRow, final int theCol, final Direction theDirection) {
        // current row and col
        int x = (int) myHeroLocation.getX();
        int y = (int) myHeroLocation.getY();

        // exiting old room
        Room oldRoom = myMaze[x][y];
        oldRoom.exit(theDirection);          // marks activated + visible in Room.exit()

        // joining new room
        setHeroLocation(theRow, theCol);
    }

    public void setRoomDepth(final int theRow, final int theCol, final int theDepth) {
        myMaze[theRow][theCol].setDepth(theDepth);
    }

    public void setStartLocation(final int theRow, final int theCol) {
        myStartLocation.setLocation(theRow, theCol);
        // optional: also start the hero there
        setHeroLocation(theRow, theCol);
    }

    public Point getStartPoint() {
        return new Point(myStartLocation);
    }

    /**
     * @return The number of rows.
     */
    public int getRows() {
        return myRows;
    }

    /**
     * @return The number of columns.
     */
    public int getCols() {
        return myCols;
    }

    /**
     * @return The current room the player is in.
     */
    public Room getCurrentRoom() {
        int x = (int) myHeroLocation.getX();
        int y = (int) myHeroLocation.getY();
        return getRoom(x, y);
    }

    /**
     * This gets the room at (row, col).
     *
     * @return The room at the given coordinates.
     */
    public Room getRoom(final int theRow, final int theCol) {
        return myMaze[theRow][theCol];
    }

    // ------------------------------------------------------------
    // NEW: Hero location accessors for the mini-map & GUI
    // ------------------------------------------------------------

    /** @return hero's current row index in the dungeon. */
    public int getHeroRow() {
        return (int) myHeroLocation.getX();
    }

    /** @return hero's current column index in the dungeon. */
    public int getHeroCol() {
        return (int) myHeroLocation.getY();
    }

    /** @return a defensive copy of the hero's location. */
    public Point getHeroLocation() {
        return new Point(myHeroLocation);
    }
}