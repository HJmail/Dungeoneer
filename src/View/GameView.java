package View;

import java.util.ArrayList;

import Model.Direction;
import Model.Dungeon;
import Model.GameConfig;
import Model.Hero;
import Model.Item;

public interface GameView 
{
	
	
	/**
	 * Prints given message or prompt into console.
	 * @param message the message to print.
	 */
	void showMessage(final String message);
	
	/**
	 * Prints the given dungeon into console.
	 * @param theDungeon the given dungeon.
	 */
    void showDungeon(final Dungeon theDungeon);
    
    GameConfig askGameConfig();
    
    /**
     * This shows the Shop Items for a given shopkeeper's items.
     */
    void showShopItems(final ArrayList<Item> theItem);
    
    
    /**
     * This method prompts user for input for the shop.
     * @return Returns the user shop choice input.
     */
    int askShop();
    
    void showPit(final int thePitDmg);
    
    /**
     * This method prompt user for a direction for moving.
     * @return Returns the user Direction input.
     */
    Direction askDirection();
    
    void showPillar(final char theChar);
    
    /**
     * Game over view just indicated to the player that the game is over.
     */
    void gameOver();
}
