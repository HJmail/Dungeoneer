package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import Model.Gremlin;
import Model.Hero;
import Model.Warrior;
import Model.Monster;
import Model.Ogre;
import Model.Shopkeeper;
import Model.Skeleton;

class CombatTest {

    private Hero hero;
    private Monster ogre;
    private Monster skeleton;
    private Monster gremlin;
    private Shopkeeper shopkeeper;

    @BeforeEach
    void setUp() {
        hero = new Warrior("Hiba");

        ogre = new Ogre("Ogre", 120, 15, 25, 2, 0.6, 0.1, 10, 20);
        skeleton = new Skeleton("Skeleton", 80, 10, 15, 3, 0.7, 0.2, 5, 10);
        gremlin = new Gremlin("Gremlin", 60, 8, 12, 4, 0.5, 0.3, 5, 15);

        shopkeeper = new Shopkeeper();

        hero.setImagePath("images/warrior.png");
        ogre.setImagePath("images/ogre.png");
        skeleton.setImagePath("images/skeleton.png");
        gremlin.setImagePath("images/gremlin.png");
    }

    /** Test 1v1 combat: hero vs ogre */
    @Test
    void test1v1Combat() {
        simulateCombat(hero, ogre);
        assertTrue(hero.isAlive(), "Hero should survive the 1v1 fight");
        assertFalse(ogre.isAlive(), "Ogre should be defeated");
    }

    /** Test 1v2 combat: hero vs skeleton + gremlin */
    @Test
    void test1v2Combat() {
        simulateCombat(hero, skeleton, gremlin);
        boolean heroAlive = hero.isAlive();
        assertTrue(heroAlive, "Hero should survive or at least one monster defeated");
        assertTrue(!skeleton.isAlive() || !gremlin.isAlive(), "At least one monster should be dead");
    }

    /** Test 1v3 combat: hero vs ogre + skeleton + gremlin */
    @Test
    void test1v3Combat() {
        simulateCombat(hero, ogre, skeleton, gremlin);
        boolean allMonstersDead = !ogre.isAlive() && !skeleton.isAlive() && !gremlin.isAlive();
        assertTrue(hero.isAlive() || allMonstersDead,
                   "Either hero wins or all monsters defeated");
    }

    /** Test shop interaction: hero buys an item */
    @Test
    void testShopInteraction() {
        int goldBefore = hero.getGold();
        shopkeeper.displayItems();
        shopkeeper.buyItem(hero, 1);
        int goldAfter = hero.getGold();

        assertTrue(goldAfter <= goldBefore,
                   "Hero’s gold should decrease or stay the same after purchase");
    }

    /**
     * Helper method that simulates turn-based combat.
     */
    private void simulateCombat(Hero hero, Monster... monsters) {
        List<String> combatLog = new ArrayList<>();

        for (Monster monster : monsters) {
            combatLog.add(hero.getName() + " engages " + monster.getName());

            while (hero.isAlive() && monster.isAlive()) {
                hero.attack(monster);
                if (monster.isAlive()) {
                    monster.attack(hero);
                }
            }

            combatLog.add("Battle with " + monster.getName() + " ended.");
        }

        // You could assert the log contents if needed
        assertFalse(combatLog.isEmpty(), "Combat log should record the battle");
    }
}
