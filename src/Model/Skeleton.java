package model;

public class Skeleton extends Monster {

    public Skeleton(String name,
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

        myImagePath = "images/skeleton.png";
    }
}