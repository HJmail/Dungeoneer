package view;

import model.Dungeon;
import model.Hero;

public interface GameView  {
  /**
   * Prints given message or prompt into console.
   * 
   * <p>@param message the message to print.
   */
  void showMessage(String message);

  /**
   * Prints the given dungeon into console.
   * 
   * <p>@param theDungeon the given dungeon.
   */
  void showDungeon(Dungeon theDungeon);
    
  /**
   * Prints hero's stats into console.
   * 
   * <p>@param theHero the given hero.
   */
  void showHeroStats(Hero theHero);
}