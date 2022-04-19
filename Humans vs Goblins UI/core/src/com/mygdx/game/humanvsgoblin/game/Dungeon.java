package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.Queue;
import java.util.stream.Collectors;

public class Dungeon {
    protected  static final Set<String> YES_LINES = Set.of("yes", "YES", "Yes", "Y", "y");
    protected  static final Set<String> NO_LINES = Set.of("no", "NO", "No", "N", "n");
    protected  static final Set<String> NORTH_LINES = Set.of("north", "NORTH", "North", "N", "n");
    protected  static final Set<String> SOUTH_LINES = Set.of("south", "SOUTH", "South", "S", "s");
    protected  static final Set<String> WEST_LINES = Set.of("west", "WEST", "West", "W", "w");
    protected  static final Set<String> EAST_LINES = Set.of("east", "EAST", "East", "E", "e");

    protected ArrayList<Fights> fightList = new ArrayList<>();

    public void makeConfigFile(){
        Json json = new Json();
        HashMap<String, ArrayList<Entity>> hm = new HashMap<>();
        Sprite sprite;

        var tex = new Texture("mapTile_022.png");
        ArrayList<Entity> grounds = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                sprite = new Sprite(tex);
                PassableLand pl = new PassableLand(sprite, new Coords(i,j));
                grounds.add(pl);
            }
        }

        ArrayList<Entity> players = new ArrayList<>();
        var player = new Human(150,10, 10, 1, "Player",
                new Coords(5,5),100,100, 1.5f,
                1.5f, .09f, 1, 1);
        player.setSprite(new Sprite(new Texture("knight.png")));
        players.add(player);

        ArrayList<Entity> enemies = new ArrayList<>();
        Goblin goblin = new Goblin(50, 10, 13, 1 , "Goblin",
                new Coords(0,0), 2, 30, 1.5f,
                1.5f,.11f, 2, 2);
        goblin.setSprite(new Sprite(new Texture("goblin.png")));
        enemies.add(goblin);

        hm.put("player", players);
        hm.put("enemies", enemies);
        hm.put("ground", grounds);
        String j = json.prettyPrint(hm);
        var fh = Gdx.files.local("config.json");
        fh.writeString(j,false);
    }

    protected static double random(boolean testMode){
        if (!testMode){
            return Math.random();
        }
        else{
            return testRandoms.remove();
        }
    }
    protected  static void quit(){
        if (testMode){
            throw new RuntimeException("Game Exited");
        }
        else{
            System.exit(-1);
        }
    }
    protected static Queue<Double> testRandoms = new LinkedList<>();
    protected static boolean testMode = false;

    public void configGame() {
        configGame("config.json");
    }

    public void configGame(String configFile) {
        generateMap(10, 10);
        var json = new Json();

        try {
            //var fh = Gdx.files.getFileHandle(configFile, Files.FileType.Internal);
            //var objr = new BufferedReader(fh.reader());
            var fh = Gdx.files.getFileHandle(configFile, Files.FileType.Local);
            String fs = fh.readString();

            HashMap<String, ArrayList<Entity>> map = json.fromJson(HashMap.class, fs);

            player = (LivingCreature) map.get("Player").get(0);
            var enemies = map.get("Enemies");
            var ground = map.get("Ground");
            var items = map.get("Items");

            world.addEntity(player,player.coords);
            for (var enemy : enemies) {
                world.addEntity(enemy, enemy.getCoords());
            }
            this.enemies = (ArrayList<LivingCreature>) enemies.stream().map(e -> (LivingCreature)e).collect(Collectors.toList());
            for (var g : ground){
                world.addEntity(g, g.getCoords());
            }
            for (var item : items){
                world.addEntity(item,item.getCoords());
            }


            //TODO add all needed parameters to entities
            //TODO allegiance, enemiesWith, moveDistance, etc
            //TODO do so by using json writing

        } catch (Exception e) {
            System.out.println("Error reading config file " + configFile);
            System.out.println(e);
            quit();
        }

        if (player == null) {
            System.out.println("NO PLAYER CONFIGURED");
            quit();
        }
    }
    private ListIterator<Fights> fightIter = null;
    public boolean hasNextFight() {
        if (fightIter == null){
            fightIter = fightList.listIterator();
        }
        var ret =  fightIter.hasNext();
        if (!ret){
            fightIter = null;
        }
        return ret;
    }

    public void showNextFight() {
        Fights fight = fightIter.next();
        if (fight.fightOccurred){
            //TODO show fight info
        }
        if (fight.drop != null){
            //TODO ask if player wants item

        }
    }

    public void showNothingThisRound() {
        //TODO
    }

/*    public void play() {
        System.out.println("HUMANS VS GOBLINS!");

        while (loop()) {
            //do nothing
        }
        if (win) {
            System.out.println("YOU DEFEATED ALL OF THE GOBLINS! YOU WIN!");
        } else {
            System.out.println("YOU DIED! YOU LOSE!");
        }
    }*/

/*    protected boolean loop() {
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
        world.moveEntity(player, newCoords);

        return !player.isDead() && !win;
    }*/

/*    protected  void showEntityInDirection(int x, int y, String direction) {
        ArrayList<Entity> entity = world.get(x, y);
        if (entity != null){
            System.out.println( "There's a " + entity.getName() + " to the " + direction);
        }
    }*/



    enum cardinalDirections {NORTH, SOUTH, WEST, EAST, NONE}

    protected LivingCreature player = null;
    protected ArrayList<LivingCreature> enemies = new ArrayList<>();
    protected boolean win = false;


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

    public conflictResult Conflict(@NotNull Entity attacker, @NotNull Entity defender) {
        Fights fightdata = new Fights();
        fightdata.attacker = attacker;
        fightdata.defender = defender;

        fight(fightdata, attacker, defender);
        var res = resolve(fightdata, attacker, defender);
        this.fightList.add(fightdata);
        return res;
    }

    protected void fight(Fights fightdata, @NotNull Entity attacker, @NotNull Entity defender) {


        var res = attacker.conflict(defender, true);
        fightdata.attackDamage = res.damageGiven;
        fightdata.attackDamageTaken = res.damageTaken;
        fightdata.fightOccurred = res.actualFight;
        res = defender.conflict(attacker, false);
        fightdata.defenseDamage = res.damageGiven;
        fightdata.defenseDamageTaken = res.damageTaken;
    }

    public conflictResult resolve(Fights fightdata, @NotNull Entity attacker, @NotNull Entity defender) {
        if (attacker.isDead() && defender.isDead()) {
            entityHasDied(attacker);
            entityHasDied(defender);
            fightdata.result = conflictResult.bothDied;
            return conflictResult.bothDied;
        } else if (!attacker.isDead() && !defender.isDead()) {
            fightdata.result = conflictResult.bothLived;
            return conflictResult.bothLived;
        } else if (!attacker.isDead() && defender.isDead()) {
            entityHasDied(defender);
            fightdata.result = conflictResult.defenderDied;
            return conflictResult.defenderDied;
        } else //attacker.isDead() && !defender.isDead(){
            entityHasDied(attacker);
        fightdata.result = conflictResult.attackerDied;
            return conflictResult.attackerDied;
    }

    protected  static void entityHasDied(@NotNull Entity entity) {
        if (entity instanceof LivingCreature) {
            System.out.println(entity.getName() + " has died!");
        }
    }

    protected static boolean promptForItem(@NotNull Item item, HasInventory entity) {
        System.out.println("You see a " + item.getName() + " on the ground.  Pick it up?");
        return getYesOrNo();
    }

    protected static boolean getYesOrNo() {

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

    protected static cardinalDirections getNSWE(String prompt, boolean north, boolean south, boolean east, boolean west) {
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

    protected static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    protected void generateMap(int x, int y) {
        world = new World(this, x, y);
    }

    protected World world;
}
