package model;

import java.io.Serializable;

public class MonsterData implements Serializable {
    private static final long serialVersionUID = 1L;

	public String myName;
    public int myHitPoints;
    public int myMinDamage;
    public int myMaxDamage;
    public int myAttackSpeed;
    public double myChanceToHit;
    public double myChanceToHeal;
    public int myMinHeal;
    public int myMaxHeal;

    public MonsterData() { }
}