import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import player.Player;
import player.Weapon;

/**
 * Main driver for the game: handles door selection, spawns enemies,
 * hands off to Combat, tracks win/loss, runs the input loop, and
 * cleans/validates raw input before using it.
 *
 * @author Ajeet Bondugula
 * @version Sep 23, 2026
 */
public class Game
{
    private static final int STARTING_HP = 100;
    private static final int DOOR_COUNT = 3;

    private Player player;
    private final List<Enemy> enemyTemplates;
    private final List<Weapon> availableWeapons;
    private final int encountersToWin;
    private final Combat combat;
    private final Random random;
    private final Scanner input;

    /**
     * Creates a Game with the given content and sources of input/randomness,
     * so tests can fully control both.
     *
     * @param availableWeapons the starting weapon choices, at least two
     * @param enemyTemplates the enemy templates spawnEnemy() picks from
     * @param encountersToWin how many encounters the player must clear to win
     * @param random the source of randomness used to spawn enemies
     * @param input the source of player input
     */
    public Game(List<Weapon> availableWeapons, List<Enemy> enemyTemplates,
        int encountersToWin, Random random, Scanner input) {
        this.availableWeapons = availableWeapons;
        this.enemyTemplates = enemyTemplates;
        this.encountersToWin = encountersToWin;
        this.random = random;
        this.input = input;
        this.combat = new Combat(random);
    }

    /**
     * Creates a Game with a starting player already in place, so tests
     * can exercise HP/win-tracking methods without going through the
     * interactive weapon prompt.
     *
     * @param availableWeapons the starting weapon choices, at least two
     * @param enemyTemplates the enemy templates spawnEnemy() picks from
     * @param encountersToWin how many encounters the player must clear to win
     * @param random the source of randomness used to spawn enemies
     * @param input the source of player input
     * @param startingPlayer the player to use instead of prompting for one
     */
    public Game(List<Weapon> availableWeapons, List<Enemy> enemyTemplates,
        int encountersToWin, Random random, Scanner input, Player startingPlayer) {
        this(availableWeapons, enemyTemplates, encountersToWin, random, input);
        this.player = startingPlayer;
    }

    /**
     * Creates a Game with the default roster of weapons and enemies,
     * reading from the console.
     */
    public Game() {
        this(defaultWeapons(), defaultEnemies(), 3, new Random(), new Scanner(System.in));
    }

    private static List<Weapon> defaultWeapons() {
        List<Weapon> weapons = new ArrayList<Weapon>();
        weapons.add(new Weapon("Sword", 12, 0.65));
        weapons.add(new Weapon("Dagger", 7, 0.85));
        return weapons;
    }

    private static List<Enemy> defaultEnemies() {
        List<Enemy> enemies = new ArrayList<Enemy>();
        enemies.add(new Witch("Witch", 30, 8, 0.6, 0.3));
        enemies.add(new Zombie("Zombie", 40, 10, 0.5, 0.4));
        enemies.add(new Skeleton("Skeleton", 25, 9, 0.55));
        return enemies;
    }

    /**
     * Runs the main game loop: pick a starting weapon, then repeatedly
     * choose a door, fight or flee the enemy behind it, until the player
     * wins or dies.
     */
    public void run() {
        if (player == null) {
            player = new Player(STARTING_HP, chooseWeapon());
        }
        System.out.println("You enter the tower with your " + player.getEquippedWeapon()
            .getName() + ". Clear " + encountersToWin + " encounters to escape.");

        while (player.isAlive() && !hasWon(encountersToWin)) {
            chooseDoor();
            Enemy enemy = spawnEnemy();
            combat.startEncounter(player, enemy);
            System.out.println("A " + enemy.getType() + " blocks your path!");

            boolean encounterOver = false;
            while (!encounterOver) {
                System.out.println("Type 'fight' or 'flee':");
                String cleaned = cleanInput(input.nextLine());
                String outcome = combat.resolveTurn(cleaned);
                System.out.println(outcome);

                if ("flee".equals(cleaned)) {
                    encounterOver = true;
                }
                else if (enemy.getHp() <= 0) {
                    player.encounterCleared();
                    System.out.println("Encounters cleared: " + player.getEncountersCleared()
                        + "/" + encountersToWin);
                    encounterOver = true;
                }
                else if (!player.isAlive()) {
                    System.out.println("You have died. Game over.");
                    encounterOver = true;
                }
            }
        }

        if (hasWon(encountersToWin)) {
            System.out.println("You escaped the tower! You win!");
        }
    }

    /**
     * Prompts the player to choose one of DOOR_COUNT doors, re-prompting
     * on invalid input. The doors are cosmetic; every door leads to an
     * encounter.
     */
    private void chooseDoor() {
        List<String> doorOptions = new ArrayList<String>();
        for (int i = 1; i <= DOOR_COUNT; i++) {
            doorOptions.add(Integer.toString(i));
        }
        System.out.println("Choose a door (1-" + DOOR_COUNT + "):");
        String choice = cleanInput(input.nextLine());
        while (!isValidChoice(choice, doorOptions)) {
            System.out.println("That's not a door. Choose a door (1-" + DOOR_COUNT + "):");
            choice = cleanInput(input.nextLine());
        }
    }

    /**
     * Prompts the player to choose a starting weapon from availableWeapons,
     * re-prompting on invalid input.
     *
     * @return the chosen Weapon
     */
    public Weapon chooseWeapon() {
        List<String> weaponOptions = new ArrayList<String>();
        System.out.println("Choose your weapon:");
        for (int i = 0; i < availableWeapons.size(); i++) {
            weaponOptions.add(Integer.toString(i + 1));
            System.out.println((i + 1) + ". " + availableWeapons.get(i).getName());
        }

        String choice = cleanInput(input.nextLine());
        while (!isValidChoice(choice, weaponOptions)) {
            System.out.println("Not a valid choice. Try again:");
            choice = cleanInput(input.nextLine());
        }
        return availableWeapons.get(Integer.parseInt(choice) - 1);
    }

    /**
     * Randomly picks an enemy template to spawn.
     *
     * @return the spawned Enemy
     * @throws IllegalStateException if there are no enemy templates to pick from
     */
    public Enemy spawnEnemy() {
        if (enemyTemplates.isEmpty()) {
            throw new IllegalStateException("No enemy templates to spawn from.");
        }
        return enemyTemplates.get(random.nextInt(enemyTemplates.size()));
    }

    /**
     * Trims and lowercases raw input so comparisons are consistent
     * regardless of whitespace or capitalization.
     *
     * @param raw the raw line read from the player
     * @return the cleaned input, or an empty string if raw is null
     */
    public String cleanInput(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.trim().toLowerCase();
    }

    /**
     * Checks whether cleaned input matches one of the valid options.
     *
     * @param input the already-cleaned input
     * @param options the list of valid options
     * @return true if input is one of options, false otherwise
     */
    public boolean isValidChoice(String input, List<String> options) {
        return options.contains(input);
    }

    /**
     * Gets the player's current HP.
     *
     * @return current HP
     */
    public int getHp() {
        return player.getHp();
    }

    /**
     * Gets the player's currently equipped weapon.
     *
     * @return the equipped weapon
     */
    public Weapon getEquippedWeapon() {
        return player.getEquippedWeapon();
    }

    /**
     * Gets the number of encounters the player has cleared so far.
     *
     * @return encounters cleared
     */
    public int getEncountersCleared() {
        return player.getEncountersCleared();
    }

    /**
     * Checks whether the player has cleared enough encounters to win.
     *
     * @param encountersToWin the number of encounters required to win
     * @return true once encounters cleared meets or exceeds encountersToWin
     */
    public boolean hasWon(int encountersToWin) {
        return player.getEncountersCleared() >= encountersToWin;
    }

    /**
     * Entry point: starts a game against real console input.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        new Game().run();
    }
}
