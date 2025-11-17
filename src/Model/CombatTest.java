package Model;

import java.util.ArrayList;
import java.util.List;

package Model;

/**
 * Test class to simulate 1v1, 1v2, and 1v3 combat scenarios.
 * Updated to support potential GUI integration.
 * 
 * @author Hiba
 * @version 0.0.4 10/27/2025
 */
public class CombatTest {

    public static void main(String[] args) {
        Hero hero = new Warrior("Hiba");

        Monster monster1 = new Ogre();
        Monster monster2 = new Skeleton();
        Monster monster3 = new Gremlin();

        hero.setImagePath("images/warrior.png");
        monster1.setImagePath("images/ogre.png");
        monster2.setImagePath("images/skeleton.png");
        monster3.setImagePath("images/gremlin.png");

        // Combat tests
        log("\n=== 1v1 COMBAT ===");
        simulateCombat(hero, monster1);

        log("\n=== 1v2 COMBAT ===");
        simulateCombat(hero, monster2, monster3);

        log("\n=== 1v3 COMBAT ===");
        simulateCombat(hero, monster1, monster2, monster3);

        // Shop interaction
        Shopkeeper shopkeeper = new Shopkeeper();
        shopkeeper.displayItems();
        shopkeeper.buyItem(hero, 1);

        log("\nAfter transactions: " + hero);
    }

    /**
     * Simulates combat between a hero and one or more monsters.
     * Stores logs for potential GUI use.
     */
    private static void simulateCombat(Hero hero, Monster... monsters) {
        List<String> combatLog = new ArrayList<>();

        for (Monster monster : monsters) {
            combatLog.add(hero.getName() + " engages " + monster.getName());
            log(hero.getName() + " engages " + monster.getName());

            while (hero.isAlive() && monster.isAlive()) {
                hero.attack(monster);
                if (monster.isAlive()) {
                    monster.attack(hero);
                }
            }
            combatLog.add("Battle with " + monster.getName() + " ended.");
            log("Battle with " + monster.getName() + " ended.");
        }
    }

    /**
     * Helper method for logging (console for now, GUI later).
     */
    private static void log(String message) {
        System.out.println(message);
    }
}
