/**
 * Represents the rarity level of an item. Labels are shared across item types,
 * but each item interprets the label's effect differently (weapons -> damage,
 * potions -> duration/strength).
 * 
 * <p>@author Cristian Acevedo-Villasana
 * 
 * <p>@version 0.0.1
 * 
 * <p>@date 11/09/25
 */
public enum Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY
}
