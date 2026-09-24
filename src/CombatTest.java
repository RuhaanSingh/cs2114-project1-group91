import java.util.Random;

import player.Player;
import player.Weapon;
import student.TestCase;

/**
 * Tests for Combat: at least one normal and one bad-input case per
 * key method, using a seeded/fixed Random so outcomes are deterministic.
 *
 * @author Ajeet Bondugula
 * @version Sep 23, 2026
 */
public class CombatTest extends TestCase
{
    private Player player;
    private Enemy enemy;

    /**
     * Sets up a fresh player and enemy before each test.
     */
    public void setUp() {
        player = new Player(50, new Weapon("Sword", 10, 0.65));
        enemy = new Skeleton("Skeleton", 20, 5, 0.5);
    }

    /**
     * Normal case: a roll below the player's accuracy lands a hit and
     * damages the enemy.
     */
    public void testResolveTurnFightHits() {
        Combat combat = new Combat(new FixedRandom(0.0));
        combat.startEncounter(player, enemy);
        String message = combat.resolveTurn("fight");
        assertEquals(10, enemy.getHp());
        assertTrue(message.contains("hit"));
    }

    /**
     * Bad-input case: an unrecognized choice does not change any state
     * and instead returns an invalid-input message so the caller can
     * re-prompt.
     */
    public void testResolveTurnInvalidChoice() {
        Combat combat = new Combat(new FixedRandom(0.0));
        combat.startEncounter(player, enemy);
        String message = combat.resolveTurn("defend");
        assertEquals(20, enemy.getHp());
        assertEquals(50, player.getHp());
        assertTrue(message.toLowerCase().contains("fight") || message.toLowerCase()
            .contains("flee"));
    }

    /**
     * Normal case: a flee roll below fleeChance succeeds and the player
     * takes no damage.
     */
    public void testResolveTurnFleeSucceeds() {
        Combat combat = new Combat(new FixedRandom(0.0));
        combat.setFleeChance(1.0 / 25.0);
        combat.startEncounter(player, enemy);
        String message = combat.resolveTurn("flee");
        assertEquals(50, player.getHp());
        assertTrue(message.contains("slip away"));
    }

    /**
     * Bad-input case: a flee roll above fleeChance fails, and the enemy
     * hits the player for its full attack value on the way out.
     */
    public void testResolveTurnFleeFails() {
        Combat combat = new Combat(new FixedRandom(0.99));
        combat.setFleeChance(1.0 / 25.0);
        combat.startEncounter(player, enemy);
        String message = combat.resolveTurn("flee");
        assertEquals(45, player.getHp());
        assertTrue(message.contains("fail to escape"));
    }

    /**
     * A Random stand-in that always returns the same value from
     * nextDouble(), so hit/miss and flee rolls are fully deterministic.
     */
    private static class FixedRandom extends Random
    {
        private final double value;

        FixedRandom(double value) {
            this.value = value;
        }

        public double nextDouble() {
            return value;
        }
    }
}
