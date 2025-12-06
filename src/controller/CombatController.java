package controller;

import java.util.List;

import model.Hero;
import model.Monster;
import view.GameView;

public class CombatController {

    private static Hero myHero;
    private static Monster myMonster;
    private static GameView myView;

    private static int myHeroAttacksRemaining;
    private static int myMonsterAttacksRemaining;

    private static boolean myBattleActive;

    // -----------------------------------------------------
    // SET VIEW
    // -----------------------------------------------------
    public static void setView(final GameView theView) {
        myView = theView;
    }

    private static void log(final String theMessage) {
        if (myView != null) {
            myView.showMessage(theMessage);
        } else {
            System.out.println(theMessage);
        }
    }

    // -----------------------------------------------------
    // START BATTLE (CALLED BY DUNGEON LOGIC OR GUI)
    // -----------------------------------------------------
    public static void startBattle(final Hero theHero,
                                   final Monster theMonster,
                                   final GameView theView) {

        myHero = theHero;
        myMonster = theMonster;
        myView = theView;

        myBattleActive = true;

        myHeroAttacksRemaining = Math.max(1, myHero.getAttackSpeed());
        myMonsterAttacksRemaining = Math.max(1, myMonster.getAttackSpeed());

        log("A wild " + myMonster.getName() + " appears!");
        log("Combat begins!");

        myView.showHeroStats(myHero);

        nextTurn();
    }

    // -----------------------------------------------------
    // NEXT TURN FLOW CONTROLLER
    // -----------------------------------------------------
    public static void nextTurn() {

        if (!myBattleActive) {
            return;
        }

        // Hero dead?
        if (!myHero.isAlive()) {
            log(myHero.getName() + " has been defeated...");
            myBattleActive = false;
            return;
        }

        // Monster dead?
        if (!myMonster.isAlive()) {
            log(myMonster.getName() + " has been slain!");
            myBattleActive = false;
            return;
        }

        // If hero still has attacks this round
        if (myHeroAttacksRemaining > 0) {
            myView.askCombatChoice(myHero, myMonster);
            return;
        }

        // Otherwise, monster takes turn
        monsterTurn();
    }

    // -----------------------------------------------------
    // HERO ACTION (NORMAL OR SPECIAL)
    // CALLED BY GUI BUTTONS
    // -----------------------------------------------------
    public static void heroAction(final String theChoice) {

        if (!myBattleActive) {
            return;
        }

        String resultMessage;

        // Special skill
        if ("SPECIAL".equals(theChoice)) {
            resultMessage = myHero.specialSkill(myMonster);
            log(resultMessage);

        } else { // Normal attack
            int damage = myHero.attack(myMonster);

            if (damage == -1) {
                log(myHero.getName() + " MISSES!");
            } else {
                log(myHero.getName() + " hits "
                    + myMonster.getName() + " for " + damage + " damage!");
            }
        }

        myHeroAttacksRemaining--;

        // If monster died
        if (!myMonster.isAlive()) {
            log(myMonster.getName() + " is defeated!");
            myBattleActive = false;
            return;
        }

        nextTurn();
    }

    // -----------------------------------------------------
    // MONSTER TURN LOGIC
    // -----------------------------------------------------
    private static void monsterTurn() {

        if (!myBattleActive) {
            return;
        }

        if (myMonsterAttacksRemaining > 0) {

            int damage = myMonster.attack(myHero);

            if (damage == -1) {
                log(myMonster.getName() + " MISSES!");
            } 
            else if (myHero.defend()) {
                log(myHero.getName() + " BLOCKS the attack!");
            }
            else {
                myHero.setHitPoints(myHero.getHitPoints() - damage);
                log(myMonster.getName() + " hits "
                        + myHero.getName() + " for " + damage + " damage!");
            }

            myMonsterAttacksRemaining--;
            myView.showHeroStats(myHero);

            if (!myHero.isAlive()) {
                log(myHero.getName() + " has fallen...");
                myBattleActive = false;
                return;
            }

            nextTurn();
            return;
        }

        // Reset attacks for next round
        myHeroAttacksRemaining = Math.max(1, myHero.getAttackSpeed());
        myMonsterAttacksRemaining = Math.max(1, myMonster.getAttackSpeed());

        nextTurn();
    }


    // -----------------------------------------------------
    // MULTIPLE MONSTERS (ENCOUNTER LOGIC)
    // -----------------------------------------------------
    public static String battleMultiple(final Hero theHero,
                                        final List<Monster> theMonsters,
                                        final GameView theView) {

        log("A group of monsters appears!");

        for (Monster monster : theMonsters) {

            if (monster == null || !monster.isAlive()) {
                continue;
            }

            log("\n=== Combat Start: " + monster.getName() + " ===");

            // Start a full turn-based battle
            startBattle(theHero, monster, theView);

            // Wait until this battle ends
            while (myBattleActive) {
                // GUI calls nextTurn() + heroAction()
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) { }
            }

            if (!theHero.isAlive()) {
                return "HERO_LOSE";
            }
        }

        return "HERO_WIN";
    }
}
