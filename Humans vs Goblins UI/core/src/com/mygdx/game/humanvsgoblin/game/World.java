package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class World {

    protected final int x;
    protected final int y;
    protected  final Dungeon dungeon;
    protected Texture texture;
    protected int tileSize = 64;

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
        texture = new Texture(Gdx.files.internal("mapTile_022.png"));

        //grid.stream().flatMap(ArrayList::stream).forEach(e-> e.add(new PassableLand(new Sprite(texture ))));
        for (int j = y -1; j >= 0; j--){
            for (int i = 0; i < x; i++){
                var s = new Sprite(texture);
                //s.scale(.5f);
                grid.get(i).get(j).add(new PassableLand(s, new Coords(i,j)));
            }
        }
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
        return dests.stream().allMatch(d ->
                d.canShareTileWith(mover));
    }

    public void moveEntity(Entity entity, Coords toCoords){
        //TODO mark if move is excluded from fights
        if (entity != null){
            if (validSpace(entity.getCoords())){
                toBeMoved.add(new Pair<>(entity,toCoords));
            }

        }

    }
    public void addEntity(Entity entity, Coords toCoords){
        if (entity != null){
            toBeMoved.add(new Pair<>(entity,toCoords));
        }
    }
    protected boolean validSpace(Coords coords){
        return coords.x >= 0 && coords.x < x && coords.y >= 0 && coords.y < y;
    }
    protected boolean validSpace(int vx, int vy){
        return validSpace(new Coords(vx,vy));
    }

    protected ArrayList<ArrayList<ArrayList<Entity>>> grid;
    protected ArrayList<Pair<Entity,Coords>> toBeMoved;
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

    public HashSet<Entity> move(){
        //toBeMoved.sort(Comparator.comparingInt(lhs -> lhs.first.getMovePriority()));

        toBeMoved.sort((lhs,rhs) -> -1 * Integer.compare(lhs.first.getMovePriority(), rhs.first.getMovePriority()));

        HashSet<Entity> dead = new HashSet<>();

        for (var iter = toBeMoved.listIterator(); iter.hasNext(); ){
            Pair<Entity, Coords> p = iter.next();
            iter.remove();
            Entity e1 = p.first;
            ArrayList<Entity> toSquare = get(p.second);
            if (dead.contains(e1)){
                continue;
            }
            boolean spaceBlocked = false;

            for (var innerIter = toSquare.listIterator(); innerIter.hasNext();) {
                Entity e2 = innerIter.next();

                if (dead.contains(e2)) {
                    e2 = null;

                }

                if (e2 == null) {
                    innerIter.remove();
                } else {
                    //if (e2.isBlocking(e1)) {
                        if (e1.isAlly(e2)){
                            spaceBlocked = true;
                        }
                        else {
                            switch (dungeon.Conflict(e1, e2)) {
                                case defenderDied:
                                    //set(p.second.x, p.second.y, e1);
                                    Item drop = Dungeon.getDrop(e2);
                                    if (drop != null) {
                                        innerIter.add(drop);
                                        innerIter.previous();
                                    }
                                    dead.add(e2);
                                    break;
                                case bothLived:
                                    if (!(e2 instanceof  PassableLand)) {
                                        spaceBlocked = true;
                                    }
                                    break;
                                case bothDied:
                                    dead.add(e1);
                                    dead.add(e2);
                                    break;
                                case attackerDied:
                                    spaceBlocked = true;
                                    dead.add(e1);

                            }
                        }
                    //}
                }
            }
            toSquare.removeAll(dead);
            if (dead.contains(e1)){
                get(e1.getCoords()).remove(e1);
            }
            else if (!spaceBlocked){
                get(e1.getCoords()).remove(e1);
                set(e1.getCoords(),e1);
            }

        }
        return dead;
    }

    public void displayGrid(SpriteBatch batch) {

        grid.stream().flatMap(ArrayList<ArrayList<Entity>>::stream).flatMap(ArrayList<Entity>::stream)
                .sorted(Comparator.comparingInt(Entity::getMovePriority)).forEach(d-> {
                    Sprite s = d.getSprite();
                    s.setPosition(d.getCoords().x * tileSize,d.getCoords().y * tileSize);
                    s.draw(batch);
                });

    }
}
