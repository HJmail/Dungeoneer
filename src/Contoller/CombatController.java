package controller;

import java.util.List;

import model.Hero;
import model.Monster;
import view.GameView;

public class CombatController {

  private static GameView myView;

  public static void setView(GameView theView) {
    myView = theView;
  }

  private static void log(String msg) {
    if (myView != null) {
      myView.showMessage(msg);
    } else {
      System.out.println(msg);  // fallback
    }
  }

  public static String battle(Hero hero, Monster monster) {

    int heroSpeed = hero.getAttackSpeed();
    int monsterSpeed = monster.getAttackSpeed();

    int heroAttacks = Math.max(1, heroSpeed);
    int monsterAttacks = Math.max(1, monsterSpeed);

    log("\n️ A wild " + monster.getName() + " appears!");
    log("Combat begins!\n");

    while (hero.isAlive() && monster.isAlive()) {

      // HERO TURN
      for (int i = 0; i < heroAttacks && monster.isAlive(); i++) {

        int damage = hero.attack(monster);

        if (damage == -1) {
          log(hero.getName() + " MISSES!");
        } else {
          log(hero.getName() + " hits " + monster.getName() 
               + " for " + damage + " damage!");
        }

        if (!monster.isAlive()) {
          log(monster.getName() + " has been defeated!");
          return "HERO_WIN";
        }
      }

      // MONSTER TURN
      for (int i = 0; i < monsterAttacks && hero.isAlive(); i++) {
        int damage = monster.attack(hero);

        if (damage == -1) {
          log(monster.getName() + " MISSES!");
        } else if (hero.defend()) {
          log(hero.getName() + " BLOCKS the attack!");
        } else {
          hero.setHitPoints(hero.getHitPoints() - damage);
          log(monster.getName() + " hits " + hero.getName() 
              + " for " + damage + " damage!");
        }
      }

      if (monster.isAlive()) {
        String healMsg = monster.heal();
        if (healMsg.contains("heals")) {
          log(" " + healMsg);
        }
      }

      log("\n[Status] " + hero.getName() + ": " 
          + hero.getHitPoints() + " HP | " 
          + monster.getName() + ": " 
          + monster.getHitPoints() + " HP\n");
    }
    
    if (!hero.isAlive()) {
      log("\n " + hero.getName() + " has been slain...");
      return "HERO_LOSE";
    }

    return "HERO_WIN";
  }

  public static String battleMultiple(Hero hero, List<Monster> monsters) {
    log("\n️ A group of monsters appears!");
    monsters.removeIf(m -> !m.isAlive());

    for (Monster monster : monsters) {

      log("\n========== Encounter ==========");
      log(" Monster: " + monster.getName());
      log("===============================\n");

      String result = battle(hero, monster);

      if (result.equals("HERO_LOSE")) {
        return "HERO_LOSE";
      }
    }

    log("\n All monsters defeated!");
    return "HERO_WIN";
  }
}