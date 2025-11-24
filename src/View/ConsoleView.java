package view;

import model.Dungeon;
import model.Hero;

public class ConsoleView implements GameView {

	@Override
	public void showMessage(final String theMessage) 
	{
		System.out.println(theMessage);
	}

	@Override
	public void showDungeon(final Dungeon theDungeon) 
	{
		System.out.println("\n" + theDungeon);
	}

	@Override
	public void showHeroStats(final Hero theHero) 
	{
		System.out.println(theHero);
	}

}