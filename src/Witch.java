// Virginia Tech Honor Code Pledge:
//
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I accept the actions of those who
// do.
// Ruhaan Singh (906857157)
// LLM Statement:
// I have not used any assistance for the assignment beyond course resources and
// staff.

import player.Player;

/**
 * A Witch enemy. Has a chance to curse the player, lowering their
 * accuracy for a turn.
 */
public class Witch extends Enemy {

    private double curseChance;

    public Witch(String type, int hp, int attackValue, double accuracy, double curseChance) {
        super(type, hp, attackValue, accuracy);
        this.curseChance = curseChance;
    }

    /**
     * Gets the chance that this witch's curse ability succeeds.
     *
     * @return the curse chance as a value between 0 and 1
     */
    public double getCurseChance() {
        return curseChance;
    }

    /**
     * Rolls against curseChance, and if it succeeds, lowers the target
     * player's accuracy for their next turn.
     *
     * @param target the player this witch is fighting
     */
    public void useAbility(Player target) {
        double roll = Math.random();
        if (roll < curseChance) {
            target.lowerAccuracy();
        }
    }
}