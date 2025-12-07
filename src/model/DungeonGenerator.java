package model;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * This is used to generate the a dungeon with a given seed.
 * @author Skyler Z. Broussard
 */
public class DungeonGenerator 
{
	/**
	 * This starts the generation of a dungeon.
	 * @param theRng Seeded Random.
	 * @param theDifficulty User inputed Difficulty
	 * @param theHero The user's hero.
	 * @return The generated Dungeon.
	 */
	public static Dungeon generate(final Random theRng, final int theDifficulty, final Hero theHero)
	{
		Dungeon dungeon = new Dungeon(theDifficulty);
		
		createEvents(dungeon, theRng, createLayout(dungeon, theRng));
		
		return dungeon;
	}
	
	/**
	 * This creates the layout of the dungeon which rooms has doors.
	 * @param theDungeon The dungeon.
	 * @param theRng The seeded random.
	 * @return returns a int representing the depth of the Dungeon.
	 */
	private static int createLayout(final Dungeon theDungeon, final Random theRng)
	{
		// getting dimensions
		int rows = theDungeon.getRows();
		int cols = theDungeon.getCols();
		
		// visited 2d array
		boolean[][] visited = new boolean[rows][cols];
		
		// RNG entry room
		int startRow = theRng.nextInt(rows);
		int startCol = theRng.nextInt(cols);
		visited[startRow][startCol] = true;
		
		// setting the start room
		theDungeon.getRoom(startRow, startCol).setRoomType(RoomType.START);;
		theDungeon.setRoomDepth(startRow, startCol, 0);
		theDungeon.getRoom(startRow, startCol).setActivated(true);
		theDungeon.setStartLocation(startRow, startCol);
		
		// Double ended queue
		Deque<Point> stack = new ArrayDeque<>();
		stack.push(new Point(startRow, startCol));
		
		// depth logic
		int maxDepth = 0;
		Point deepestRoom = new Point(startRow, startCol);
		
		// keep going until no more room's
		while(!stack.isEmpty()) 
		{
			Point current = stack.peek();
			int currentRow = current.x;
			int currentCol = current.y;
			
			List<Point> neighbors = getUnvistedNeighbors(currentRow, currentCol, rows, cols, visited);
			
			// check if no more neighbors
			if(!neighbors.isEmpty())
			{
				// next neighbor
				Point nextNeighbor = neighbors.get(theRng.nextInt(neighbors.size()));
				int nextRow = nextNeighbor.x;
				int nextCol = nextNeighbor.y;
				
				// visited
				visited[nextRow][nextCol] = true;
				
				// get both rooms
				Room currentRoom = theDungeon.getRoom(currentRow, currentCol);
				Room nextRoom = theDungeon.getRoom(nextRow, nextCol);
				
				// connecting the rooms
				Direction theDirection = getDirection(new int[] {currentRow, currentCol},
													new int[] {nextRow, nextCol});
				
				currentRoom.getDirections().add(theDirection);
				nextRoom.getDirections().add(theDirection.opposite());
				
		        // depth logic 
		        int nextDepth = currentRoom.getDepth() + 1;
		        nextRoom.setDepth(nextDepth);

		        if (nextDepth > maxDepth) {
		            maxDepth = nextDepth;
		            deepestRoom = nextNeighbor;
		        }
				
				// stack continued
				stack.push(nextNeighbor); 
			}
			else 
			{
				stack.pop();
			}
		}
		
		// setting exit.
		int deep_x = (int)deepestRoom.getX();
		int deep_y = (int)deepestRoom.getY();
		theDungeon.getRoom(deep_x, deep_y).setRoomType(RoomType.EXIT);
		
		return maxDepth;
	}
	
	/**
	 * This creates the events within the dungeon.
	 * @param theDungeon The dungeon.
	 * @param theRng Seeded Random.
	 * @param maxDepth Max depth.
	 */
	private static void createEvents(final Dungeon theDungeon, final Random theRng, int maxDepth)
	{
		int rows = theDungeon.getRows();
		int cols = theDungeon.getCols();
		
		List<Point> possiablePillars = new ArrayList<>();
		
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				Room room = theDungeon.getRoom(r, c);
				
				// deals with START
				if(room.getRoomType() == RoomType.START ||
						room.getRoomType() == RoomType.EXIT) continue;
				
				possiablePillars.add(new Point(r, c));
			}
		}
		
		// setting pillars
		Collections.shuffle(possiablePillars, theRng); // mix possibles
		for(int  i = 0; i < 4; i++)
		{
			Point roomPoint = possiablePillars.get(i);
			Room room = theDungeon.getRoom((int)roomPoint.getX(), (int) roomPoint.getY());
			room.setRoomType(RoomType.PILLAR);
			room.setItems(EnumSet.of(ItemType.PILLAR));
		}
		
		// setting rest of the rooms
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				Room room = theDungeon.getRoom(r, c);
				
				// skips start, end, and pillars
				if(room.getRoomType() == RoomType.START ||
						room.getRoomType() == RoomType.EXIT ||
						room.getRoomType() == RoomType.PILLAR)
					continue;
				
				int roll = theRng.nextInt(100);
				int depth = room.getDepth();
				
				// rng rates
				if(depth <= 1) // 
				{
					if(roll < 50)
					{
						room.setRoomType(RoomType.NONE);
					}
					else
					{
						room.setRoomType(RoomType.TREASURE);
						room.setItems(EnumSet.of(getRandomItem(theRng)));
					}
				}
				else if(depth < maxDepth * 0.5) // less than half max depth
				{
					if(roll <= 20) // 0-20
					{
						room.setRoomType(RoomType.ENCOUNTER); // need generation code... Skeleton and gremlin?
						// spawn monsters
						List<Monster> monsters = new ArrayList<>();
						int monsterCount = (depth <= 3) ? 1 : (depth <= 5 ? 2 : 3);

						for (int i = 0; i < monsterCount; i++) {
						    monsters.add(getMonsterForDepth(depth, theRng));
						}

						room.setMonsters(monsters);
						
						room.setItems(EnumSet.of(getRandomItem(theRng)));
					}
					else if(roll <= 60) // 21-60
					{
						room.setRoomType(RoomType.TREASURE);
						room.setItems(EnumSet.of(getRandomItem(theRng), getRandomItem(theRng)));
					}
					else if(roll <= 75)
					{
						room.setRoomType(RoomType.PIT);
					}
					else if(roll <= 85)
					{
						room.setRoomType(RoomType.SHOP);
						room.setShopkeeper(new Shopkeeper());
					}
					else 
					{
						room.setRoomType(RoomType.NONE);
					}
				}
				else // deep section
				{
					if(roll <= 50)
					{
						room.setRoomType(RoomType.ENCOUNTER); // all monsters?
						List<Monster> monsters = new ArrayList<>();
	                    int monsterCount = 2 + theRng.nextInt(2); // 2–3 monsters

	                    for (int i = 0; i < monsterCount; i++) {
	                        monsters.add(getMonsterForDepth(depth, theRng));
	                    }

	                    room.setMonsters(monsters);
	                    
						room.setItems(EnumSet.of(ItemType.WEAPON, getRandomItem(theRng)));
					}
					else if(roll <= 75)
					{
						room.setRoomType(RoomType.SHOP);
						room.setShopkeeper(new Shopkeeper());
					}
					else if(roll <= 90)
					{
						room.setRoomType(RoomType.PIT);
					}
					else
					{
						room.setRoomType(RoomType.NONE);
					}
				}
			}
		}
	}
	
	/**
	 * This gets a random item based on a roll.
	 * @param theRandom the seeded Random.
	 * @return The ItemType generated.
	 */
	private static ItemType getRandomItem(final Random theRandom)
	{
		int roll = theRandom.nextInt(3);
		ItemType item = ItemType.GOLD;
		
		if(roll == 1 )
		{
			item = ItemType.HEALING_POTION;
		}
		else if (roll == 2)
		{
			item = ItemType.VISION_POTION;
		}
		return item;
	}
	
	/**
	 * This gets the Direction of the form the current room to next room.
	 * @param theCurrent current room
	 * @param theNext next room.
	 * @return the Direction.
	 */
	private static Direction getDirection(final int[] theCurrent, final int[] theNext)
	{
		Direction theDirection = Direction.NORTH; 
		
	    int dr = theNext[0] - theCurrent[0]; // row delta
	    int dc = theNext[1] - theCurrent[1]; // col delta

	    if (dr ==  1 && dc == 0) theDirection = Direction.SOUTH;
	    if (dr ==  0 && dc == -1) theDirection = Direction.WEST;
	    if (dr ==  0 && dc ==  1) theDirection = Direction.EAST;
	    
	    return theDirection;
	}

	/**
	 * This gets the unvisited neighbors of the given room.
	 * @param theRow The current row.
	 * @param theCol the curren col.
	 * @param theRows total rows.
	 * @param theCols total cols.
	 * @param theVisited 2d array of booleans represented visited.
	 * @return A List of Points that tell us unvisited.
	 */
	private static List<Point> getUnvistedNeighbors(final int theRow, final int theCol,
													final int theRows, final int theCols,
													final boolean[][] theVisited)
	{
		List<Point> result = new ArrayList<>();
		int [][] neighbors = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // N, S, W, E
		
		// filter through neighbors
		for(int[] n: neighbors)
		{
	        int nr = theRow + n[0];
	        int nc = theCol + n[1];
	        
	        if(nr >= 0 && nr < theRows &&
	        	nc >= 0 && nc < theCols &&
	        	!theVisited[nr][nc])
	        {
	        	// add if in bounds and not visited
	        	result.add(new Point(nr, nc));
	        }
		}
		return result;
	}
	
	/**
	 * Monster creation.
	 * @param theDepth the depth of the current room.
	 * @param theRng the seeded random.
	 * @return Monster that got generated.
	 */
	private static Monster getMonsterForDepth(final int theDepth, final Random theRng) {
	    if (theDepth <= 2) {
	        return MonsterFactory.createMonster("Gremlin");
	    }
	    if (theDepth <= 4) {
	        return MonsterFactory.createMonster("Skeleton");
	    }
	    if (theRng.nextDouble() < 0.7) {
	        return MonsterFactory.createMonster("Skeleton");
	    }
	    return MonsterFactory.createMonster("Ogre");
	}

}

