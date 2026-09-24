import player.Player;
import player.Weapon;
import student.TestCase;

/**
 * Tests for Witch's curse ability using extreme curseChance values
 * so outcomes are deterministic.
 *
 * @author Ruhaan Singh
 * @version Sep 23, 2026
 */
public class WitchTest extends TestCase
{
    private Player player;

    /**
     * Sets up a fresh player before each test.
     */
    public void setUp() {
        player = new Player(50, new Weapon("Sword", 12, 0.65));
    }

    /**
     * A curseChance of 1.0 always lowers the player's accuracy.
     */
    public void testUseAbilityAlwaysCurses() {
        Witch witch = new Witch("Witch", 20, 5, 0.7, 1.0);
        witch.useAbility(player);
        assertEquals(0.5, player.getAccuracyModifier(), 0.001);
    }

    /**
     * A curseChance of 0.0 never lowers the player's accuracy.
     */
    public void testUseAbilityNeverCurses() {
        Witch witch = new Witch("Witch", 20, 5, 0.7, 0.0);
        witch.useAbility(player);
        assertEquals(1.0, player.getAccuracyModifier(), 0.001);
    }
}
