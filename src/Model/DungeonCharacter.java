package Model;

/**
 * Abstract base class representing all characters in the dungeon. Supports both
 * console and GUI-based interactions.
 *
 * @author Hiba
 */
public abstract class DungeonCharacter {

	/** Character's name. */
	protected String myName;

	/** Character's health points. */
	protected int myHitPoints;

	/** Character's minimum attack damage. */
	protected int myMinDamage;

	/** Character's maximum attack damage. */
	protected int myMaxDamage;

	/** Character's attack speed. */
	protected int myAttackSpeed;

	/** Character's chance to successfully hit an opponent. */
	protected double myChanceToHit;

	/** Gold carried (mainly used by Hero). */
	protected int myGold;

	/** Path to this character's image for GUI use. */
	protected String myImagePath;

	/**
	 * Constructs a DungeonCharacter with its stats. This is used by both Monsters
	 * and Heroes.
	 */
	protected DungeonCharacter(String theName, int theHitPoints, int theMinDamage, int theMaxDamage, int theAttackSpeed,
			double theChanceToHit) {

		myName = theName;
		myHitPoints = theHitPoints;
		myMinDamage = theMinDamage;
		myMaxDamage = theMaxDamage;
		myAttackSpeed = theAttackSpeed;
		myChanceToHit = theChanceToHit;

		myGold = 0; // default
		myImagePath = "images/default.png";
	}

	/** Returns true if the character is still alive. */
	public boolean isAlive() {
		return myHitPoints > 0;
	}

	/** Standard attack. */
	public int attack(DungeonCharacter theOpponent) {
		if (Math.random() <= myChanceToHit) {
			int damage = (int) (Math.random() * (myMaxDamage - myMinDamage + 1)) + myMinDamage;
			theOpponent.myHitPoints -= damage;
			return damage; // IMPORTANT: return value instead of printing
		}
		return -1; // tells the caller it missed
	}

	public String getName() {
		return myName;
	}

	public int getHitPoints() {
		return myHitPoints;
	}

	public void setHitPoints(final int theHitPoints) {
		myHitPoints = theHitPoints;
	}

	public int getGold() {
		return myGold;
	}

	public void setGold(final int theGold) {
		myGold = theGold;
	}

	public int getAttackSpeed() {
		return myAttackSpeed;
	}

	public double getChanceToHit() {
		return myChanceToHit;
	}

	public int getMinDamage() {
		return myMinDamage;
	}

	public int getMaxDamage() {
		return myMaxDamage;
	}
	
	public void setMinDamage(int theMinDamage) {
	    myMinDamage = theMinDamage;
	}

	public void setMaxDamage(int theMaxDamage) {
	    myMaxDamage = theMaxDamage;
	}

	public String getImagePath() {
		return myImagePath;
	}

	public void setImagePath(final String thePath) {
		myImagePath = thePath;
	}

	@Override
	public String toString() {
		return myName + " [HP: " + myHitPoints + ", Gold: " + myGold + "]";
	}
}
