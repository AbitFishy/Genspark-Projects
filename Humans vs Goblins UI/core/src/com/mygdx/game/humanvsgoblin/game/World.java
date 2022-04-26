package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class World {

    protected final int x;
    protected final int y;
    protected  final Dungeon dungeon;
    protected Texture texture;
    protected int tileSize = 64;

    public void remove(Item toBeRemoved) {
        diedThisRound.add(toBeRemoved);
    }

    protected enum MoveOrAttack {MOVE, ATTACK,}

    World(Dungeon dungeon, int x, int y){
//        var sg = new Entity[x][];
 //       for (var e: sg)
//        {
 //           e = new Entity[y];
//        }

        grid = new ArrayList<>();
        for (int i = 0; i < y; i++){
            grid.add(new ArrayList<>());
        }

        for (var row : grid){
            for (int j = 0; j < x; j++){
                row.add(new ArrayList<>());
            }
        }
        toBeMoved = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.dungeon = dungeon;

        //grid.stream().flatMap(ArrayList::stream).forEach(e-> e.add(new PassableLand(new Sprite(texture ))));
        /*for (int j = y -1; j >= 0; j--){
            for (int i = 0; i < x; i++){
                var s = "mapTile_022.png";
                //s.scale(.5f);
                grid.get(i).get(j).add(new PassableLand(s, new Coords(i,j)));
            }
        }*/
    }


/*    public String drawGrid(){
        StringBuilder sb = new StringBuilder();
        for (int j = y - 1; j >= 0; j--){
            for (int i = 0; i< x; i++){
                sb.append(Objects.nonNull(grid[i][j] ) ? grid[i][j].toString() : " ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }*/
/*    public ArrayList<Entity> entitiesAt(int x, int y){
        return grid[x][y];
    }*/
    public boolean canMoveHere(Entity mover, Coords destination){
        var dests = get(destination);
        if (dests == null){
            return false;
        }
        return dests.stream().allMatch(d ->
                d.canShareTileWith(mover));
    }

    public void moveEntity(Entity entity, Coords fromCoords, Coords toCoords, World.MoveOrAttack moa){
        //TODO mark if move is excluded from fights
        if (entity != null){
            if (validSpace(entity.getCoords())){
                toBeMoved.add(new Actions(entity, fromCoords,toCoords, moa));
            }

        }

    }
    public void addEntity(Entity entity, Coords toCoords){
        if (entity != null){
            toBeMoved.add(new Actions(entity, toCoords,toCoords, MoveOrAttack.MOVE));
        }
    }
    protected boolean validSpace(Coords coords){
        return coords.x >= 0 && coords.x < x && coords.y >= 0 && coords.y < y;
    }
    protected boolean validSpace(int vx, int vy){
        return validSpace(new Coords(vx,vy));
    }

    protected ArrayList<ArrayList<ArrayList<Entity>>> grid;
    protected ArrayList<Actions> toBeMoved;
/*
        if ((grid[toX][toY] == null) ){
            if (grid[fromX][fromY] != null){
                grid[toX][toY] = grid[fromX][fromY];
                grid[fromX][fromY] = null;
                return true;
            }
        }
        else {
            if (grid[fromX][fromY] != null){
                Dungeon.Conflict(grid[fromX][fromY],grid[toX][toY]);
            }
        }
        return false;
    }
    */
    public ArrayList<Entity> get(Coords coords){
        return get(coords.x,coords.y);
    }
    public ArrayList<Entity> get(int x, int y){
        if (x >= 0 && x < this.x
        &&  y >= 0 && y < this.y)
        {
            return grid.get(x).get(y);
        }
        else{
            return null;
        }
    }
    protected void set(Coords coords, Entity entity){
        set(coords.x,coords.y ,entity);
        if (entity != null) {
            entity.setCoords(coords);
        }
    }
    protected void set(int x, int y, Entity entity){
        if (x >= 0 && x < this.x
                &&  y >= 0 && y < this.y)
        {
            grid.get(x).get(y).add(entity);
        }
    }

    private HashSet<Entity> diedThisRound = new HashSet<>();

    public HashSet<Entity> move(){
        //toBeMoved.sort(Comparator.comparingInt(lhs -> lhs.first.getMovePriority()));

        toBeMoved.sort((lhs,rhs) -> -1 * Integer.compare(lhs.entity.getMovePriority(), rhs.entity.getMovePriority()));

        HashSet<Entity> dead = new HashSet<>();
        HashSet<Entity> newEnts= new HashSet<>();

        for (var iter = toBeMoved.listIterator(); iter.hasNext(); ){
            var p = iter.next();
            iter.remove();
            Entity e1 = p.entity;
            ArrayList<Entity> toSquare = get(p.destination);
            if (dead.contains(e1)){
                continue;
            }

            boolean spaceBlocked = false;

            for (var innerIter = toSquare.listIterator(); innerIter.hasNext();) {
                Entity e2 = innerIter.next();
                //dead = (HashSet<Entity>) dead.stream().filter(e -> !e.isInanimateObject()).collect(Collectors.toSet());
                if (dead.contains(e2)) {
                    e2 = null;

                }

                    if (e2 == null) {
                            innerIter.remove();

                    } else {
                        if (p.moa == MoveOrAttack.ATTACK) {
/*                            if (e1.isAlly(e2)) {
                                spaceBlocked = true;

                            } else {*/


                            switch (dungeon.Conflict(e1, e2)) {
                                case defenderDied:
                                    //set(p.second.x, p.second.y, e1);
                                    Item drop = dungeon.getDrop(e2);
                                    if (drop != null) {
                                        drop.setCoords(e2.getCoords());
                                        dungeon.getLastFightData().drop = drop;
                                    }
                                    addIfReallyDead(dead, e2);
                                    break;
                                case bothLived:
                                    break;
                                case bothDied:
                                    addIfReallyDead(dead, e1);
                                    addIfReallyDead(dead, e2);
                                    break;
                                case attackerDied:
                                    //spaceBlocked = true;
                                    addIfReallyDead(dead, e1);
                            }
                            if (p.fightInfo == null) {
                                p.fightInfo = new ArrayList<>();
                            }
                            p.fightInfo.add(dungeon.getLastFightData());
                            if (!p.fightOccurred) {
                                p.fightOccurred = p.fightInfo.get(p.fightInfo.size() - 1).fightOccurred;
                            }


                        } else { //if just moving
                            if (!e1.canShareTileWith(e2)) {
                                spaceBlocked = true;
                                p.moveBlocked = true;
                                p.moveBlockedBy = e2;
                            } else {
                                if (e2 instanceof Item) {
                                    dungeon.Conflict(e1, e2);
                                    p.fightInfo.add(dungeon.getLastFightData());
                                    p.pickupItemOpportunity = true;
                                    p.itemToPickUp = (Item) e2;
                                }

                            }

                        }
                    }

            }

            dungeon.addAction(p);
            toSquare.removeAll(dead);
            if (dead.contains(e1)){
                get(e1.getCoords()).remove(e1);
            }
            else if (!spaceBlocked){
                p.moveSuccess = true;
                //set(e.getCoords(), e);
            }

        }
        //TODO remove dead from calculations here, but keep them *somewhere* so they
        //TODO still display until their death is shown/(animated)
        diedThisRound.addAll(dead);
        newEnts.forEach(e -> moveEntity(e,e.getCoords(),e.getCoords(),MoveOrAttack.MOVE));
        return dead;
    }

    private void addIfReallyDead( Set<Entity> theDead,Entity entity){
        if (!entity.isInanimateObject()){
            theDead.add(entity);
        }
    }

    public void displayGrid(SpriteBatch batch) {

        grid.stream().flatMap(ArrayList<ArrayList<Entity>>::stream).flatMap(ArrayList<Entity>::stream)
                .sorted(Comparator.comparingInt(Entity::getMovePriority)).forEach(d-> {
                    displaySprite(batch, d);
                });

    }

    public void displayDead(SpriteBatch batch){
        diedThisRound.forEach(d -> displaySprite(batch, d));
    }
    public void clearDead(){
        diedThisRound.forEach(this::removeFromGrid);
        diedThisRound.clear();
    }
    public void clearDead(Entity dead){
        removeFromGrid(dead);
        diedThisRound.remove(dead);
    }

    private void removeFromGrid(Entity dead){
        get(dead.getCoords()).remove(dead);
    }

    protected void displaySprite(SpriteBatch batch, Entity d) {
        Sprite s = dungeon.spriteManager.retrieveSprite(d.getSprite());
        s.setPosition(d.getCoords().x * tileSize, d.getCoords().y * tileSize);
        s.draw(batch);
    }
}
