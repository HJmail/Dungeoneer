package model;

import java.util.ArrayList;


public class Shopkeeper {

    private static final int HEALING_POTION_COST = 25;
    private static final int VISION_POTION_COST = 40;
    private static final int WEAPON_UPGRADE_COST = 100;
    
    private static final ArrayList<Item> myItems = new ArrayList<Item>();

    public String displayItems() {
        return "Welcome to my shop!\n" +
               "1. Healing Potion - " + HEALING_POTION_COST + " gold\n" +
               "2. Vision Potion - " + VISION_POTION_COST + " gold\n" +
               "3. Weapon Upgrade - " + WEAPON_UPGRADE_COST + " gold\n" +
               "4. Stop Shopping\n";
    }
    
    public ArrayList<Item> getItems()
    {
    	return myItems; // not a clone
    }

    public String buyItem(Hero theHero, int theChoice) {
        switch (theChoice) {
            case 1:
                return purchase(theHero, HEALING_POTION_COST, "Healing Potion");
            case 2:
                return purchase(theHero, VISION_POTION_COST, "Vision Potion");
            case 3:
                return upgradeWeapon(theHero);
            case 4:
            	return "Thank you for Shopping.";
            default:
                return "Invalid choice.";
        }
    }

    private String purchase(Hero theHero, int theCost, String theItemName) {
        if (theHero.getGold() < theCost) {
            return "Not enough gold!";
        }

        theHero.setGold(theHero.getGold() - theCost);
        return "You purchased a " + theItemName + "! (Added to inventory soon)";
    }

    private String upgradeWeapon(Hero theHero) {
        if (theHero.getGold() < WEAPON_UPGRADE_COST) {
            return "Not enough gold!";
        }

        theHero.setGold(theHero.getGold() - WEAPON_UPGRADE_COST);
        theHero.setMinDamage(theHero.getMinDamage() + 5);
        theHero.setMaxDamage(theHero.getMaxDamage() + 10);

        return "Your weapon has been upgraded!";
    }
}