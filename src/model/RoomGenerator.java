package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;


public class RoomGenerator 
{
	private static int HEAL_POT = 50;
	
	private static int VISION_POT = 1;
	
	Dungeon myDungeon;
	
	int myPillarCount;
	
	Random myRandom;
	
	RoomType myRoomType;
	
	public void generate(final Dungeon theDungeon, final Random theRng)
	{
		myPillarCount = 0;
		myRandom = theRng;
		
		int rows = theDungeon.getRows();
		int cols = theDungeon.getCols();
		
		for(int row = 0; row < rows; row++)
		{
			for(int col = 0; col < cols; col++)
			{
				Room room = theDungeon.getRoom(row, col);
				//System.out.println(room.getRoomType());
				createLayout(room);
				if (room.getRoomType() != RoomType.NONE) createEvents(room, theRng);
			}
		}
	}
	
	private void createLayout(final Room theRoom)
	{
		Room room = theRoom;
		int rows = room.getTilesRows();
		int cols = room.getTilesCols();
		
	    int lastRowIndex = rows - 1;
	    int lastColIndex = cols - 1;
		int middle = rows/2;
		
		// two things ... generate tile type and set valid tiles.
		for(int row = 0; row < rows; row++)
		{
			for(int col = 0; col < cols; col++)
			{
				boolean isBorder = (row == 0 || col == 0 ||
							row == lastRowIndex || col == lastColIndex);
				Point point = new Point(row, col);
				if(isBorder) // must be wall or door
				{
					boolean isRowMiddle = (row == middle);
					boolean isColMiddle = (col == middle);
					
					boolean isNorth = row == 0 && isColMiddle && room.getDirections().contains(Direction.NORTH);
	                boolean isSouth = row == lastRowIndex && isColMiddle && room.getDirections().contains(Direction.SOUTH);
	                boolean isEast  = isRowMiddle && col == lastColIndex && room.getDirections().contains(Direction.EAST);
	                boolean isWest  = isRowMiddle && col == 0 && room.getDirections().contains(Direction.WEST);
					
					if(isNorth)
					{
						room.setTile(point, TileType.DOOR_N);
					}
					else if(isSouth)
					{
						room.setTile(point, TileType.DOOR_S);
					}
					else if(isEast)
					{
						room.setTile(point, TileType.DOOR_E);
					}
					else if(isWest)
					{
						room.setTile(point, TileType.DOOR_W);
					}
					else
					{
						room.setTile(point, TileType.WALL);
					}
				}
				else
				{
					room.setTile(point, TileType.FLOOR);
				}
			}
		}
	}
	
	private void createEvents(final Room theRoom, final Random theRng)
	{
		Room room = theRoom;
		Random rng = theRng;
		RoomType rt = room.getRoomType();
		EnumSet<RoomType> MiddleRooms = EnumSet.of(RoomType.START, RoomType.EXIT,
													RoomType.SHOP);
		boolean isMiddleRoom = (MiddleRooms.contains(rt));
		
		// Start, Exit, and Shop all will spawn middle.
		if(isMiddleRoom)
		{
			switch(rt)
			{
				case START:
					theRoom.setMiddleTile(TileType.ENTRANCE);
					break;
				case SHOP:
					theRoom.setMiddleTile(TileType.SHOP); //WHEN WE ADD SHOP...
					break;
				default: // exit
					theRoom.setMiddleTile(TileType.EXIT);
			}	
		}
		else // Pillar, Treasure, Pit, Encounter will be random.
		{	
			switch(rt)
			{
				case PIT:
					generatePitRoom(room, rng);
					break;
				case PILLAR:
					generatePillarRoom(room, rng);
					break;
				case ENCOUNTER:
					generateEncounterRoom(room, rng);
					break;
				default:
					generateTreasureRoom(room, rng);
			}
			
		}
	}
	
	private void generateEncounterRoom(final Room theRoom, final Random theRng)
	{
		List<Point> notFull = getNotFull(theRoom);
		List<Monster> monsters = theRoom.getMonsters();
		if(monsters != null)
		{
			// placing monsters
			for(int i = 0; i < monsters.size(); i++)
			{
				//System.out.println(notFull);
				int index = theRng.nextInt(notFull.size());
				Point roomPoint = notFull.get(index);
				notFull.remove(index);
				Monster monster = monsters.get(i);
				switch(monster.getClass().getSimpleName().toLowerCase())
				{
					case "gremlin":
						theRoom.setTile(roomPoint, TileType.GREMLIN);
						break;
					case "skeleton":
						theRoom.setTile(roomPoint, TileType.SKELETON);
						break;
					case "ogre":
						theRoom.setTile(roomPoint, TileType.OGRE);
						break;
				}
				theRoom.getTile(roomPoint).setCharacter(monsters.get(i));;
			} 
		}
		generateTreasureRoom(theRoom, theRng);
	}
	
	
	
	private void generatePillarRoom(final Room theRoom, final Random theRng)
	{
		if(myPillarCount < 4)
		{
			char[] pillars = {'A', 'C', 'I', 'P'};
			List<Point> notFull = getNotFull(theRoom);
			Point pillarPoint = notFull.get(theRng.nextInt(notFull.size()));
			
			theRoom.setTile(pillarPoint, TileType.fromChar(pillars[myPillarCount]));
			if(pillars[myPillarCount] == 'C')
			{
				theRoom.getTile(pillarPoint).setItem(new Pillar('E'));
			}
			else
			{
				theRoom.getTile(pillarPoint).setItem(new Pillar(pillars[myPillarCount]));
			}
			System.out.println(myPillarCount);
			myPillarCount++;
		}
	}
	
	private void generatePitRoom(final Room theRoom,
								final Random theRng)
	{
		int numberOfPits = (theRoom.getDepth() * 2) + 4;
		List<Point> notFull = getNotFull(theRoom);
		
		for(int i = 0; i < numberOfPits; i++)
		{
			int index = theRng.nextInt(notFull.size() - 1);
			Point roomPoint = notFull.get(index);
			notFull.remove(index);
			
			theRoom.setTile(roomPoint, TileType.PIT);
		}
	}
	
	private void generateTreasureRoom(final Room theRoom, final Random theRng)
	{
		List<Point> notFull = getNotFull(theRoom);
		List<ItemType> items = new ArrayList<ItemType>(theRoom.getItems());

		for(int i = 0; i < theRoom.getItems().size(); i++)
		{
			int index = theRng.nextInt(notFull.size() - 1);
			Point roomPoint = notFull.get(index);
			notFull.remove(index);
			
			ItemType it = items.get(i);
			int roll = theRng.nextInt(100);
			Rarity rarity = getRarity(roll);
			
			switch(it)
			{
				case WEAPON:
					Weapon newWeapon;
					int weaponRoll = myRandom.nextInt(4);
					switch(weaponRoll)
					{
						case 1:
							newWeapon = Weapon.createSpear(rarity);
							theRoom.getTile(roomPoint).setTile(TileType.SPEAR);
							break;
						case 2:
							newWeapon = Weapon.createFlail(rarity);
							theRoom.getTile(roomPoint).setTile(TileType.FLAIL);
							break;
						case 3:
							newWeapon = Weapon.createFalchion(rarity);
							theRoom.getTile(roomPoint).setTile(TileType.FALCHION);
							break;
						case 4:
							newWeapon = Weapon.createMorningStar(rarity);
							theRoom.getTile(roomPoint).setTile(TileType.MORNING_STAR);
							break;
						default:
							newWeapon = Weapon.createStick(rarity);
							theRoom.getTile(roomPoint).setTile(TileType.STICK);
					}
					theRoom.getTile(roomPoint).setItem(newWeapon);
					
					break;
				case VISION_POTION:
					theRoom.getTile(roomPoint).setItem(new VisionPotion(
												VISION_POT, rarity));
					theRoom.getTile(roomPoint).setTile(TileType.VISION_POTION);
					break;
				case HEALING_POTION:
					theRoom.getTile(roomPoint).setItem(new HealingPotion(
												HEAL_POT, rarity));
					theRoom.getTile(roomPoint).setTile(TileType.HEALING_POTION);
					break;
				default: // gold
					int goldRng = (int) (theRoom.getDepth() * (8 + (roll * 0.1)));
					theRoom.getTile(roomPoint).setItem(new Gold(goldRng));
					theRoom.getTile(roomPoint).setTile(TileType.GOLD);
			}
		}
	}
	
	private List<Point> getNotFull(final Room theRoom)
	{
		List<Point> notFull = new ArrayList<>();
		for(int r = 0 ; r < theRoom.getFullTiles().length; r++)
		{
			for(int c = 0; c < theRoom.getFullTiles()[0].length; c++)
			{
				boolean isEmpty = theRoom.getFullTiles()[r][c] == false;			
				if(isEmpty)
				{
					notFull.add(new Point(r, c));
				}
			}
		}
		return notFull;
	}
	
	private Rarity getRarity(final int theRoll)
	{ 	// Roll is 1-100
		Rarity rarity = Rarity.COMMON;
		if(theRoll > 50 && theRoll < 75)
		{
			rarity = Rarity.UNCOMMON;
		}
		else if(theRoll > 75 && theRoll < 88)
		{
			rarity = Rarity.RARE;
		}
		else if(theRoll > 88 && theRoll < 94)
		{
			rarity = Rarity.EPIC;
		}
		else if(theRoll > 94)
		{
			rarity = Rarity.LEGENDARY;
		}
		return rarity;
	}
}
