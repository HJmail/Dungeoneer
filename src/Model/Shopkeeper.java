package Model;
	/**
	 * Represents a shopkeeper that sells items to the hero.
	 * @author Hiba Jmaileh
	 */
	public class Shopkeeper {

		public String displayItems() {
		    return "Welcome to my shop!\n" +
		           "1. Healing Potion - 25 gold\n" +
		           "2. Vision Potion - 40 gold\n" +
		           "3. Weapon Upgrade - 100 gold\n";
		}

		public String buyItem(Hero hero, int choice) {
		    switch (choice) {
		        case 1:
		            return purchase(hero, 25, "Healing Potion");
		        case 2:
		            return purchase(hero, 40, "Vision Potion");
		        case 3:
		            return purchase(hero, 100, "Weapon Upgrade");
		        default:
		            return "Invalid choice.";
		    }
		}

		private String purchase(Hero hero, int cost, String itemName) {
		    if (hero.getGold() >= cost) {
		        hero.setGold(hero.getGold() - cost);
		        return "You purchased a " + itemName + "!";
		    } else {
		        return "Not enough gold!";
		    }
		}

}
