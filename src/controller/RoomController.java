package controller;

import java.util.Random;

import model.DungeonTile;
import model.Gold;
import model.HealingPotion;
import model.Pillar;
import model.Rarity;
import model.Room;
import model.VisionPotion;
import model.Weapon;

/**
 * Handles placing loot in {@link Room} objects based on their {@link DungeonTile} type.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Drop gold, potions, weapons, and pillars into rooms.</li>
 *     <li>Apply randomized {@link Rarity} to weapon drops.</li>
 *     <li>Mark rooms as looted after items are placed.</li>
 * </ul>
 * 
 * @author(s): Cristian, Skyler, Hiba
 * @version 23.0.1
 */
public class RoomController {

    /**
     * Random number generator used for loot rarity rolls.
     */
    private final Random myRng = new Random();

    /**
     * Places loot in the given room based on the specified tile type.
     * <p>
     * For each supported {@link DungeonTile}:
     * <ul>
     *     <li>Creates the appropriate item (gold, potion, weapon, or pillar).</li>
     *     <li>Adds the item to the room's inventory via {@link Room#addItem(Object)}.</li>
     *     <li>Marks the room as looted via {@link Room#setIsLooted(boolean)}.</li>
     * </ul>
     * Tiles that are not associated with loot (handled in the {@code default} branch)
     * leave the room unchanged.
     * </p>
     *
     * @param theRoom the room that should receive loot
     * @param theTile the tile type for this room, used to determine what to drop
     */
    public void addLoot(final Room theRoom, final DungeonTile theTile) {

        switch (theTile) {

            // --- Currency ---
            case GOLD -> {
                // Example: always drop 25 gold; adjust amount as desired.
                theRoom.addItem(new Gold(25));
                theRoom.setIsLooted(true);
            }

            // --- Potions ---
            case HEALING_POTION -> {
                theRoom.addItem(new HealingPotion());
                theRoom.setIsLooted(true);
            }

            case VISION_POTION -> {
                theRoom.addItem(new VisionPotion());
                theRoom.setIsLooted(true);
            }

            // --- Weapons ---
            case STICK -> {
                Rarity r = randomRarity();
                theRoom.addItem(Weapon.createStick(r));
                theRoom.setIsLooted(true);
            }

            case SPEAR -> {
                Rarity r = randomRarity();
                theRoom.addItem(Weapon.createSpear(r));
                theRoom.setIsLooted(true);
            }

            case FALCHION -> {
                Rarity r = randomRarity();
                theRoom.addItem(Weapon.createFalchion(r));
                theRoom.setIsLooted(true);
            }

            case FLAIL -> {
                Rarity r = randomRarity();
                theRoom.addItem(Weapon.createFlail(r));
                theRoom.setIsLooted(true);
            }

            case MORNING_STAR -> {
                Rarity r = randomRarity();
                theRoom.addItem(Weapon.createMorningStar(r));
                theRoom.setIsLooted(true);
            }

            // --- Pillars ---
            case ABSTRACTION_PILLAR -> {
                theRoom.addItem(new Pillar('A'));
                theRoom.setIsLooted(true);
            }

            case ENCAPSULATION_PILLAR -> {
                theRoom.addItem(new Pillar('E'));
                theRoom.setIsLooted(true);
            }

            case INHERITANCE_PILLAR -> {
                theRoom.addItem(new Pillar('I'));
                theRoom.setIsLooted(true);
            }

            case POLYMORPHISM_PILLAR -> {
                theRoom.addItem(new Pillar('P'));
                theRoom.setIsLooted(true);
            }

            // Tiles that do not drop loot
            default -> {
                // Nothing dropped in this room.
            }
        }
    }

    /**
     * Rolls a random {@link Rarity} value for weapon drops.
     * <p>
     * Current distribution:
     * <ul>
     *     <li>COMMON: 50% (0–49)</li>
     *     <li>UNCOMMON: 30% (50–79)</li>
     *     <li>RARE: 15% (80–94)</li>
     *     <li>LEGENDARY: 5% (95–99)</li>
     * </ul>
     * </p>
     *
     * @return a randomly selected weapon rarity
     */
    private Rarity randomRarity() {
        int roll = myRng.nextInt(100);
        if (roll < 50) {
            return Rarity.COMMON;
        }
        if (roll < 80) {
            return Rarity.UNCOMMON;
        }
        if (roll < 95) {
            return Rarity.RARE;
        }
        return Rarity.LEGENDARY;
    }
}
