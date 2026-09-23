package player;
import student.TestCase;

/**
 * // -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author Benjamin Maloney
 *  @version Sep 20, 2026
 */
public class PotionTest extends TestCase
{
    private Potion meep;
    private Potion bleep;
    private Player player;
    private Weapon bonk;
    public void setUp() {
        meep = new Potion("Health Potion",15);
        bleep = new Potion("Cure");
        bonk = new Weapon("Bonk",15,.5);
        player = new Player(50,bonk);
    }
    /**
     * 
     */
    public void testConstrutor() {
        assertEquals("Cure",bleep.getName());
        assertEquals(0,bleep.getHealAmount());
        
        assertEquals("Health Potion", meep.getName());
        assertEquals(15,meep.getHealAmount());
    }
    /**
     * 
     */
    public void testUse() {
        assertEquals(50,player.getHp());
        meep.use(player);
        assertEquals(65,player.getHp());
        //Poison potion
        player.setPoisoned(true);
        assertTrue(player.isPoisoned());
        bleep.use(player);
        assertFalse(player.isPoisoned());
    }
}
