package player;
/**
 * // -------------------------------------------------------------------------
/**
 *  Potion is a subclass to Item which serves to either heal the player or 
 *  change the players poison status.
 * 
 *  @author Benjamin Maloney
 *  @version Sep 20, 2026
 */
public class Potion
    extends Item
{
    private int healAmount;
    /**
     * The Potion constructor used to initialize the field and the super 
     * class field.
     * 
     * @param name The name of the potion.
     */
    public Potion(String name) {
        super(name);
        this.healAmount = 0;
    }
    /**
     * The Potion constructor used to initialize the field and the super class.
     * 
     * @param name The name of the potion.
     * @param healAmount the amount healed by the potion
     */
    public Potion(String name, int healAmount) {
        super(name);
        this.healAmount = healAmount;
        
    }
    /**
     * The get method for healAmount.
     * 
     * @return healAmount.
     */
    public int getHealAmount() {
        return healAmount;
    }
    /**
     * The potion's action determined by its healAmount either healing the 
     * player or curing poison.
     */
    public void use(Player player) {
        if (healAmount == 0) {
            player.setPoisoned(false);
        }
        else {
        player.heal(healAmount);
        }
    }
    
}
