package model;

public class Gremlin extends Monster {

    public Gremlin(String name,
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

        myImagePath = "Dungeoneer_Character/gremlin.png";
    }
}