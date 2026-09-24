import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import player.Player;
import player.Weapon;
import student.TestCase;

/**
 * Tests for Game: at least one normal and one bad-input case per key
 * method.
 *
 * @author Ajeet Bondugula
 * @version Sep 23, 2026
 */
public class GameTest extends TestCase
{
    private List<Weapon> weapons;
    private List<Enemy> enemies;
    private Game game;

    /**
     * Sets up a Game with a preset player so getters and win-tracking
     * can be tested without going through the interactive prompts.
     */
    public void setUp() {
        weapons = new ArrayList<Weapon>();
        weapons.add(new Weapon("Sword", 12, 0.65));
        weapons.add(new Weapon("Dagger", 7, 0.85));

        enemies = new ArrayList<Enemy>();
        enemies.add(new Skeleton("Skeleton", 20, 5, 0.5));

        Player startingPlayer = new Player(50, weapons.get(0));
        game = new Game(weapons, enemies, 3, new Random(1), new Scanner(""), startingPlayer);
    }

    /**
     * Normal case: spawnEnemy() with a non-empty template list returns
     * a non-null Enemy.
     */
    public void testSpawnEnemyNormal() {
        Enemy spawned = game.spawnEnemy();
        assertNotNull(spawned);
    }

    /**
     * Bad-input case: spawnEnemy() with no templates to pick from
     * throws instead of returning a broken or null Enemy.
     */
    public void testSpawnEnemyNoTemplates() {
        Game emptyGame = new Game(weapons, new ArrayList<Enemy>(), 3, new Random(1),
            new Scanner(""), new Player(50, weapons.get(0)));
        Exception thrown = null;
        try {
            emptyGame.spawnEnemy();
        }
        catch (IllegalStateException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }

    /**
     * Normal case: cleanInput trims whitespace and lowercases the input.
     */
    public void testCleanInputNormal() {
        assertEquals("fight", game.cleanInput(" FIGHT \n"));
    }

    /**
     * Bad-input case: cleanInput handles null input without crashing.
     */
    public void testCleanInputNull() {
        assertEquals("", game.cleanInput(null));
    }

    /**
     * Normal case: isValidChoice returns true for an option in the list.
     */
    public void testIsValidChoiceNormal() {
        assertTrue(game.isValidChoice("fight", List.of("fight", "flee")));
    }

    /**
     * Bad-input case: isValidChoice returns false for an option that
     * doesn't exist.
     */
    public void testIsValidChoiceBad() {
        assertFalse(game.isValidChoice("idk", List.of("fight", "flee")));
    }

    /**
     * Normal case: hasWon returns true once encountersCleared reaches
     * encountersToWin.
     */
    public void testHasWonNormal() {
        Player winner = new Player(50, weapons.get(0));
        winner.encounterCleared();
        winner.encounterCleared();
        winner.encounterCleared();
        Game wonGame = new Game(weapons, enemies, 3, new Random(1), new Scanner(""), winner);
        assertTrue(wonGame.hasWon(3));
    }

    /**
     * Bad-input case: hasWon returns false when no encounters have been
     * cleared yet.
     */
    public void testHasWonBad() {
        assertEquals(0, game.getEncountersCleared());
        assertFalse(game.hasWon(3));
    }

    /**
     * Normal case: getHp reflects the preset player's current HP.
     */
    public void testGetHp() {
        assertEquals(50, game.getHp());
    }

    /**
     * Normal case: getEquippedWeapon reflects the preset player's weapon.
     */
    public void testGetEquippedWeapon() {
        assertEquals(weapons.get(0), game.getEquippedWeapon());
    }
    /** Each spawn has independent HP and an unused reassembly ability. */
    public void testSpawnEnemyIsFresh() {
        Enemy first = game.spawnEnemy();
        first.takeDamage(20);
        first.useAbility(null);
        Enemy second = game.spawnEnemy();
        assertNotSame(first, second);
        assertEquals(20, second.getHp());
        assertFalse(((Skeleton)second).getHasReassembled());
        assertEquals(20, enemies.get(0).getHp());
    }

    /** Witch and zombie copies preserve their configured ability chances. */
    public void testSpawnAbilityEnemies() {
        enemies.clear();
        enemies.add(new Witch("Witch", 30, 8, 0.6, 0.3));
        Witch witch = (Witch)game.spawnEnemy();
        assertNotSame(enemies.get(0), witch);
        assertEquals(0.3, witch.getCurseChance(), 0.001);
        enemies.clear();
        enemies.add(new Zombie("Zombie", 40, 10, 0.5, 0.4));
        Zombie zombie = (Zombie)game.spawnEnemy();
        assertNotSame(enemies.get(0), zombie);
        assertEquals(40, zombie.getHp());
        assertEquals(0.4, zombie.getDrainChance(), 0.001);
    }

    /** A failed flee stays in combat, and fatal damage reports game over. */
    public void testFailedFleeStaysInEncounter() {
        enemies.clear();
        enemies.add(new Zombie("Zombie", 40, 10, 0.0, 0.0));
        Random rolls = new Random() {
            public double nextDouble() {
                return 0.99;
            }
        };
        Game fleeingGame = new Game(weapons, enemies, 3, rolls,
            new Scanner("1\nflee\nflee\n"), new Player(20, weapons.get(0)));
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        java.io.PrintStream original = System.out;
        try {
            System.setOut(new java.io.PrintStream(output));
            fleeingGame.run();
        }
        finally {
            System.setOut(original);
        }
        assertEquals(0, fleeingGame.getHp());
        assertEquals(0, fleeingGame.getEncountersCleared());
        assertTrue(output.toString().contains("Your HP: 20 | Zombie HP: 40"));
        assertTrue(output.toString().contains("Your HP: 0 | Zombie HP: 40"));
        assertTrue(output.toString().contains("You have died. Game over."));
    }
}
