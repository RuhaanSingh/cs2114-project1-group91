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
 * Represents an enemy in the game. Has stats that every
 * enemy type has, like health and hit percentage.
 * Specific enemy types will extend this class and add their own special ability.
 */
public abstract class Enemy {

    private String type;
    private int hp;
    private int attackValue;
    private double accuracy;

    public Enemy(String type, int hp, int attackValue, double accuracy) {
        this.type = type;
        this.hp = hp;
        this.attackValue = attackValue;
        this.accuracy = accuracy;
    }

    /**
     * Gets the current HP of this enemy
     *
     * @return the enemy's current HP
     */
    public int getHp() {
        return hp;
    }

    /**
     * Gets the attack value of this enemy.
     *
     * @return the enemy's attack value
     */
    public int getAttackValue() {
        return attackValue;
    }

    /**
     * Gets the accuracy of this enemy
     *
     * @return the enemy's accuracy as a value between 0 and 1
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * Gets the type name of this enemy.
     *
     * @return the enemy's type
     */
    public String getType() {
        return type;
    }

    /**
     * Reduces this enemy's HP by the amount specified
     *
     * @param amount the amount of damage to apply
     */
    public void takeDamage(int amount) {
        int newHp = hp - amount;
        if (newHp < 0) {
            newHp = 0;
        }
        hp = newHp;
    }

    /**
     * Adds HP to this enemy
     *
     * @param amount the amount of HP to add
     */
    public void heal(int amount) {
        hp = hp + amount;
    }

    /**
     * Uses this enemy's unique ability against the given player 
     *
     * @param target the player this enemy is fighting
     */
    public abstract void useAbility(Player target);

}