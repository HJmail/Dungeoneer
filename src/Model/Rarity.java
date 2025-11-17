/**
 * Represents the rarity level of an item. Labels are shared across item types,
 * but each item interprets the label's effect differently (weapons -> damage,
 * potions -> duration/strength).
 * 
 * @author Cristian Acevedo-Villasana
 * @version 0.0.1
 * @date 11/09/25
 */
public enum Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY
}
