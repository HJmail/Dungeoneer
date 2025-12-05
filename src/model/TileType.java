package model;

public enum TileType 
{
    FLOOR('M', "wall.png"),
    WALL('.', "floor.png"),
    PIT('X', "floor_pit.png"),
    VOID(' ', "void.png"),
    ENTRANCE('E', "enterance_tile.png"),
    EXIT('O', "exit_tile.png"),
    DOOR_N('^', "door_north"),
    DOOR_S('v', "door_south"),
    DOOR_E('>', "door_east"),
    DOOR_W('<', "door_west"),
    
    HORIZONTAL('-', "null"),
    VERTICAL('|', "null"),
    INTERSECTION('+', "null"),
    
    SPEAR('1', "null"),
    FALCHION('2', "null"),
    FLAIL('3', "null"),
    MORNING_STAR('4', "null"),
    STICK('5', "null"),
    
    GOLD('$', "null"),
    HEALING_POTION('H', "null"),
    VISION_POTION('V', "null"),
    
    ABSTRACTION_PILLAR('A', "null"),
    ENCAPSULATION_PILLAR('C', "null"),
    INHERITANCE_PILLAR('I', "null"),
    POLYMORPHISM_PILLAR('P', "null"),
	
	GREMLIN('G', "gremlin_down.png"),
	SKELETON('S', "skeleton_down.png"),
	OGRE('Z', "ogre_down.png");
	
	
    private static final String PATH = "Dungeoneer_Terrain/";
    
    private final char myChar;
    
    private final String myFileName;
    
    TileType(final char theChar, final String theFileName)
    {
    	myChar = theChar;
    	myFileName = theFileName;
    }
    
    public String getFilePath()
    {
    	return PATH + myFileName;
    }
    
    public char getChar()
    {
    	return myChar;
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
