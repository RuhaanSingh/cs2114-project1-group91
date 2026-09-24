import java.util.Random;

import player.Player;

/**
 * Runs a single fight between the current Player and Enemy: resolves
 * fight/flee choices, applies damage, and reports the outcome. Uses an
 * injected Random for all hit/flee rolls so results can be reproduced
 * in tests.
 *
 * @author Angelina Gust
 * @version Sep 23, 2026
 */
public class Combat
{
    private Player player;
    private Enemy enemy;
    private final Random rng;
    private double fleeChance;
    private boolean escaped;

    /**
     * Creates a Combat that rolls hit/flee chances off the given Random.
     *
     * @param rng the source of randomness, injected so tests can control it
     */
    public Combat(Random rng) {
        this.rng = rng;
        this.fleeChance = 1.0 / 25.0;
    }

    /**
     * Loads the two combatants for the next encounter.
     *
     * @param player the player entering combat
     * @param enemy the enemy the player is facing
     */
    public void startEncounter(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.escaped = false;
        player.clearAccuracyModifier();
    }

    /**
     * Sets how likely a flee attempt is to succeed.
     *
     * @param fleeChance a value between 0 and 1
     */
    public void setFleeChance(double fleeChance) {
        this.fleeChance = fleeChance;
    }

    /**
     * Gets the enemy currently loaded into this encounter.
     *
     * @return the current enemy, or null if no encounter has started
     */
    public Enemy getEnemy() {
        return enemy;
    }

    /**
     * Resolves one turn of combat based on the player's choice.
     *
     * @param choice the player's already-cleaned input
     * @return a message describing what happened this turn
     */
    public String resolveTurn(String choice) {
        if ("fight".equals(choice)) {
            return resolveFight();
        }
        if ("flee".equals(choice)) {
            return resolveFlee();
        }
        return "That's not something you can do right now. Type 'fight' or 'flee'.";
    }

    /**
     * Reports whether the player successfully fled this encounter.
     *
     * @return true after a successful escape
     */
    public boolean hasEscaped() {
        return escaped;
    }

    private String resolveFight() {
        StringBuilder message = new StringBuilder();
        double accuracy = player.getEquippedWeapon().getAccuracy()
            * player.getAccuracyModifier();
        if (rng.nextDouble() < accuracy) {
            int damage = player.getEquippedWeapon().getAttackValue();
            enemy.takeDamage(damage);
            message.append("You hit the ").append(enemy.getType())
                .append(" for ").append(damage).append(" damage. ");
        }
        else {
            message.append("You swing and miss. ");
        }
        player.clearAccuracyModifier();

        if (enemy.getHp() <= 0) {
            if (enemy instanceof Skeleton) {
                enemy.useAbility(player);
            }
            if (enemy.getHp() <= 0) {
                message.append("The ").append(enemy.getType()).append(" falls.");
                return message.toString();
            }
            message.append("The Skeleton reassembles with 1 HP! ");
        }

        if (rng.nextDouble() < enemy.getAccuracy()) {
            int damage = enemy.getAttackValue();
            if (enemy instanceof Zombie) {
                int previousHp = enemy.getHp();
                enemy.useAbility(player);
                if (enemy.getHp() > previousHp) {
                    message.append("The Zombie drains ").append(damage)
                        .append(" HP from you and heals itself.");
                    return message.toString();
                }
            }
            player.takeDamage(damage);
            message.append("The ").append(enemy.getType())
                .append(" hits you for ").append(damage).append(" damage.");
            if (player.isAlive() && enemy instanceof Witch) {
                enemy.useAbility(player);
                if (player.getAccuracyModifier() < 1.0) {
                    message.append(" You are cursed: accuracy is halved for your next attack.");
                }
            }
        }
        else {
            message.append("The ").append(enemy.getType()).append(" misses you.");
        }
        return message.toString();
    }

    private String resolveFlee() {
        if (rng.nextDouble() < fleeChance) {
            escaped = true;
            player.clearAccuracyModifier();
            return "You slip away safely.";
        }
        int damage = enemy.getAttackValue();
        player.takeDamage(damage);
        return "You fail to escape and the " + enemy.getType()
            + " hits you for " + damage + " damage.";
    }
}
