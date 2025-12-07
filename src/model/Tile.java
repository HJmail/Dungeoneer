package model;

public class Tile 
{	
	DungeonTile myTileType;
	
	DungeonCharacter myCharacter;
	
	Item myItem;
	
	public Tile(final DungeonTile theTileType)
	{
		myTileType = theTileType;
		myCharacter = null;
		myItem = null;
	}
	
	public void setTile(final DungeonTile theTileType)
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
		myTileType = DungeonTile.FLOOR;
		return i;
	}
	
	public DungeonTile getTileType()
	{
		return  myTileType;
	}
}
