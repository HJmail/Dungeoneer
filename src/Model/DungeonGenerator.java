package Model;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import Model.EventType;

public class DungeonGenerator 
{
	public static Dungeon generate(final long theSeed, final int theDifficulty, final Hero theHero)
	{
		Dungeon dungeon = new Dungeon(theHero, theDifficulty);
		Random rng = new Random(theSeed);
		
		createLayout(dungeon, rng);
		createEvents(dungeon, rng);
		
		return dungeon;
	}
	
	private static void createLayout(final Dungeon theDungeon, final Random theRng)
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
		theDungeon.setRoomType(startRow, startCol, EnumSet.of(EventType.START));
		
		// Double ended queue
		Deque<Point> stack = new ArrayDeque<>();
		stack.push(new Point(startRow, startCol));
		
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
				
				// get both rooms
				Room currentRoom = theDungeon.getRoom(currentRow, currentCol);
				Room nextRoom = theDungeon.getRoom(nextRow, nextCol);
				
				// connecting the rooms
				Direction theDirection = getDirection(new int[] {currentRow, currentCol},
													new int[] {nextRow, nextCol});
				
				currentRoom.getDirections().add(theDirection);
				nextRoom.getDirections().add(theDirection.opposite());
			}
			else 
			{
				stack.pop();
			}
		}
	}
	
	private static void createEvents(final Dungeon theDungeon, final Random theRng)
	{
		
	}
	
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

	private static List<Point> getUnvistedNeighbors(final int theRow, final int theCol,
													final int theRows, final int theCols,
													final boolean[][] theVisited)
	{
		List<Point> result = new ArrayList<>();
		int [][] neighbors = {{-1, 0}, {1, 0}, {0, -1}, {0, -1}};
		
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
	
}
