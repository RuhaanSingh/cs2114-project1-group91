package player;
/**
 * // -------------------------------------------------------------------------
/**
 *  The Item class serves as an abstract class that only serves to work as a 
 *  half baked class that is never suited for the final product as is an 
 *  abstract class.
 * 
 *  @author Benjamin Maloney
 *  @version Sep 20, 2026
 */
public abstract class Item
{
    private String name;
    /**
     * The Item constructor used to be called upon by Item subclass and 
     * initialize the field.
     */
    public Item(String name) {
        this.name = name;
    }
    /**
     * The get method for name.
     * 
     * @return name;
     */
    public String getName() {
        return name;
    }
    /**
     * never intended to return anything only a placeholder.
     */
    public abstract void use(Player player);
}
