import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventoryTest {

    private Inventory inv;

    @BeforeEach
    void setUp() throws Exception {
        inv = new Inventory();
    }

    @Test
    void testAddAndHasItem() {
        Pillar pillarA = new Pillar('A');
        inv.addItem(pillarA);
        assertTrue(inv.hasItem(pillarA), "Inventory should contain the added pillar.");
    }

    @Test
    void testRemoveItem() {
        Weapon sword = new Weapon("Sword", 15);
        inv.addItem(sword);
        inv.removeItem(sword);
        assertFalse(inv.hasItem(sword), "Inventory should not contain removed weapon.");
    }

    @Test
    void testCanExitFalseWhenIncomplete() {
        inv.addItem(new Pillar('A'));
        inv.addItem(new Pillar('E'));
        assertFalse(inv.canExit(), "Should not be able to exit with only 2 pillars collected.");
    }

    @Test
    void testCanExitTrueWhenAllPillarsCollected() {
        inv.addItem(new Pillar('A'));
        inv.addItem(new Pillar('E'));
        inv.addItem(new Pillar('I'));
        inv.addItem(new Pillar('P'));
        assertTrue(inv.canExit(), "Should be able to exit once all 4 pillars are collected.");
    }

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
