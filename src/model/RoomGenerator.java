package model;

import java.awt.Point;
import java.util.Random;

public class RoomGenerator 
{
	
	public static Room generate(final Random theRng, final RoomType theRoomType)
	{
		Room room = new Room();
		room.setRoomType(theRoomType);
		
		createLayout(room);
		if (room.getRoomType() != RoomType.NONE) createEvents(room, theRng);
		
		return room;
	}
	
	private static void createLayout(final Room theRoom)
	{
		Room room = theRoom;
		int lastRow = room.getTiles().length;
		int lastCol = room.getTiles()[0].length;
		int middle = 7;
		
		// two things ... generate tile type and set valid tiles.
		for(int row = 0; row < lastRow; row++)
		{
			for(int col = 0; col < lastCol; col++)
			{
				boolean isBorder = (row == 0 || col == 0 || row == lastRow || col == lastCol);
				Point point = new Point(row, col);
				
				if(isBorder) // must be wall or door
				{
					boolean isRowMiddle = (row == middle);
					boolean isColMiddle = (row == middle);
					
					if(isRowMiddle || isColMiddle) // doors
					{
						boolean isNorth = row == 0 && isColMiddle && room.getDirections().contains(Direction.NORTH);
						boolean isEast = isRowMiddle && col == lastCol && room.getDirections().contains(Direction.EAST);
						boolean isSouth = row == lastRow && isColMiddle && room.getDirections().contains(Direction.SOUTH);
						boolean isWest = isRowMiddle && col == 0 && room.getDirections().contains(Direction.WEST);
						
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
					}
				}
				else
				{
					room.setTile(point, TileType.FLOOR);
				}
			}
		}
	}
	
	private static void createEvents(final Room theRoom, final Random theRng)
	{
		Room room = theRoom;
		Random rng = theRng;
		
		// Start, Exit, and Shop all will spawn middle.
		
		// Pillar, Treasure, Pit, Encounter will be random.
	}
}
