package model;

public class Tile 
{	
	TileType myTileType;
	
	DungeonCharacter myCharacter;
	
	Item myItem;
	
	public Tile(final TileType theTileType)
	{
		myTileType = theTileType;
		myCharacter = null;
		myItem = null;
	}
	
	public void setTile(final TileType theTileType)
	{
		myTileType = theTileType;
	}
	
	public void setItem(final Item theItem)
	{
		myItem = theItem;
	}
	
	public void setCharacter(final DungeonCharacter theCharacter)
	{
		myCharacter = theCharacter;
	}
}
