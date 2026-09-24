import java.util.Random;
import player.Player;
import player.Weapon;

/**
 * The Combat class simulates one fight between the player and the enemy.
 * This class resolves fight/flee, applies damage, reports results, and 
 * handles the random hit/flee rolls directly using the Random attribute. 
 * 
 * @author Angelina Gust (angelinagust)
 * @version 2026.09.21
 **/
public class Combat {

    private Player player;
    private Enemy enemy;
    private Random rng;
    private double fleeChance = 1.0 / 25.0; 
    private boolean isPlayerCursed = false;

    /**
     * Constructs the Combat method.
     * 
     * @param player the Player participating in the combat
     * @param enemy the Enemy that the player is fighting 
     * @param rng the Random generator that is used for accuracy and flee commands.
     */
    public Combat(Player player, Enemy enemy, Random rng) {
        this.player = player;
        this.enemy = enemy; 
        this.rng = rng;
    }

    /**
     * Executes the correct method based on what the user has input.
     * If input is not "flee" or "fight", the appropriate output is returned. 
     * 
     * @param choice the String that the user has input when prompted
     * @return the String that is returned based on what the user has input
     */
    public String resolveTurn(String choice) {
        if (choice == null || (!choice.equalsIgnoreCase("fight") && 
        !choice.equalsIgnoreCase("flee"))) {
            return "Invalid choice. Please respond with either 'fight' or 'flee'";
        }

        if (choice.equalsIgnoreCase("fight")) {
            return executeFight();
        } 
        
        else {
            return executeFlee();
        }
    }

    /**
     * Executes the action for fight when it is the players turn during combat.
     * Calculates the accuracy and attack damage of the specified weapon, the 
     * damage that the enemy exerts during its turn of combat, the damage that
     * the enemy receives from combat, and the damage the player recieves from
     * being poisoned by the enemy. 
     * 
     * @return a String describing what has occurred during the specified round on combat
     */
    public String executeFight() {
        StringBuilder result = new StringBuilder();
        Weapon weapon = player.getEquippedWeapon();

        double accuracy = weapon.getAccuracy();
        if (isPlayerCursed) {
            accuracy -= 0.2;
            isPlayerCursed = false;
            result.append("Your attack accuracy had been decreased because you were cursed!");
        }

        if (rng.nextDouble() < accuracy) {
            int damage = weapon.getAttackValue();
            enemy.takeDamage(damage);
            result.append("You hit the ").append(enemy.getType())
            .append(" with the ").append(weapon.getName())
            .append(" for ").append(damage).append(" damage.");
        }

        else {
            result.append("You attempted to use your ")
            .append(weapon.getName()).append(" on the ")
            .append(enemy.getType()).append(" but missed!");
        }

        if (enemy.getHP() <= 0) {

            if (enemy instanceof Skeleton) {
                enemy.useAbility(player);
            }

            if (enemy.getHP() <= 0) {
                player.encounterCleared();
                result.append(" You defeated the ")
                .append(enemy.getType()).append("!");
                return result.toString();
            }

            else {
                result.append(" The Skeleton reassembled itself and was revived with 1HP!");
            }
        }

        result.append("\n").append(enemyAttack());

        if (player.isAlive() && player.isPoisoned()) {
            player.takeDamage(2);
            result.append(" You took 2 damage from the poison!");
        }

        return result.toString();
    }

    /**
     * Executes the action for flee when the player comes across and enemy.
     * Checks if the flee attempt was successful or not based on fleeChance. 
     * If the flee attempt was unsuccessful, the enemyAttack is prompted on the player. 
     * 
     * @return a String describing whether the players attempt at an escape was a failure or was successful
     */
    public String executeFlee() {
        StringBuilder result = new StringBuilder();

        if (rng.nextDouble() < fleeChance) {
            result.append("You have successfully fled from the ")
            .append(enemy.getType()).append("!");
        }

        else {
            result.append("You've failed to flee! The ")
            .append(enemy.getType()).append(" has stopped your escape!\n")
            .append(enemyAttack());
        }

        return result.toString();
    }

    /**
     * Executes the enemy's attack on the player during combat.
     * Depending on what the enemy is (Zombie, Witch, or Skeleton), its respective 
     * methods are ran, along with the effects each one has on the Player. 
     * 
     * @return a String that provides the outcome of the enemyAttack method
     */
    public String enemyAttack() {
        StringBuilder result = new StringBuilder();

        if (rng.nextDouble() < enemy.getAccuracy()) {
            if (enemy instanceof Zombie) {
                int previousHP = enemy.getHP();
                enemy.useAbility(player);
                int drainedHP = enemy.getHP() - previousHP;

                if (drainedHP > 0) {
                    result.append("The Zombie drained ")
                    .append(drainedHP).append(" of your HP, using it to heal itself.");
                }

                else {
                    player.takeDamage(enemy.getAttackValue());
                    result.append("The Zombie hit you for ")
                    .append(enemy.getAttackValue()).append(" damage!");
                }
            }

            else if (enemy instanceof Witch) {
                enemy.useAbility(player);
                this.isPlayerCursed = true;
                result.append("The Witch has cast a curse on you! You now have lowered accuracy for your next turn.");
            }

            else {
                player.takeDamage(enemy.getAttackValue());
                result.append("The ").append(enemy.getType())
                .append(" attacked you for ")
                .append(enemy.getAttackValue()).append(" damage!");
            }
        }

        else {
            result.append("The ").append(enemy.getType())
            .append(" attacked you but missed!");
        }

        return result.toString();
    }

}