package view;


import java.util.ArrayList;

import model.Direction;
import model.Dungeon;
import model.GameConfig;
import model.Inventory;
import model.Item;

public interface GameView 
{
	/**
	 * Prints given message or prompt into console.
	 * @param message the message to print.
	 */
	void showMessage(final String message);
	
	void askInventory(final Inventory theInventory);
	
	/**
	 * Prints the given dungeon into console.
	 * @param theDungeon the given dungeon.
	 */
    void showDungeon(final Dungeon theDungeon);
    
    /**
     *  Gets needed data for the game set up.
     * @return GameConfig containing all game set up.
     */
    GameConfig askGameConfig();
    
    /**
     * This shows the Shop Items for a given shopkeeper's items.
     */
    void showShopItems(final ArrayList<Item> theItem);
    
    /**
     * This asks the user to select form attack or special attack while in combat.
     */
    void askCombat();
    
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
