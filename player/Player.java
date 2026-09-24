package player;

/**
 * // -------------------------------------------------------------------------
/**
 *  The player class is used to monitor and adjust the player status meaning 
 *  its health, and poison status.
 * 
 *  @author Benjamin Maloney
 *  @version Sep 17, 2026
 */
public class Player
{
    private int hp;
    private Weapon equippedWeapon;
    private int encountersCleared;
    private boolean poisoned;
    private double accuracyModifier;
    /**
     * Player constructor used to initialize the field in Player.
     * 
     * @param hp The amount of health points the player has.
     * @param equippedWeapon used to hold the value of the Weapon object 
     * equipped by the player.
     */
    public Player(int hp,Weapon equippedWeapon) {
        this.hp = hp;
        this.equippedWeapon = equippedWeapon;
        this.encountersCleared = 0;
        this.poisoned = false;
        this.accuracyModifier = 1.0;
    }
    /**
     * Updates the player's Hp by a specified amount.
     * 
     */
    public void takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        hp = Math.max(0, hp-amount);
    }
    /**
     * Gets the player's poison status.
     * 
     * @return poison status;
     */
    public boolean isPoisoned() {
        return poisoned;
    }
    /**
     * Gets the player's Hp.
     * 
     * @return the player's Hp.
     */
    public int getHp() {
        return hp;
    }
    /**
     * Understand if the player is alive or not.
     * 
     * @return true if the player is alive otherwise false.
     */
    public boolean isAlive() {
        return hp>0;
    }
    /**
     * Updates the Weapon equipped by the player.
     * 
     */
    public void equip(Weapon w) {
       equippedWeapon = w;
    }
    /**
     * Updates the player's health.
     */
    public void heal(int amount) {
        if (amount<0) {
            throw new IllegalArgumentException();
        }
        hp+=amount;
    }
    /**
     * Get the number of completed encounters.
     * 
     * @return the number of encounters cleared.
     */
    public int getEncountersCleared() {
        return encountersCleared;
    }
    /**
     * Get the Weapon equipped.
     * 
     * @return the weapon equipped by the player.
     */
    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }
    /**
     * Updates the int value encountersCleared by +1.
     */
    public void encounterCleared() {
        encountersCleared ++;
    }
    /**
     * Updates the poison status of the player.
     */
    public void setPoisoned(boolean poisoned) {
        this.poisoned = poisoned;
    }

    // Added by Ajeet so Witch.useAbility() (already calling lowerAccuracy())
    // has somewhere to store the curse effect for Combat to read.
    /**
     * Applies a curse that halves the player's accuracy until it is cleared.
     */
    public void lowerAccuracy() {
        accuracyModifier = 0.5;
    }

    /**
     * Gets the current accuracy multiplier caused by curses.
     *
     * @return 1.0 normally, or less while cursed.
     */
    public double getAccuracyModifier() {
        return accuracyModifier;
    }

    /**
     * Resets the accuracy multiplier back to normal after it has been used.
     */
    public void clearAccuracyModifier() {
        accuracyModifier = 1.0;
    }

}
