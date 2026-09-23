package player;
/**
 * // -------------------------------------------------------------------------
/**
 *  The weapon class is an item that when used will be equipped and later used 
 *  to attack player enemies.
 * 
 *  @author Benjamin Maloney
 *  @version Sep 20, 2026
 */
public class Weapon
    extends Item
{
    private int attackValue;
    private double accuracy;
    /**
     * Weapon constructor used to initialize the field and call on the 
     * super class.
     * 
     * @param name The string value for the name of the weapon.
     * @param attackValue The amount of damage done if an attack hits.
     * @param accuracy How likely an attack is to land.
     */
    public Weapon(String name, int attackValue, double accuracy) {
        super(name);
        this.attackValue = attackValue;
        this.accuracy = accuracy;
    }
    /**
     * A get method for the attackValue.
     * 
     * @return attackValue.
     */
    public int getAttackValue() {
        return attackValue;
    }
    /**
     * A get method for accuracy.
     * 
     * @return accuracy.
     */
    public double getAccuracy() {
        return accuracy;
    }
    /**
     * enables the weapon to use the weapon.
     */
    public void use(Player player) {
        player.equip(this);
    }
}
