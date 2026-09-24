import student.TestCase;

/**
 * Tests for Skeleton's reassemble ability.
 *
 * @author Ruhaan Singh
 * @version Sep 23, 2026
 */
public class SkeletonTest extends TestCase
{
    private Skeleton skeleton;

    /**
     * Sets up a fresh skeleton before each test.
     */
    public void setUp() {
        skeleton = new Skeleton("Skeleton", 10, 5, 0.7);
    }

    /**
     * Normal case: the skeleton revives at 1 HP after hitting zero.
     */
    public void testUseAbilityRevivesAtZeroHp() {
        skeleton.takeDamage(10);
        skeleton.useAbility(null);
        assertEquals(1, skeleton.getHp());
        assertTrue(skeleton.getHasReassembled());
    }

    /**
     * The skeleton cannot reassemble a second time.
     */
    public void testUseAbilityDoesNotReviveTwice() {
        skeleton.takeDamage(10);
        skeleton.useAbility(null);
        skeleton.takeDamage(1);
        skeleton.useAbility(null);
        assertEquals(0, skeleton.getHp());
    }
}
