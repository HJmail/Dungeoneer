package model;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * A Room in the dungeon using the NEW DungeonTile system.
 */
public class Room {

    /** Doors/Directions available. */
    private EnumSet<Direction> myDirections = EnumSet.noneOf(Direction.class);

    /** The tile type of this room (gold, pit, weapon, pillar, floor, start, exit). */
    private DungeonTile myTile = DungeonTile.FLOOR;

    /** Items inside this room. */
    private final List<Item> myItems = new ArrayList<>();

    /** Monsters inside this room. */
    private final List<Monster> myMonsters = new ArrayList<>();

    /** If the hero is here. */
    private Hero myHero;

    /** Depth in the maze tree. */
    private int myDepth;

    /** Pit flag. */
    private boolean myHasPit = false;


    // -------------------------------------------------------------
    // Basic Getters/Setters
    // -------------------------------------------------------------

    public EnumSet<Direction> getDirections() {
        return myDirections;
    }

    public void setDirections(EnumSet<Direction> dirs) {
        myDirections = EnumSet.copyOf(dirs);
    }

    public int getDepth() {
        return myDepth;
    }

    public void setDepth(int d) {
        myDepth = d;
    }

    public DungeonTile getTile() {
        return myTile;
    }

    public void setTile(DungeonTile t) {
        myTile = t;
    }

    public boolean hasPit() {
        return myHasPit;
    }

    public void setPit(boolean hasPit) {
        myHasPit = hasPit;
    }


    // -------------------------------------------------------------
    // Item Handling
    // -------------------------------------------------------------

    public void addItem(Item i) {
        if (i != null) {
            myItems.add(i);
        }
    }

    public List<Item> getItems() {
        return myItems;
    }


    // -------------------------------------------------------------
    // Monster Handling
    // -------------------------------------------------------------

    public void addMonster(Monster m) {
        if (m != null) {
            myMonsters.add(m);
        }
    }

    public List<Monster> getMonsters() {
        return myMonsters;
    }


    // -------------------------------------------------------------
    // Hero Enter/Exit
    // -------------------------------------------------------------

    public void enter(Hero h) {
        myHero = h;
        activateRoom();
    }

    public void exit() {
        myHero = null;
    }


    // -------------------------------------------------------------
    // Room Activation Logic
    // -------------------------------------------------------------

    private void activateRoom() {
        if (myHero == null) return;

        // Pit damage (if flagged)
        if (myHasPit) {

            int damage = (int)(Math.random() * 20) + 1;

            int newHP = myHero.getHitPoints() - damage;
            if (newHP < 0) newHP = 0;

            myHero.setHitPoints(newHP);

            System.out.println(
                myHero.getName() + " fell into a pit for " + damage + " damage! HP now " + newHP
            );
        }
    }
    
 // -------------------------------------------------------------
 // Additional Fields Required by Controllers
 // -------------------------------------------------------------

 /** Whether the room's loot (items, gold, etc.) has already been taken. */
 private boolean myIsLooted = false;

 /** Optional shopkeeper (some dungeon layouts include shops). */
 private Shopkeeper myShopkeeper;



 // -------------------------------------------------------------
 // Looting / Loot State
 // -------------------------------------------------------------

 public boolean isLooted() {
     return myIsLooted;
 }

 public void setIsLooted(boolean looted) {
     myIsLooted = looted;
 }



 // -------------------------------------------------------------
 // Shopkeeper (Optional Room Feature)
 // -------------------------------------------------------------

 public Shopkeeper getShopkeeper() {
     return myShopkeeper;
 }

 public void setShopkeeper(Shopkeeper s) {
     myShopkeeper = s;
 }



 // -------------------------------------------------------------
 // Monster / Item Setters (Needed by RoomController & Generator)
 // -------------------------------------------------------------

 public void setMonsters(List<Monster> list) {
     myMonsters.clear();
     if (list != null) {
         myMonsters.addAll(list);
     }
 }

 public void setItems(List<Item> list) {
     myItems.clear();
     if (list != null) {
         myItems.addAll(list);
     }
 }
//-------------------------------------------------------------
//Character for GUI Map Rendering
//-------------------------------------------------------------

/**
* Returns a character used by ConsoleView / DungeonBoardPanel.
*/
 /**
  * Returns a display character for ConsoleView / GUI.
  */
 public char getRoomChar() {

     switch (myTile) {

         // Basic terrain
         case FLOOR: return '.';
         case WALL: return 'M';

         // Doors
         case DOOR_N: return '^';
         case DOOR_S: return 'v';
         case DOOR_E: return '>';
         case DOOR_W: return '<';

         // Entrance & Exit
         case ENTRANCE: return 'E';
         case EXIT: return 'O';

         // Pit
         case PIT: return 'X';

         // Items
         case GOLD: return '$';
         case HEALING_POTION: return 'H';
         case VISION_POTION: return 'V';

         // Weapons
         case SPEAR: return '1';
         case FALCHION: return '2';
         case FLAIL: return '3';
         case MORNING_STAR: return '4';
         case STICK: return '5';

         // Pillars
         case ABSTRACTION_PILLAR: return 'A';
         case ENCAPSULATION_PILLAR: return 'C';
         case INHERITANCE_PILLAR: return 'I';
         case POLYMORPHISM_PILLAR: return 'P';

         // Maze characters
         case HORIZONTAL: return '-';
         case VERTICAL: return '|';
         case INTERSECTION: return '+';

         // Empty/void space
         case VOID: return ' ';

         default:
             return ' ';
     }
 }

//-------------------------------------------------------------
//Exit with Direction (fix for callers using exit(Direction))
//-------------------------------------------------------------

public void exit(Direction d) {
  // Direction unused here but required for RoomController / Dungeon
  myHero = null;
}

}
