package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.lang.StringBuilder;
import java.util.*;
import java.util.Queue;
import java.util.stream.Collectors;

public class Dungeon {
/*    protected  static final Set<String> YES_LINES = Set.of("yes", "YES", "Yes", "Y", "y");
    protected  static final Set<String> NO_LINES = Set.of("no", "NO", "No", "N", "n");
    protected  static final Set<String> NORTH_LINES = Set.of("north", "NORTH", "North", "N", "n");
    protected  static final Set<String> SOUTH_LINES = Set.of("south", "SOUTH", "South", "S", "s");
    protected  static final Set<String> WEST_LINES = Set.of("west", "WEST", "West", "W", "w");
    protected  static final Set<String> EAST_LINES = Set.of("east", "EAST", "East", "E", "e");*/


    public final LivingCreature getPlayer(){
        return player;
    }
    public final ArrayList<LivingCreature> getEnemies(){
        return enemies;
    }
    public final SpriteManager getSpriteManager(){
        return spriteManager;
    }

    protected ArrayList<Actions> actionList = new ArrayList<>();
    protected SpriteManager spriteManager = new SpriteManager();
    protected EntityFactory entityFactory = new EntityFactory(spriteManager);
    protected  InfoBox infoBox;

    //protected int delta;

    public void makeConfigFile(String fileName){
        Json json = new Json();
        //HashMap<String, Entity[]> hm = new HashMap<>();
        GameSaveFile gsf = new GameSaveFile();
        String sprite;

        //var tex = new Texture("mapTile_022.png");
        ArrayList<Entity> grounds = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                sprite = "mapTile_022.png";
                PassableLand pl = new PassableLand(sprite, new Coords(i,j));
                grounds.add(pl);
            }
        }

        ArrayList<Entity> players = new ArrayList<>();
        var player = new Human(150,10, 10, 1, "Player",
                new Coords(5,5),100,100, 1.5f,
                1.5f, .09f, 1, 1);
        player.setSprite("knight.png");
        players.add(player);

        ArrayList<Entity> enemies = new ArrayList<>();
        Goblin goblin = new Goblin(50, 10, 13, 1 , "Goblin",
                new Coords(5,6), 2, 30, 1.5f,
                1.5f,.11f, 2, 2);
        goblin.setSprite("goblin.png");
        enemies.add(goblin);

        ArrayList<Entity> items = new ArrayList<>();
        Item item = new Sword();
        item.setSprite("sword.png");
        items.add(item);

        gsf.setPlayers(players);
        gsf.setEnemies(enemies);
        gsf.setGrounds(grounds);
        gsf.setItems(items);

        String j = json.prettyPrint(gsf);
        var fh = Gdx.files.local(fileName);
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

            //HashMap<String, Array<Entity>> map = json.fromJson(HashMap.class, fs);
            GameSaveFile gsf = json.fromJson(GameSaveFile.class, fs);

            player = (LivingCreature) gsf.getPlayers().get(0);
            var enemies = gsf.getEnemies();
            var ground = gsf.getGrounds();
            var items = gsf.getItems();

            entityFactory.registerEntity(player.getName(), player.getSprite());

            player.sprite = spriteManager.setSpriteFromFilename(player.sprite);
            world.set(player.coords,player);
            for (var enemy : enemies) {
                entityFactory.registerEntity(enemy.getName(),  enemy.getSprite());
                enemy.setSprite(spriteManager.setSpriteFromFilename(enemy.getSprite()));
                world.set(enemy.getCoords(),enemy);
            }
            this.enemies = (ArrayList<LivingCreature>) enemies.stream().map(e -> (LivingCreature)e).collect(Collectors.toList());
            for (var g : ground){
                entityFactory.registerEntity(g.getName(), g.getSprite());
                g.setSprite(spriteManager.setSpriteFromFilename(g.getSprite()));
                world.set(g.getCoords(), g);
            }
            for (var item : items){
                entityFactory.registerEntity(item.getName(), item.getSprite());
                item.setSprite(spriteManager.setSpriteFromFilename(item.getSprite()));
                world.set(item.getCoords(), item);
            }

        } catch (Exception e) {
            System.out.println("Error reading config file " + configFile);
            e.printStackTrace();
            quit();
        }

        if (player == null) {
            System.out.println("NO PLAYER CONFIGURED");
            quit();
        }
    }
    private ListIterator<Actions> actionIter = null;
    private boolean endOfActions = false;
    public boolean hasNextAction() {
        if (!endOfActions && actionIter == null){
            actionIter = actionList.listIterator();
        }

        var ret =  actionIter.hasNext();
        if (!ret){
            endOfActions = true;

        }
        return ret;
    }
    public void resetActions(){
        actionList.clear();
        actionIter = null;
        endOfActions = false;
    }
/*    enum ActionReplayState {NEW_ACTION, CONTINUE_ACTION}
    Actions currentAction;
    int waitTime = 1000; // 1 second
    int timeWaited;*/

    StringBuilder actionStr = new StringBuilder();
    //int actionCount = 0;
    public boolean showNextAction() {
        var currentAction = actionIter.next();
        if (currentAction.moa == World.MoveOrAttack.MOVE) {
            if (currentAction.moveSuccess) {
                var e = currentAction.entity;
                //e.setCoords(currentAction.destination);
                world.set(currentAction.destination,e); //TODO decide if this is actually when the move happens
                actionStr.append(e.getName()).append(" moved to ").append(e.getCoords().toString());
                //infoBox.setText(currentAction.entity.getName() + " moved");
                // actionStr.append(actionCount++).append(": ").append(currentAction.entity.getName()).append(" moved. ");

                // actionStr.append(actionCount++).append(": ").append(currentAction.entity.getName()).append(" blocked. ");
            }
            else {
                var e = currentAction.entity;
                actionStr.append(e.getName()).append(" was blocked by ").append( currentAction.moveBlockedBy).append(" at ").append(e.getCoords().toString());
            }
        }
        else {
            if (currentAction.fightOccurred) {
                currentAction.fightInfo.forEach(f -> {
                    if (f.fightOccurred) {
                        actionStr.append(f.attacker.getName()).append(" attacks ").append(f.defender.getName()).append(" with ")
                                .append(f.attackDamage).append(" points of attack, dealing ").append(f.attackDamageTaken)
                                .append(" points of damage!  ").append(f.defender.getName()).append(" counter attacks with ")
                                .append(f.defenseDamage).append(" points of attack, dealing ").append(f.defenseDamageTaken)
                                .append(" points of damage!  ");
                        switch (f.result) {
                            case defenderDied:
                                world.clearDead(f.defender);
                                if (f.defender instanceof LivingCreature) enemies.remove(f.defender);
                                actionStr.append(f.defender.getName()).append(" died. ");

                                break;
                            case attackerDied:
                                actionStr.append(f.attacker.getName()).append(" died. ");
                                world.clearDead(f.attacker);
                                if (f.attacker instanceof LivingCreature) enemies.remove(f.attacker);
                                break;
                            case bothDied:
                                actionStr.append(f.attacker.getName()).append(" died. ");
                                actionStr.append(f.defender.getName()).append(" died. ");
                                world.clearDead(f.attacker);
                                if (f.attacker instanceof LivingCreature) enemies.remove(f.attacker);
                                world.clearDead(f.defender);
                                if (f.defender instanceof LivingCreature) enemies.remove(f.defender);
                                break;
                            default:
                        }
                    }
                });
            }else {
                actionStr.append("No Fight Occurred involving ").append(currentAction.entity);
            }
        }
        boolean pickUpItem = false;
        // TODO must be a better way to handle below

            if (currentAction.pickupItemOpportunity || currentAction.fightInfo.stream().anyMatch(fi -> fi.drop != null)) {
                Item drop;
                if (currentAction.pickupItemOpportunity) {
                    drop = currentAction.itemToPickUp;
                } else {
                    drop = Objects.requireNonNull(currentAction.fightInfo.stream().filter(fi -> fi.drop != null).findFirst().orElse(null)).drop;
                }
                actionStr.append(drop.getName()).append(" was dropped.");
                dropItem = drop;
                if (currentAction.entity instanceof HasInventory) {
                    entityPickingUp = (HasInventory) currentAction.entity;
                    entityPickingUpName = currentAction.entity.getName();
                    actionStr.append(" Pick it up? ");
                    pickUpItem = true;
                }
                else{
                    declineItem();
                }
            }
        infoBox.setText(actionStr.toString());
        System.out.println("<<" + actionStr + ">>");
        return pickUpItem;
    }

    private HasInventory entityPickingUp;
    private String entityPickingUpName;
    private Item dropItem;

    public void showNothingThisRound() {
        actionStr.append("Nothing happened this turn");
        infoBox.setText(actionStr.toString());
        System.out.println("<<" + actionStr + ">>");
    }

    public void acceptItem(){
        actionStr.append(entityPickingUpName).append(" picks up ").append(dropItem.getName());
        entityPickingUp.storeItem(dropItem);
        world.remove(dropItem);
        infoBox.setText(actionStr.toString());
        System.out.println("<<" + actionStr + ">>");
        entityPickingUp = null;
        entityPickingUpName = null;
        dropItem = null;
    }
    public void declineItem(){
        actionStr.append(dropItem).append( " was left on the ground. ");
        this.world.set(dropItem.getCoords(), dropItem);
        infoBox.setText(actionStr.toString());
        System.out.println("<<" + actionStr + ">>");
        entityPickingUp = null;
        entityPickingUpName = null;
        dropItem = null;
    }

    public void addAction(Actions p)
    {
        actionList.add(p);
    }

    public void displayPending(SpriteBatch batch) {
        if (dropItem != null){
            world.displaySprite(batch,dropItem);
        }
    }

    public boolean hasWon() {
        return  !player.isDead() && enemies.size() == 0;
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

    public @Nullable Item getDrop(Entity entity) {
        if (entity instanceof LivingCreature) {
            //var creature = (LivingCreature) entity;
            //int bonus = creature.getAttackBonus();
            //int damage = (int) creature.getDamage(bonus, 1, creature.getCritDam(), creature.getCritRate());

/*            if (damage > 40) {
                //item quality stuff @TODO
            }*/
            Item drop = (Item)entityFactory.getNewEntity("Sword");
            System.out.println(entity.getName() + " dropped one " + drop.getName());
//TODO return items based on various criteria, not just the same sword
            return drop;
        }
        System.out.println("Nothing was dropped");
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
        //this.fightList.add(fightdata);
        this.lastFightData = fightdata;
        return res;
    }

    private Fights lastFightData;
    public Fights getLastFightData(){
        return lastFightData;
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


    protected void generateMap(int x, int y) {
        world = new World(this, x, y);
    }

    protected World world;
}
