package model;

public enum DungeonTile {

    // Basic terrain
    FLOOR('.'),
    WALL('M'),

    // Doors
    DOOR_N('^'),
    DOOR_S('v'),
    DOOR_E('>'),
    DOOR_W('<'),

    // Entrance & Exit
    ENTRANCE('E'),
    EXIT('O'),

    // Pit
    PIT('X'),

    // Items placed on the map
    GOLD('$'),
    HEALING_POTION('H'),
    VISION_POTION('V'),

    // Individual weapon tiles (map file can use '1'–'5')
    SPEAR('1'),
    FALCHION('2'),
    FLAIL('3'),
    MORNING_STAR('4'),
    STICK('5'),

    // Pillars
    ABSTRACTION_PILLAR('A'),
    ENCAPSULATION_PILLAR('C'),   // use 'C' so it doesn't clash with ENTRANCE 'E'
    INHERITANCE_PILLAR('I'),
    POLYMORPHISM_PILLAR('P'),

    // Maze drawing characters
    HORIZONTAL('-'),
    VERTICAL('|'),
    INTERSECTION('+'),

    // Optional: empty/void space
    VOID(' ');

    private final char symbol;

    DungeonTile(final char c) {
        symbol = c;
    }

    public char getSymbol() {
        return symbol;
    }

    /** Find a tile by ASCII symbol. */
    public static DungeonTile fromSymbol(final char c) {
        for (DungeonTile t : values()) {
            if (t.symbol == c) {
                return t;
            }
        }
        return FLOOR; // default
    }
}
