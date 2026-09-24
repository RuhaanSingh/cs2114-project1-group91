import player.Player;

/**
 * A Zombie enemy. Has a chance to drain HP from the player when it
 * attacks, healing itself by the same amount.
 */
public class Zombie extends Enemy {

    private double drainChance;

    public Zombie(String type, int hp, int attackValue, double accuracy, double drainChance) {
        super(type, hp, attackValue, accuracy);
        this.drainChance = drainChance;
    }

    /**
     * Gets the chance that this zombie's drain ability succeeds.
     *
     * @return the drain chance as a value between 0 and 1
     */
    public double getDrainChance() {
        return drainChance;
    }

    /**
     * Rolls against drainChance, and if it succeeds, drains HP from the
     * target player and adds it to this zombie's own HP.
     *
     * @param target the player this zombie is fighting
     */
    public void useAbility(Player target) {
        double roll = Math.random();
        if (roll < drainChance) {
            int drained = getAttackValue();
            target.takeDamage(drained);
            heal(drained);
        }
    }
}