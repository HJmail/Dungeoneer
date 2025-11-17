package Model;

public class Thief extends Hero {

    public Thief(String theName) {
        super(theName,
              75,     // hit points
              20,     // min damage
              40,     // max damage
              6,      // attack speed
              0.8,    // chance to hit
              0.4);   // chance to block

        myImagePath = "images/thief.png";
        myGold = 80;
    }
    
    @Override
    public String specialSkill(DungeonCharacter opponent) {
        double roll = Math.random();

        // 40% chance successful → extra attack this round
        if (roll <= 0.4) {
            int damage1 = this.attack(opponent);
            int damage2 = this.attack(opponent);
            return myName + " performs a SURPRISE ATTACK for " + damage1 + 
                   " damage and gets an EXTRA attack for " + damage2 + " damage!";
        }

        // 20% chance caught → no attack at all
        if (roll <= 0.6) {  // 0.4 to 0.6 = 20%
            return myName + " is caught while trying a surprise attack and does NO damage!";
        }

        // 40% normal attack
        int normalDamage = this.attack(opponent);
        if (normalDamage == -1) {
            return myName + "'s surprise attack turns into a normal attack but it MISSES!";
        }
        return myName + "'s surprise attack turns into a normal attack for " + normalDamage + " damage!";
    }

}
