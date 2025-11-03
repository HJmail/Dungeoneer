import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Inventory} class in the Dungeon Adventure project.
 * 
 * <p>This test suite verifies that the inventory system correctly manages
 * item addition, removal, and pillar collection logic, ensuring that the
 * player can only exit the dungeon once all four pillars of OOP are obtained.</p>
 * 
 * <p>Each test method validates a specific feature of the {@code Inventory}
 * class using JUnit 5 assertions.</p>
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 11/01/25
 */
class InventoryTest {

    /** The Inventory instance used for testing. */
    private Inventory inv;

    /**
     * Sets up a new {@link Inventory} object before each test.
     * 
     * @throws Exception if an error occurs during setup (not expected)
     */
    @BeforeEach
    void setUp() throws Exception {
        inv = new Inventory();
    }

    /**
     * Tests adding an item to the inventory and verifying its presence using
     * {@link Inventory#hasItem(Item)}.
     * 
     * <p>This ensures that the {@code addItem()} method correctly stores
     * objects in the inventory list.</p>
     */
    @Test
    void testAddAndHasItem() {
        Pillar pillarA = new Pillar('A');
        inv.addItem(pillarA);
        assertTrue(inv.hasItem(pillarA), "Inventory should contain the added pillar.");
    }

    /**
     * Tests removing an item from the inventory.
     * 
     * <p>This verifies that {@link Inventory#removeItem(Item)} successfully
     * deletes an existing item from the list and that {@code hasItem()} reflects
     * this removal correctly.</p>
     */
    @Test
    void testRemoveItem() {
        Weapon sword = new Weapon("Sword", 15);
        inv.addItem(sword);
        inv.removeItem(sword);
        assertFalse(inv.hasItem(sword), "Inventory should not contain removed weapon.");
    }

    /**
     * Tests that the player cannot exit the dungeon if not all pillars have been collected.
     * 
     * <p>This ensures that {@link Inventory#canExit()} returns {@code false} when
     * one or more pillars are missing.</p>
     */
    @Test
    void testCanExitFalseWhenIncomplete() {
        inv.addItem(new Pillar('A'));
        inv.addItem(new Pillar('E'));
        assertFalse(inv.canExit(), "Should not be able to exit with only 2 pillars collected.");
    }

    /**
     * Tests that the player can exit the dungeon once all four pillars are collected.
     * 
     * <p>This verifies that {@link Inventory#canExit()} correctly returns
     * {@code true} when all pillars (A, E, I, P) are present.</p>
     */
    @Test
    void testCanExitTrueWhenAllPillarsCollected() {
        inv.addItem(new Pillar('A'));
        inv.addItem(new Pillar('E'));
        inv.addItem(new Pillar('I'));
        inv.addItem(new Pillar('P'));
        assertTrue(inv.canExit(), "Should be able to exit once all 4 pillars are collected.");
    }

    /**
     * Tests that the inventory can handle multiple different item types at once.
     * 
     * <p>This ensures that {@link Inventory#addItem(Item)} correctly adds various
     * subclasses of {@link Item}, such as potions, gold, and weapons, without issue.</p>
     */
    @Test
    void testInventoryHandlesMultipleItemTypes() {
        HealingPotion heal = new HealingPotion(5);
        VisionPotion vision = new VisionPotion(3);
        Gold gold = new Gold(50);
        Weapon sword = new Weapon("Sword", 20);

        inv.addItem(heal);
        inv.addItem(vision);
        inv.addItem(gold);
        inv.addItem(sword);

        assertTrue(inv.hasItem(heal), "Inventory should contain Healing Potion.");
        assertTrue(inv.hasItem(gold), "Inventory should contain Gold.");
        assertTrue(inv.hasItem(sword), "Inventory should contain Weapon.");
    }

}

