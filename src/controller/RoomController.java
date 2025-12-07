package controller;

import java.util.Random;

import model.DungeonTile;
import model.Room;
import model.Rarity;
import model.Weapon;
import model.HealingPotion;
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
        theRoom.addItem(new HealingPotion());
        theRoom.setIsLooted(true);
      }

      case VISION_POTION -> {
        theRoom.addItem(new VisionPotion());
        theRoom.setIsLooted(true);
      }

      // --- Weapon tiles ---
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

        default -> {
            // Nothing dropped in this room.
        }
        }
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