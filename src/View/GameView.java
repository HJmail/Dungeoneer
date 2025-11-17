package View;

import Model.Dungeon;
import Model.Hero;

public interface GameView 
{
	/**
	 * Prints given message or prompt into console.
	 * @param message the message to print.
	 */
	void showMessage(String message);
	
	/**
	 * Prints the given dungeon into console.
	 * @param theDungeon the given dungeon.
	 */
    void showDungeon(Dungeon theDungeon);
    
    /**
     * Prints hero's stats into console.
     * @param theHero the given hero.
     */
    void showHeroStats(Hero theHero);
}
