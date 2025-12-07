package model;

/**
 * This is a Dungeon Tile Enum for Room Generation.
 * @author Skyler, Cristian
 * @version 12/6/2025
 */
public enum DungeonTile 
{
    FLOOR('.', "Dungeoneer Terrain/wall.png", null, true),
    WALL('M', "Dungeoneer Terrain/floor.png", null, false),
    PIT('X', "Dungeoneer Terrain/floor_pit.png", null, true),
    ENTRANCE('E', "Dungeoneer Terrain/entrance_tile.png", FLOOR, true),
    EXIT('O', "Dungeoneer Terrain/exit_tile.png", FLOOR, true),
    DOOR_N('^', "Dungeoneer Terrain/door_north.png", null, true, Direction.NORTH),
    DOOR_S('v', "Dungeoneer Terrain/door_south.png", null, true, Direction.SOUTH),
    DOOR_E('>', "Dungeoneer Terrain/door_east.png", null, true, Direction.EAST),
    DOOR_W('<', "Dungeoneer Terrain/door_west.png", null, true, Direction.WEST),
    SHOP('s', "Dungeoneer Characters/shop.png", FLOOR, false),
    
    SPEAR('1', "Dungeoneer Items/spear.png", FLOOR, true),
    FALCHION('2', "Dungeoneer Items/falchion.png", FLOOR, true),
    FLAIL('3', "Dungeoneer Items/flail.png", FLOOR, true),
    MORNING_STAR('4', "Dungeoneer Items/morning_star.png", FLOOR, true),
    STICK('5', "Dungeoneer Items/stick.png", FLOOR, true),
    
    GOLD('$', "Dungeoneer Items/gold.png", FLOOR, true),
    HEALING_POTION('H', "Dungeoneer Items/potion_healing.png", FLOOR, true),
    VISION_POTION('V', "Dungeoneer Items/potion_vision.png", FLOOR, true),
    
    ABSTRACTION_PILLAR('A', "Dungeoneer Items/abstraction_pillar.png", FLOOR, true),
    ENCAPSULATION_PILLAR('C', "Dungeoneer Items/encapsulation_pillar.png", FLOOR, true),
    INHERITANCE_PILLAR('I', "Dungeoneer Items/inheritance_pillar.png", FLOOR, true),
    POLYMORPHISM_PILLAR('P', "Dungeoneer Items/polymorphism_pillar.png", FLOOR, true),
	
	GREMLIN('G', "Dungeoneer Characters/gremlin_down.png", FLOOR, true),
	SKELETON('S', "Dungeoneer Characters/skeleton_down.png", FLOOR, true),
	OGRE('Z', "Dungeoneer Characters/ogre_down.png", FLOOR, true);
    
	/**
	 * This is the char of the enum.
	 */
    private final char myChar;
    
    /**
     * This is the file name.
     */
    private final String myFileName;
    
    /**
     * This is the DungeonTile.
     */
    private final DungeonTile myBaseType;
    
    /**
     * This checks if a room is walkable.
     */
    private boolean myIsWalkable;
    
    /**
     * This is the Door Direction.
     */
    private Direction myDoorDirection;
    
    /**
     * This is a constructor with 4 args.
     * @param theChar Char representation.
     * @param theFileName File Path.
     * @param theBaseType Base Type if its an overlay.
     * @param theIsWalkable can be walked through.
     */
    DungeonTile(final char theChar,
    		final String theFileName,
    		final DungeonTile theBaseType,
    		final boolean theIsWalkable)
    {
    	myChar = theChar;
    	myFileName = theFileName;
    	myBaseType = theBaseType;
    	myIsWalkable = theIsWalkable;
    }
    
    /**
     * Another constructor for Doors.
     * @param theChar Char representation.
     * @param theFileName File Path.
     * @param theBaseType Base Type if its an overlay.
     * @param theIsWalkable can be walked through.
     * @param theDoorDirection the direction of the door.
     */
    DungeonTile(final char theChar,
    		final String theFileName,
    		final DungeonTile theBaseType,
    		final boolean theIsWalkable,
    		final Direction theDoorDirection)
    {
    	myChar = theChar;
    	myFileName = theFileName;
    	myBaseType = theBaseType;
    	myIsWalkable = theIsWalkable;
    	myDoorDirection = theDoorDirection;
    }
    
    /**
     * This gets the file path.
     * @return String of file path.
     */
    public String getFilePath()
    {
    	return myFileName;
    }
    
    /**
     * This gets the char.
     * @return the char of the room.
     */
    public char getChar()
    {
    	return myChar;
    }
    
    /**
     * This gets the DungeonTile of the Room.
     * @return the DungeonTile of the room.
     */
    public DungeonTile getBaseType()
    {
    	return myBaseType;
    }
    
    /**
     * This gets if a room is walkable.
     * @return
     */
    public boolean isWalkable()
    {
    	return myIsWalkable;
    }
    
    /**
     * This check if a given Tile type is a door.
     * @return boolean that checks if it is a door.
     */
    public boolean isDoor()
    {
    	return myDoorDirection != null;
    }
    
    /** 
     * This gets the door's direction.
     * @return
     */
    public Direction getDoorDirection()
    {
    	return myDoorDirection;
    }
    
    /**
     * This turns a char to a enum type.
     * @param c the given char.
     * @return the dungeon tile type.
     */
    public static DungeonTile fromChar(final char c) 
    {
        for (DungeonTile type : values()) 
        {
            if (type.myChar == c) return type;
        }
        return FLOOR; // default
    }
}
