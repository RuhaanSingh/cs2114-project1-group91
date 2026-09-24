import player.Player;
import player.Weapon;
import student.TestCase;

/**
 * Tests for Zombie's drain ability using extreme drainChance values
 * so outcomes are deterministic.
 *
 * @author Ruhaan Singh
 * @version Sep 23, 2026
 */
public class ZombieTest extends TestCase
{
    private Player player;

    /**
     * Sets up a fresh player before each test.
     */
    public void setUp() {
        player = new Player(50, new Weapon("Sword", 12, 0.65));
    }

    /**
     * A drainChance of 1.0 always drains the player and heals the zombie.
     */
    public void testUseAbilityAlwaysDrains() {
        Zombie zombie = new Zombie("Zombie", 20, 5, 0.7, 1.0);
        int startingHp = zombie.getHp();
        int playerStartingHp = player.getHp();
        zombie.useAbility(player);
        assertEquals(startingHp + 5, zombie.getHp());
        assertEquals(playerStartingHp - 5, player.getHp());
    }

    /**
     * A drainChance of 0.0 never drains the player or heals the zombie.
     */
    public void testUseAbilityNeverDrains() {
        Zombie zombie = new Zombie("Zombie", 20, 5, 0.7, 0.0);
        int startingHp = zombie.getHp();
        int playerStartingHp = player.getHp();
        zombie.useAbility(player);
        assertEquals(startingHp, zombie.getHp());
        assertEquals(playerStartingHp, player.getHp());
    }
}
