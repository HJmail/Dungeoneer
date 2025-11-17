package Model;

public class Warrior extends Hero {

    public Warrior(String theName) {
        super(theName,
              125,   // hit points
              35,    // min damage
              60,    // max damage
              4,     // attack speed
              0.8,   // chance to hit
              0.2);  // chance to block

        myImagePath = "images/warrior.png";
        myGold = 100;
    }
    
    @Override
    public String specialSkill(DungeonCharacter opponent) {
        // 40% chance to succeed
        if (Math.random() <= 0.4) {
            int damage = (int) (Math.random() * (175 - 75 + 1)) + 75;
            opponent.setHitPoints(opponent.getHitPoints() - damage);
            return myName + " performs a CRUSHING BLOW for " + damage + " damage!";
        }

        return myName + " attempts a CRUSHING BLOW but misses!";
    }

}
