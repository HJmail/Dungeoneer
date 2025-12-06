package model;

public enum TileType 
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
    SHOP('s', "null", FLOOR, false),
    
    //HORIZONTAL('-', "null"),
    //VERTICAL('|', "null"),
    //INTERSECTION('+', "null"),
    
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
    
    private final char myChar;
    
    private final String myFileName;
    
    private final TileType myBaseType;
    
    private boolean myIsWalkable;
    
    private Direction myDoorDirection;
    
    TileType(final char theChar,
    		final String theFileName,
    		final TileType theBaseType,
    		final boolean theIsWalkable)
    {
    	myChar = theChar;
    	myFileName = theFileName;
    	myBaseType = theBaseType;
    	myIsWalkable = theIsWalkable;
    }
    
    TileType(final char theChar,
    		final String theFileName,
    		final TileType theBaseType,
    		final boolean theIsWalkable,
    		final Direction theDoorDirection)
    {
    	myChar = theChar;
    	myFileName = theFileName;
    	myBaseType = theBaseType;
    	myIsWalkable = theIsWalkable;
    	myDoorDirection = theDoorDirection;
    }
    
    public String getFilePath()
    {
    	return myFileName;
    }
    
    public char getChar()
    {
    	return myChar;
    }
    
    public TileType getBaseType()
    {
    	return myBaseType;
    }
    
    public boolean isWalkable()
    {
    	return myIsWalkable;
    }
    
    public boolean isDoor()
    {
    	return myDoorDirection != null;
    }
    
    public Direction getDoorDirection()
    {
    	return myDoorDirection;
    }
    
    public static TileType fromChar(final char c) 
    {
        for (TileType type : values()) 
        {
            if (type.myChar == c) return type;
        }
        return FLOOR; // default
    }
}
