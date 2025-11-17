package Model;

public class Ogre extends Monster {

    public Ogre(String name,
                int hitPoints,
                int minDamage,
                int maxDamage,
                int attackSpeed,
                double chanceToHit,
                double chanceToHeal,
                int minHeal,
                int maxHeal) {

        super(name, hitPoints, minDamage, maxDamage,
              attackSpeed, chanceToHit, chanceToHeal,
              minHeal, maxHeal);

        myImagePath = "images/ogre.png";
    }
}
