package edu.uw.tcss.dungeoneer.model;

public class Shopkeeper {
	 /**
     * Displays the items the shopkeeper has for sale.
     */
    public void displayItems() {
        System.out.println("Welcome to my shop!");
        System.out.println("1. Healing Potion - 25 gold");
        System.out.println("2. Vision Potion - 40 gold");
        System.out.println("3. Weapon Upgrade - 100 gold");
    }

    /**
     * Handles the hero's purchase based on input.
     * @param hero the hero buying the item.
     * @param choice the player's selection.
     */
    public void buyItem(Hero hero, int choice) {
        switch (choice) {
            case 1:
                purchase(hero, 25, "Healing Potion");
                break;
            case 2:
                purchase(hero, 40, "Vision Potion");
                break;
            case 3:
                purchase(hero, 100, "Weapon Upgrade");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Helper method to handle gold deduction and confirmation.
     */
    private void purchase(Hero hero, int cost, String itemName) {
        if (hero.getGold() >= cost) {
            hero.setGold(hero.getGold() - cost);
            System.out.println("You purchased a " + itemName + "!");
            // In the future: hero.getInventory().add(new Potion());
        } else {
            System.out.println("Not enough gold!");
        }
    }
}
