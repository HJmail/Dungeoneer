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
	
	public boolean hasHero()
	{
		return myCharacter instanceof Hero;
	}
	
	public boolean isWalkable()
	{
		return myTileType.isWalkable();
	}
	
	public boolean hasItem()
	{
		return (myItem != null);
	}
	
	public Item getItem()
	{
		Item i = myItem;
		myItem = null;
		return i;
	}
	
	public TileType getTileType()
	{
		return  myTileType;
	}
}
