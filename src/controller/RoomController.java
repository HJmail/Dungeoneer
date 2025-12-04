package controller;

import java.util.Random;

import model.DungeonTile;
import model.Room;
import model.Rarity;
import model.Weapon;
import model.HealingPotion;
import model.Hero;
import model.VisionPotion;
import model.Pillar;
import model.Gold;

/**
 * Handles adding loot to rooms.
 */
public class RoomController {

    private final Random myRng = new Random();
    

    /**
     * Places loot in the room based on tile type.
     */
    public void addLoot(Room theRoom, DungeonTile theTile) {

        switch (theTile) {

        case GOLD -> {
            theRoom.addItem(new Gold(25));  // or any amount you want
            theRoom.setIsLooted(true);
        }


        case HEALING_POTION -> {
            theRoom.addItem(new HealingPotion(25, Rarity.COMMON));
            theRoom.setIsLooted(true);
        }

        case VISION_POTION -> {
            theRoom.addItem(new VisionPotion(3, Rarity.UNCOMMON));
            theRoom.setIsLooted(true);
        }

        // --- Weapon tiles ---
        case STICK -> {
            theRoom.addItem(new Weapon("Stick", 8, Rarity.COMMON));
            theRoom.setIsLooted(true);
        }
        case SPEAR -> {
            theRoom.addItem(new Weapon("Spear", 10, Rarity.UNCOMMON));
            theRoom.setIsLooted(true);
        }
        case FALCHION -> {
            theRoom.addItem(new Weapon("Falchion", 12, Rarity.UNCOMMON));
            theRoom.setIsLooted(true);
        }
        case FLAIL -> {
            theRoom.addItem(new Weapon("Flail", 14, Rarity.RARE));
            theRoom.setIsLooted(true);
        }
        case MORNING_STAR -> {
            theRoom.addItem(new Weapon("Morning Star", 16, Rarity.RARE));
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

        default -> {
            // Nothing dropped in this room.
        }
        }
    }
    
    public static String activateEncounter(Hero hero, Room room) {

        // No monsters, no encounter
        if (room.getMonsters().isEmpty()) {
            return "NO_MONSTERS";
        }

        // Run combat
        String result = CombatController.battleMultiple(hero, room.getMonsters());

        // If hero won, clear monsters from the room
        if (result.equals("HERO_WIN")) {
            room.getMonsters().clear();
        }

        return result;
    }

    /** Random rarity helper. */
    private Rarity randomRarity() {
        int roll = myRng.nextInt(100);
        if (roll < 50) return Rarity.COMMON;
        if (roll < 80) return Rarity.UNCOMMON;
        if (roll < 95) return Rarity.RARE;
        return Rarity.LEGENDARY;
    }
}