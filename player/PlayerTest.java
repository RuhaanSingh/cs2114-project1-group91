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
public class PlayerTest extends TestCase
{
    private Player player;
    private Weapon blade;
    private Weapon hoe;
    private Potion splash;
    private Potion instant;
    
    /**
     * 
     */
    public void setUp() {
        hoe = new Weapon("Diamond Hoe", 100, .12);
        blade = new Weapon("Flint & Steel", 25, 1);
        player = new Player(50,blade);
        splash = new Potion("Fire Resistance");
        instant = new Potion("Instant Healing", 50);
    }
    /**
     * 
     */
    public void testConstructor() {
        assertEquals(50, player.getHp());
        assertEquals(0, player.getEncountersCleared());
        assertEquals(blade, player.getEquippedWeapon());
        assertFalse(player.isPoisoned());
        assertTrue(player.isAlive());
    }
    /**
     * 
     */
    public void testTakeDamage() {
        player.takeDamage(10);
        assertEquals(40, player.getHp());
        player.takeDamage(50);
        assertEquals(0, player.getHp());
        
        Exception thrown = null;
        try {
            player.takeDamage(-10);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }
    /**
     * 
     */
    public void testIsAlive() {
        assertTrue(player.isAlive());
        player.takeDamage(50);
        assertFalse(player.isAlive());
    }
    /**
     * 
     */
    public void testHeal() {
        instant.use(player);
        assertEquals(100,player.getHp());
    }
    /**
     * 
     */
    public void testEquip() {
        assertEquals(blade, player.getEquippedWeapon());
        player.equip(hoe);
        assertEquals(hoe, player.getEquippedWeapon());
        
        Exception thrown = null;
        try {
            player.heal(-10);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }
    /**
     * 
     */
    public void testEncounterCleared() {
        assertEquals(0, player.getEncountersCleared());
        player.encounterCleared();
        assertEquals(1, player.getEncountersCleared());
        player.encounterCleared();
        assertEquals(2, player.getEncountersCleared());
    }
    /**
     * 
     */
    public void testPoisoned() {
        assertFalse(player.isPoisoned());
        player.setPoisoned(true);
        assertTrue(player.isPoisoned());
        splash.use(player);
        assertFalse(player.isPoisoned());
    }
}
