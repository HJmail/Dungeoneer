package View;

import java.util.ArrayList;

import Model.Direction;
import Model.Dungeon;
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
    
    /**
     * This shows the Shop Items for a given shopkeeper's items.
     */
    void showShopItems(final ArrayList<Item> theItem);
    
    /**
     * This method prompts user for input for the shop.
     * @return Returns the user shop choice input.
     */
    int askShop();
    
    /**
     * This method prompt user for a direction for moving.
     * @return Returns the user Direction input.
     */
    Direction askDirection();
    
    /**
     * This method prints the start of combat details.
     */
    void showCombatStart();
    
    /**
     *  This method prints the end of combat details
     */
    void showCombatResult();
}
