package Model;

/**
 * Test class to simulate 1v1, 1v2, and 1v3 combat scenarios.
 * @author Hiba
 * @version 0.0.3 10/27/2025
 */
public class CombatTest {

    public static void main(String[] args) {
        Hero hero = new Warrior("Hiba");

        Monster monster1 = new Ogre();
        Monster monster2 = new Skeleton();
        Monster monster3 = new Gremlin();

        System.out.println("=== 1v1 COMBAT ===");
        simulateCombat(hero, monster1);

        System.out.println("\n=== 1v2 COMBAT ===");
        simulateCombat(hero, monster2, monster3);

        System.out.println("\n=== 1v3 COMBAT ===");
        simulateCombat(hero, monster1, monster2, monster3);

        Shopkeeper shopkeeper = new Shopkeeper();
        shopkeeper.displayItems();
        shopkeeper.buyItem(hero, 1);

        System.out.println("\nAfter transactions: " + hero);
    }

    private static void simulateCombat(Hero hero, Monster... monsters) {
        for (Monster monster : monsters) {
            System.out.println(hero.getName() + " engages " + monster.getName());
            while (hero.isAlive() && monster.isAlive()) {
                hero.attack(monster);
                if (monster.isAlive()) {
                    monster.attack(hero);
                }
            }
            System.out.println("Battle with " + monster.getName() + " ended.");
        }
    }
}
