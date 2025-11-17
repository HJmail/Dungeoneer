package Model;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class DungeonGenerator 
{
	public static Dungeon generate(final long theSeed, final int theDifficulty, final Hero theHero)
	{
		Dungeon dungeon = new Dungeon(theHero, theDifficulty);
		Random rng = new Random(theSeed);
		
		
		
		
		return dungeon;
	}
	
	private static void createLayout(Dungeon theDungeon, Random theRng)
	{
		int rows = theDungeon.getRows();
		int cols = theDungeon.getCols();
		
		boolean[][] visted = new boolean[rows][cols];
		
		
		int startRow = theRng.nextInt(rows);
		int startCol = theRng.nextInt(cols);
		visted[startRow][startCol] = true;
		
		Deque<Point> stack = new ArrayDeque<>();
		stack.push(new Point(startRow, startCol));
		
		while(!stack.isEmpty())
		{
			Point current = stack.peek();
			int currentRow = current.x;
			int currentCol = current.y;
			
			
		}
	}
	
}
