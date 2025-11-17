package Model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Inventory} class in the Dungeon Adventure project.
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.2
 * @date 11/09/25
 */
class InventoryTest {

    /** The Inventory instance used for testing. */
    private Inventory inv;

    /**
     * Sets up a new {@link Inventory} object before each test.
     */
    @BeforeEach
    void setUp() throws Exception {
        inv = new Inventory();
    }

    /** Tests that adding an item makes it detectable via hasItem(). */
    @Test
    void testAddAndHasItem() {
        Pillar pillarA = new Pillar('A');
        inv.addItem(pillarA);
        assertTrue(inv.hasItem(pillarA), "Inventory should contain the added pillar.");
    }

    /** Tests removing any item from inventory. */
    @Test
    void testRemoveItem() {
        Weapon sword = new Weapon("Sword", 15, Rarity.COMMON);
        inv.addItem(sword);
        inv.removeItem(sword);
        assertFalse(inv.hasItem(sword), "Inventory should not contain removed weapon.");
    }

    /** Player cannot exit if fewer than 4 pillars are collected. */
    @Test
    void testCanExitFalseWhenIncomplete() {
        inv.addItem(new Pillar('A'));
        inv.addItem(new Pillar('E'));
        assertFalse(inv.canExit(), "Should not be able to exit with only 2 pillars collected.");
    }

    /** Player can exit when ALL pillars are collected. */
    @Test
    void testCanExitTrueWhenAllPillarsCollected() {
        inv.addItem(new Pillar('A'));
        inv.addItem(new Pillar('E'));
        inv.addItem(new Pillar('I'));
        inv.addItem(new Pillar('P'));
        assertTrue(inv.canExit(), "Should be able to exit once all 4 pillars are collected.");
    }

    /** Inventory must accept different Item subclasses. */
    @Test
    void testInventoryHandlesMultipleItemTypes() {
        HealingPotion heal = new HealingPotion(5, Rarity.UNCOMMON);
        VisionPotion vision = new VisionPotion(3, Rarity.RARE);
        Gold gold = new Gold(50);
        Weapon sword = new Weapon("Sword", 15, Rarity.EPIC);

        inv.addItem(heal);
        inv.addItem(vision);
        inv.addItem(gold);
        inv.addItem(sword);

        assertTrue(inv.hasItem(heal), "Inventory should contain Healing Potion.");
        assertTrue(inv.hasItem(vision), "Inventory should contain Vision Potion.");
        assertTrue(inv.hasItem(gold), "Inventory should contain Gold.");
        assertTrue(inv.hasItem(sword), "Inventory should contain Weapon.");
    }
    
    @Test
    void testWeaponRarityDamageScaling() {
        Weapon common = new Weapon("Sword", 15, Rarity.COMMON);
        Weapon uncommon = new Weapon("Sword", 15, Rarity.UNCOMMON);
        Weapon rare = new Weapon("Sword", 15, Rarity.RARE);
        Weapon epic = new Weapon("Sword", 15, Rarity.EPIC);
        Weapon legendary = new Weapon("Sword", 15, Rarity.LEGENDARY);

        assertEquals(15, common.getDamage());
        assertEquals(17, uncommon.getDamage());
        assertEquals(19, rare.getDamage());
        assertEquals(21, epic.getDamage());
        assertEquals(23, legendary.getDamage());
    }
    
    @Test
    void testHealingPotionRarityScaling() {
        HealingPotion common = new HealingPotion(5, Rarity.COMMON);
        HealingPotion uncommon = new HealingPotion(5, Rarity.UNCOMMON);
        HealingPotion rare = new HealingPotion(5, Rarity.RARE);
        HealingPotion epic = new HealingPotion(5, Rarity.EPIC);
        HealingPotion legendary = new HealingPotion(5, Rarity.LEGENDARY);

        assertEquals(5, common.getHealAmount());
        assertEquals(6, uncommon.getHealAmount());
        assertEquals(7, rare.getHealAmount());
        assertEquals(8, epic.getHealAmount());
        assertEquals(9, legendary.getHealAmount());
    }
    
    @Test
    void testVisionPotionRarityScaling() {
        VisionPotion common = new VisionPotion(3, Rarity.COMMON);
        VisionPotion uncommon = new VisionPotion(3, Rarity.UNCOMMON);
        VisionPotion rare = new VisionPotion(3, Rarity.RARE);
        VisionPotion epic = new VisionPotion(3, Rarity.EPIC);
        VisionPotion legendary = new VisionPotion(3, Rarity.LEGENDARY);

        assertEquals(3, common.getDuration());
        assertEquals(4, uncommon.getDuration());
        assertEquals(5, rare.getDuration());
        assertEquals(6, epic.getDuration());
        assertEquals(7, legendary.getDuration());
    }
}

