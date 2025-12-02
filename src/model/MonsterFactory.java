package model;

import java.util.Map;

/**
 * A factory class that creates Monster objects using
 * the data loaded from the SQLite database. This class only
 * handles object creation — controller code will use this
 * factory when generating monsters in the dungeon.
 *
 * @author Hiba
 * @version 1.0 (03/02/2025)
 */
public class MonsterFactory {

    /** Cached monster data loaded from the database. */
    private static final Map<String, MonsterData> MY_MONSTER_STATS = MonsterDatabase.loadMonsters();

    /**
     * Creates a Monster object based on the given monster name.
     *
     * @param theName The type of monster to create (e.g., "Ogre").
     * @return A Monster object with stats loaded from the database.
     */
    public static Monster createMonster(final String theName) {

        MonsterData data = MY_MONSTER_STATS.get(theName);

        if (data == null) {
            System.err.println("Monster type not found in database: " + theName);
            return null;
        }

        switch (theName) {

            case "Ogre":
                return new Ogre(
                        data.myName,
                        data.myHitPoints,
                        data.myMinDamage,
                        data.myMaxDamage,
                        data.myAttackSpeed,
                        data.myChanceToHit,
                        data.myChanceToHeal,
                        data.myMinHeal,
                        data.myMaxHeal
                );

            case "Skeleton":
                return new Skeleton(
                        data.myName,
                        data.myHitPoints,
                        data.myMinDamage,
                        data.myMaxDamage,
                        data.myAttackSpeed,
                        data.myChanceToHit,
                        data.myChanceToHeal,
                        data.myMinHeal,
                        data.myMaxHeal
                );

            case "Gremlin":
                return new Gremlin(
                        data.myName,
                        data.myHitPoints,
                        data.myMinDamage,
                        data.myMaxDamage,
                        data.myAttackSpeed,
                        data.myChanceToHit,
                        data.myChanceToHeal,
                        data.myMinHeal,
                        data.myMaxHeal
                );

            default:
                System.err.println("Unknown monster type: " + theName);
                return null;
        }
    }
}
