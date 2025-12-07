package model;

/**
 * Enumeration of all possible dungeon tile types used to represent the 2D
 * dungeon map. Each tile corresponds to a single character symbol used in
 * ASCII and graphical rendering.
 *
 * This enum supports quick lookup from map characters, and serves as a
 * bridge between textual map representations and game logic.
 *
 * Implements Professor’s project requirements:
 * 
 *  -Entrance ('i'), Exit ('O')
 *  -Pits ('X')
 *  -Healing ('H') and Vision ('V') potions
 *  -Four Pillars of OO: A, E, I, P
 *  -Shopkeeper ('S')
 * 
 *
 * @author Cristian Acevedo-Villasana
 * @version 0.0.3
 * @date 12/05/25
 */
public enum DungeonTile {

    // ─────────────── Terrain ───────────────
    FLOOR('.'),
    WALL('M'),

    // ─────────────── Doors ───────────────
    DOOR_N('^'),
    DOOR_S('v'),
    DOOR_E('>'),
    DOOR_W('<'),

    // ─────────────── Entry / Exit ───────────────
    ENTRANCE('i'),
    EXIT('O'),

    // ─────────────── Hazards ───────────────
    PIT('X'),

    // ─────────────── Loot / Items ───────────────
    GOLD('$'),
    HEALING_POTION('H'),
    VISION_POTION('V'),

    // ─────────────── Weapons ───────────────
    SPEAR('1'),
    FALCHION('2'),
    FLAIL('3'),
    MORNING_STAR('4'),
    STICK('5'),

    // ─────────────── Pillars of OO ───────────────
    ABSTRACTION_PILLAR('A'),
    ENCAPSULATION_PILLAR('C'),   // ⚠ changed to 'C' (for Encapsulation)
    INHERITANCE_PILLAR('I'),
    POLYMORPHISM_PILLAR('P'),

    // ─────────────── NPC ───────────────
    SHOPKEEPER('S'),

    // ─────────────── Map Drawing ───────────────
    HORIZONTAL('-'),
    VERTICAL('|'),
    INTERSECTION('+'),

    // ─────────────── Fallback ───────────────
    VOID(' ');

    /** ASCII symbol representing this tile on the map. */
    private final char symbol;

    DungeonTile(final char c) {
        this.symbol = c;
    }

    public char getSymbol() {
        return symbol;
    }

    /**
     * Returns the DungeonTile associated with a given ASCII symbol.
     * Defaults to FLOOR if no match is found.
     *
     * @param c the character symbol to convert
     * @return corresponding DungeonTile, or FLOOR if none matches
     */
    public static DungeonTile fromSymbol(final char c) {
        for (DungeonTile t : values()) {
            if (t.symbol == c) {
                return t;
            }
        }
        return FLOOR;
    }
}