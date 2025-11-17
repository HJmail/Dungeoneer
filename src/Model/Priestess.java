package Model;

public class Priestess extends Hero {

    public Priestess(String theName) {
        super(theName,
              75,     // hit points
              25,     // min damage
              45,     // max damage
              5,      // attack speed
              0.7,    // chance to hit
              0.3);   // chance to block

        myImagePath = "images/priestess.png";
        myGold = 120; // If you want gold → set it here, not in the constructor
    }
    
    @Override
    public String specialSkill(DungeonCharacter opponent) {
        // Priestess heals herself
        int healAmount = (int) (Math.random() * (30 - 20 + 1)) + 20; // Heal 20–30 HP
        myHitPoints += healAmount;

        return myName + " heals for " + healAmount + " hit points!";
    }

}
