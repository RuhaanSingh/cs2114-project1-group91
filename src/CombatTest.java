import java.util.Random;

import player.Player;
import player.Weapon;
import student.TestCase;

/**
 * Tests for Combat: at least one normal and one bad-input case per
 * key method, using a seeded/fixed Random so outcomes are deterministic.
 *
 * @author Angelina Gust
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

    /** Checks that escape state reflects success and resets for each encounter. */
    public void testEscapeState() {
        Combat combat = new Combat(new FixedRandom(0.5));
        combat.startEncounter(player, enemy);
        combat.setFleeChance(0.0);
        combat.resolveTurn("flee");
        assertFalse(combat.hasEscaped());
        combat.setFleeChance(1.0);
        combat.resolveTurn("flee");
        assertTrue(combat.hasEscaped());
        combat.startEncounter(player, enemy);
        assertFalse(combat.hasEscaped());
    }

    /** A dead zombie cannot drain the player or revive itself. */
    public void testDeadZombieDoesNotDrain() {
        Combat combat = new Combat(new FixedRandom(0.0));
        Enemy zombie = new Zombie("Zombie", 10, 5, 1.0, 1.0);
        combat.startEncounter(player, zombie);
        assertTrue(combat.resolveTurn("fight").contains("falls"));
        assertEquals(0, zombie.getHp());
        assertEquals(50, player.getHp());
    }

    /** A successful drain replaces normal damage rather than doubling it. */
    public void testZombieDrainDealsDamageOnce() {
        Combat combat = new Combat(new FixedRandom(0.0));
        Enemy zombie = new Zombie("Zombie", 30, 5, 1.0, 1.0);
        combat.startEncounter(player, zombie);
        assertTrue(combat.resolveTurn("fight").contains("drains"));
        assertEquals(25, zombie.getHp());
        assertEquals(45, player.getHp());
    }

    /** A missed attack cannot curse the player. */
    public void testWitchMissDoesNotCurse() {
        Combat combat = new Combat(new FixedRandom(0.0));
        combat.startEncounter(player, new Witch("Witch", 30, 5, 0.0, 1.0));
        combat.resolveTurn("fight");
        assertEquals(1.0, player.getAccuracyModifier(), 0.001);
        assertEquals(50, player.getHp());
    }

    /** Curses are reported and expire after the next attack. */
    public void testCurseLastsOneAttack() {
        Combat combat = new Combat(new FixedRandom(0.5));
        Enemy witch = new Witch("Witch", 30, 5, 1.0, 1.0);
        combat.startEncounter(player, witch);
        assertTrue(combat.resolveTurn("fight").contains("cursed"));
        assertEquals(0.5, player.getAccuracyModifier(), 0.001);
        witch.takeDamage(20);
        assertTrue(combat.resolveTurn("fight").contains("miss"));
        assertEquals(1.0, player.getAccuracyModifier(), 0.001);
    }

    /** Reassembly is reported as survival and can happen only once. */
    public void testSkeletonReassemblesOnceInCombat() {
        Combat combat = new Combat(new FixedRandom(0.0));
        Enemy skeleton = new Skeleton("Skeleton", 10, 5, 0.0);
        combat.startEncounter(player, skeleton);
        String first = combat.resolveTurn("fight");
        assertTrue(first.contains("reassembles"));
        assertFalse(first.contains("falls"));
        assertEquals(1, skeleton.getHp());
        assertTrue(combat.resolveTurn("fight").contains("falls"));
        assertEquals(0, skeleton.getHp());
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
