import student.TestCase;

/**
 * Tests for the shared Enemy behavior. Uses Skeleton as a concrete class
 * since Enemy itself is abstract.
 *
 * @author Ruhaan Singh
 * @version Sep 23, 2026
 */
public class EnemyTest extends TestCase
{
    private Skeleton skeleton;

    /**
     * Sets up a fresh skeleton before each test.
     */
    public void setUp() {
        skeleton = new Skeleton("Skeleton", 30, 5, 0.8);
    }

    /**
     * Normal case: taking damage reduces HP by that amount.
     */
    public void testTakeDamageNormal() {
        skeleton.takeDamage(10);
        assertEquals(20, skeleton.getHp());
    }

    /**
     * Boundary case: damage greater than current HP floors HP at zero.
     */
    public void testTakeDamageMoreThanCurrentHp() {
        skeleton.takeDamage(50);
        assertEquals(0, skeleton.getHp());
    }

    /**
     * Normal case: healing increases HP by that amount.
     */
    public void testHealNormal() {
        skeleton.heal(5);
        assertEquals(35, skeleton.getHp());
    }

    /**
     * Boundary case: healing by zero leaves HP unchanged.
     */
    public void testHealZero() {
        skeleton.heal(0);
        assertEquals(30, skeleton.getHp());
    }

    /**
     * Normal case: getters return the values set in the constructor.
     */
    public void testGetters() {
        assertEquals("Skeleton", skeleton.getType());
        assertEquals(30, skeleton.getHp());
        assertEquals(5, skeleton.getAttackValue());
        assertEquals(0.8, skeleton.getAccuracy(), 0.001);
    }
}
