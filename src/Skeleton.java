/**
 * A Skeleton enemy. Has a chance to come back to life once after
 * hitting 0 HP, instead of dying right away.
 */
public class Skeleton extends Enemy {

    private boolean hasReassembled;

    public Skeleton(String type, int hp, int attackValue, double accuracy) {
        super(type, hp, attackValue, accuracy);
        hasReassembled = false;
    }

    /**
     * Checks whether this skeleton has already used its ability.
     *
     * @return true if it has already reassembled once, false otherwise
     */
    public boolean getHasReassembled() {
        return hasReassembled;
    }

    /**
     * If this skeleton's HP has hit 0 and it hasn't reassembled yet, it
     * comes back with 1 HP and can't do this again.
     *
     * @param target the player this skeleton is fighting 
     */
    public void useAbility(Player target) {
        if (getHp() == 0 && hasReassembled == false) {
            heal(1);
            hasReassembled = true;
        }
    }
}