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
    INTERSECTION('+', "null");
	
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
}
