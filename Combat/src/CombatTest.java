import org.junit.Test;
import java.util.Random;
import player.Player;
import player.Weapon;

/**
 * This is the test class for the Combat class.
 * 
 * @author Angelina Gust (angelinagust)
 * @version 2026.09.21
 **/
public class CombatTest extends student.TestCase {
    private Player player;
    private Weapon sword;
    
    /**
     * Sets up essential fixtures that are needed for the tests to run correctly,
     * such as initalizing the Weapon and Player objects. 
     */
    public void setUp() {
        sword = new Weapon("sword", 25, 1.0);
        player = new Player(100, sword);
    }

    /**
     * Tests the resolveTurn method if an invalid statement was input.
     */
    @Test
    public void testResolveTurnInvalidInput() {
        Enemy enemy = new Skeleton("Skeleton", 40, 10, 0.5);
        Combat combat = new Combat(player, enemy, new Random(1));

        String invalid = combat.resolveTurn("defend");
        String expected = "Invalid choice. Please respond with either 'fight' or 'flee'";
        assertEquals(expected, invalid);

        invalid = combat.resolveTurn(null);
        assertEquals(expected, invalid);
    }

    /**
     * Tests the executeFight method assuming you successfully defeated the enemy.
     */
    @Test
    public void testExecuteFightSuccess() {
        Enemy enemy = new Zombie("Zombie", 10, 10, 0.5, 0.2);
        Combat combat = new Combat(player, enemy, new Random(1));
        String result = combat.resolveTurn("fight");

        assertTrue(result.contains("You defeated the Zombie!"));
        assertEquals(0, enemy.getHP());
        assertEquals(1, player.getEncountersCleared());
    }

    /**
     * Tests the executeFight class with one round of combat.
     */
    @Test
    public void testExecuteFight() {
        Enemy enemy = new Zombie("Zombie", 40, 10, 0.5, 0.2);
        Combat combat = new Combat(player, enemy, new Random(1));
        String result = combat.resolveTurn("fight");

        assertEquals(15, enemy.getHP());
        assertEquals(90, player.getHP());
        assertTrue(result.contains("You hit the Zombie with the sword for 25 damage."));
    }

    /**
     * Tests the reassembly of the Skeleton during the executeFight method.
     */
    @Test
    public void testSkeletonRevive() {
        Enemy enemy = new Skeleton("Skeleton", 15, 10, 0.5);
        Combat combat = new Combat(player, enemy, new Random(1));
        String result = combat.resolveTurn("fight");

        assertTrue(result.contains("The Skeleton reassembled itself and was revived with 1HP!"));
        assertEquals(1, enemy.getHP());
        assertEquals(0, player.getEncountersCleared());
    }

    /**
     * Tests that the poison provides correct damage during the executeFight method.
     */
    @Test
    public void testPoisonDamage() {
        Enemy enemy = new Witch("Witch", 50, 0, 0.0, 0.0);
        player.setPoisoned(true);
        Combat combat = new Combat(player, enemy, new Random(1));
        String result = combat.resolveTurn("fight");

        assertTrue(result.contains("You took 2 damage from the poison!"));
        assertEquals(98, player.getHP()); 
    }

    /**
     * Tests the executeFlee method assuming you successfully fled from the enemy. 
     */
    @Test
    public void testExecuteFleeSuccess() {
        Enemy enemy = new Skeleton("Skeleton", 40, 10, 0.5);
        Combat combat = new Combat(player, enemy, new Random(1000));

        String result = combat.resolveTurn("flee");
        String expected = "You have successfully fled from the Skeleton!";
        assertEquals(expected, result);
    }

    /**
     * Tests the executeFlee method assuming you failed to flee from the enemy. 
     */
    @Test
    public void testExecuteFleeFail() {
        Enemy enemy = new Zombie("Zombie", 40, 10, 0.5, 0.2);
        Combat combat = new Combat(player, enemy, new Random(1));

        String result = combat.resolveTurn("flee");
        String expected = "You've failed to flee! The Zombie has stopped your escape!\n" +
        "The Zombie hit you for 10 damage!";

        assertEquals(expected, result);
        assertEquals(90, player.getHP());
    }
}