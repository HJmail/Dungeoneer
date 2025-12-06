package model;

import java.io.Serializable;

public class Gremlin extends Monster implements Serializable {

    private static final long serialVersionUID = 1L;

    public Gremlin(String name, int hp, int mind, int maxd, int spd,
                   double hit, double healChance, int minHeal, int maxHeal) {

        super(name, hp, mind, maxd, spd, hit, healChance, minHeal, maxHeal);
        myImagePath = "images/gremlin.png";
    }
}
