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
public class WeaponTest extends TestCase
{
    private Weapon meep;
    private Player player;
    private Weapon bleep;
    /**
     * 
     */
    public void setUp() {
        meep = new Weapon("Pointy",45, .9);
        player = new Player(50, meep);
        bleep = new Weapon("bonk", 85, .5);
        
    }
    /**
     * 
     */
    public void testConstructor() {
        assertEquals("Pointy",meep.getName());
        assertEquals(45,meep.getAttackValue());
        assertEquals(0.9,meep.getAccuracy(), 0.01);
       
    }
    /**
     * 
     */
    public void testUse() {
        assertEquals(player.getEquippedWeapon(), meep);
        bleep.use(player);
        assertEquals(player.getEquippedWeapon(), bleep);
    }
  
}
