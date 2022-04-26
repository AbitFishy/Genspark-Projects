import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

public class Dungeon {
    private static final Set<String> YES_LINES = Set.of("yes", "YES", "Yes", "Y", "y");
    private static final Set<String> NO_LINES = Set.of("no", "NO", "No", "N", "n");
    private static final Set<String> NORTH_LINES = Set.of("north", "NORTH", "North", "N", "n");
    private static final Set<String> SOUTH_LINES = Set.of("south", "SOUTH", "South", "S", "s");
    private static final Set<String> WEST_LINES = Set.of("west", "WEST", "West", "W", "w");
    private static final Set<String> EAST_LINES = Set.of("east", "EAST", "East", "E", "e");

    static double random(boolean testMode){
        if (!testMode){
            return Math.random();
        }
        else{
            return testRandoms.remove();
        }
    }
    static void quit(){
        if (testMode){
            throw new RuntimeException("Game Exited");
        }
        else{
            System.exit(-1);
        }
    }
    static Queue<Double> testRandoms = new LinkedList<>();
    static boolean testMode = false;

    public void configGame() {
        configGame("game files/dungeongameconfig.txt");
    }

    public void configGame(String configFile) {
        generateMap(10, 10);

        try {
            BufferedReader fr = new BufferedReader(new FileReader(configFile));

            while (fr.ready()) {
                String line = fr.readLine();
                var la = line.split(" ");
                Entity h = null;
                switch (la[0]) {
                    case "Human":
                        if (la.length == 3) {
                            h = new Human(Integer.parseInt(la[1]), Integer.parseInt(la[2]));
                        } else if (la.length > 1) {
                            h = new Human(Integer.parseInt(la[1]), Integer.parseInt(la[2]),
                                    Integer.parseInt(la[3]), Integer.parseInt(la[4]), la[5],
                                    Integer.parseInt(la[6]), Integer.parseInt(la[7]), Integer.parseInt(la[9]));
                        } else {
                            h = new Human();
                        }
                        player = (LivingCreature) h;
                        break;
                    case "Goblin":
                        if (la.length == 3) {
                            h = new Goblin(Integer.parseInt(la[1]), Integer.parseInt(la[2]));
                        } else if (la.length > 1) {
                            h = new Goblin(Integer.parseInt(la[1]), Integer.parseInt(la[2]),
                                    Integer.parseInt(la[3]), Integer.parseInt(la[4]), la[5],
                                    Integer.parseInt(la[6]), Integer.parseInt(la[7]), Integer.parseInt(la[9]));
                        } else {
                            h = new Goblin();
                        }
                        enemies.add((LivingCreature) h);
                        break;
                }
                if (h != null) {
                    world.addEntity(h, h.getCoords());
                }
            }

        } catch (FileNotFoundException fnfe) {
            System.out.println("FILE NOT FOUND: " + configFile);
            quit();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO ERROR READING CONFIG FILE: " + configFile);
            quit();
        }

        if (player == null) {
            System.out.println("NO PLAYER CONFIGURED");
            quit();
        }
    }

    public void play() {
        System.out.println("HUMANS VS GOBLINS!");

        while (loop()) {
            //do nothing
        }
        if (win) {
            System.out.println("YOU DEFEATED ALL OF THE GOBLINS! YOU WIN!");
        } else {
            System.out.println("YOU DIED! YOU LOSE!");
        }
    }

    boolean loop() {
        var dead  = world.move();
        enemies.removeAll(dead);
        if (enemies.size() == 0) {
            win = true;
            return !player.isDead() && !win;
        }
        if (player.isDead()){
            return false;
        }

        System.out.println("\n\nThe land around you...");
        System.out.println(world.drawGrid());
        Coords newCoords = player.getCoords().deepCopy();
        StringBuilder dir = new StringBuilder("You have " + player.health + " points of health.\nWhich Direction would you like to go?\n");
        boolean n, s, e, w;
        n = s = e = w = false;
        if (world.validSpace(newCoords.x, newCoords.y + 1)) {
            dir.append("North ");
            showEntityInDirection(newCoords.x, newCoords.y + 1, "North");
            n = true;
        }
        if (world.validSpace(newCoords.x, newCoords.y - 1)) {
            dir.append("South ");
            showEntityInDirection(newCoords.x, newCoords.y - 1, "South");
            s = true;
        }
        if (world.validSpace(newCoords.x + 1, newCoords.y)) {
            dir.append("East ");
            showEntityInDirection(newCoords.x+1, newCoords.y, "East");
            e = true;
        }
        if (world.validSpace(newCoords.x - 1, newCoords.y)) {
            dir.append("West");
            showEntityInDirection(newCoords.x - 1, newCoords.y, "West");
            w = true;
        }
        switch (getNSWE(dir.toString(), n, s, e, w)) {
            case NORTH:
                ++newCoords.y;
                break;
            case SOUTH:
                --newCoords.y;
                break;
            case WEST:
                --newCoords.x;
                break;
            case EAST:
                ++newCoords.x;
                break;
        }
        world.moveEntity(player.getCoords(), newCoords);

        return !player.isDead() && !win;
    }

    private void showEntityInDirection(int x, int y, String direction) {
        Entity entity = world.entityAt(x, y);
        if (entity != null){
            System.out.println( "There's a " + entity.getName() + " to the " + direction);
        }
    }

    enum cardinalDirections {NORTH, SOUTH, WEST, EAST}

    LivingCreature player = null;
    ArrayList<LivingCreature> enemies = new ArrayList<>();
    boolean win = false;


    public static float getCrit(float critDam, float critRate) {
        if (random(testMode) < critRate) {
            return 1 + critDam;
        } else {
            return 1f;
        }
    }

    public static @Nullable Item getDrop(Entity entity) {
        if (entity instanceof LivingCreature) {
            var creature = (LivingCreature) entity;
            int bonus = creature.getAttackBonus();
            int damage = (int) creature.getDamage(bonus, 1, creature.getCritDam(), creature.getCritRate());

            if (damage > 40) {
                //item quality stuff @TODO
            }

            return new Sword(); //TODO return actual item
        }
        return null;
    }

    public static int calcDamageTaken(int damage, int defense) {
        if (damage - defense > 0) {
            return (int) ((damage - defense) * (1 + random(testMode)));
        } else if (random(testMode) <  (float) (defense - damage + 1) / (defense + 1)) {
            return (int) (damage * random(testMode));

        }
        return 0;
    }

    enum conflictResult {
        attackerDied,
        defenderDied,
        bothDied,
        bothLived,
    }

    static conflictResult Conflict(@NotNull Entity attacker, @NotNull Entity defender) {
        fight(attacker, defender);
        return resolve(attacker, defender);
    }

    static void fight(@NotNull Entity attacker, @NotNull Entity defender) {
        attacker.conflict(defender, true);
        defender.conflict(attacker, false);
    }

    static conflictResult resolve(@NotNull Entity attacker, @NotNull Entity defender) {
        if (attacker.isDead() && defender.isDead()) {
            entityHasDied(attacker);
            entityHasDied(defender);
            return conflictResult.bothDied;
        } else if (!attacker.isDead() && !defender.isDead()) {
            return conflictResult.bothLived;
        } else if (!attacker.isDead() && defender.isDead()) {
            entityHasDied(defender);
            return conflictResult.defenderDied;
        } else //attacker.isDead() && !defender.isDead(){
            entityHasDied(attacker);
            return conflictResult.attackerDied;
    }

    private static void entityHasDied(@NotNull Entity entity) {
        if (entity instanceof LivingCreature) {
            System.out.println(entity.getName() + " has died!");
        }
    }

    static boolean promptForItem(@NotNull Item item, HasInventory entity) {
        System.out.println("You see a " + item.getName() + " on the ground.  Pick it up?");
        return getYesOrNo();
    }

    static boolean getYesOrNo() {

        int attempts = 20;
        while (attempts-- >= 0) {
            System.out.println("Yes or No");
            try {
                String line = br.readLine();
                if (YES_LINES.contains(line)) {
                    return true;
                } else if (NO_LINES.contains(line)) {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }
        System.out.println("Too Many Bad Inputs in getYesOrNo. Quitting");
        quit();
        return false;
    }

    static cardinalDirections getNSWE(String prompt, boolean north, boolean south, boolean east, boolean west) {
        int attempts = 20;
        while (attempts-- >= 0) {
            System.out.println(prompt);
            try {
                String line = br.readLine();
                if (north && NORTH_LINES.contains(line)) {
                    return cardinalDirections.NORTH;
                } else if (south && SOUTH_LINES.contains(line)) {
                    return cardinalDirections.SOUTH;
                } else if (west && WEST_LINES.contains(line)) {
                    return cardinalDirections.WEST;
                } else if (east && EAST_LINES.contains(line)) {
                    return cardinalDirections.EAST;
                }
            } catch (IOException e) {
                System.out.println("IO EXCEPTION READING DIRECTION");
                quit();
                return cardinalDirections.NORTH;
            }
        }
        System.out.println("Too Many Bad Inputs in getYesOrNo. Quitting");
        quit();
        return cardinalDirections.NORTH;

    }

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    void generateMap(int x, int y) {
        world = new World(x, y);
    }

    World world;
}
