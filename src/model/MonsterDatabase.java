package model;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads monster statistics from the SQLite database (monsters.db).
 * This class reads the "monsters" table and builds MonsterData objects.
 *
 * @author Hiba
 * @version 1.0 (03/02/2025)
 */
public class MonsterDatabase {

    /** Relative path to the SQLite database file. */
    private static final String DB_PATH = "data/monsters.db";

    /**
     * Loads all monsters from the database and returns them in a map.
     *
     * @return A map where the key is the monster name, and the value is the MonsterData object.
     */
    public static Map<String, MonsterData> loadMonsters() {
        Map<String, MonsterData> monsterMap = new HashMap<>();

        String url = "jdbc:sqlite:" + DB_PATH;
        String sql = "SELECT * FROM monsters;";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                MonsterData data = new MonsterData();

                data.myName = rs.getString("name");
                data.myHitPoints = rs.getInt("hit_points");
                data.myMinDamage = rs.getInt("min_damage");
                data.myMaxDamage = rs.getInt("max_damage");
                data.myAttackSpeed = rs.getInt("attack_speed");
                data.myChanceToHit = rs.getDouble("chance_to_hit");
                data.myChanceToHeal = rs.getDouble("chance_to_heal");
                data.myMinHeal = rs.getInt("min_heal");
                data.myMaxHeal = rs.getInt("max_heal");

                monsterMap.put(data.myName, data);
            }

        } catch (SQLException e) {
            System.err.println("Error loading monster data: " + e.getMessage());
        }

        return monsterMap;
    }
}
