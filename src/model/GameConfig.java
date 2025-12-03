package model;

public class GameConfig 
{
	final private Hero myHero;
	final private int myDifficulty;
	final private long mySeed;
	
	public GameConfig(final Hero theHero,
					  final int theDifficulty,
					  final long theSeed)
	{
		myHero = theHero;
		myDifficulty = theDifficulty;
		mySeed = theSeed;
	}
	
	public Hero getHero()
	{
		return myHero;
	}
	
	public int getDifficulty()
	{
		return myDifficulty;
	}
	
	public long getSeed()
	{
		return mySeed;
	}
}