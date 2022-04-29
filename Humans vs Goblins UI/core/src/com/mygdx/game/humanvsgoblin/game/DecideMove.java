package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class DecideMove {
    protected Sprite pathOverlay;
    protected Sprite targetOverlay;
    protected Coords currentPosition =  Coords.nullCoords;
    protected Coords targetPosition = Coords.nullCoords;
    protected ArrayList<Coords> path = new ArrayList<>();
    protected Dungeon dungeon;
    protected Entity mover;

    DecideMove(Dungeon dungeon, Entity toBeMoved){
        pathOverlay = new Sprite(new Texture("blueoverlay.png"));
        targetOverlay = new Sprite(new Texture("orangeoverlay.png"));
        this.dungeon = dungeon;
        this.mover = toBeMoved;
    }

    void newMove(Coords position){
        currentPosition = position;
        targetPosition = Coords.nullCoords;
        path = new ArrayList<>();
        path.add(currentPosition);
    }

   List<Coords> getPath(){
/*        if (!targetPosition.equals(Coords.nullCoords)){
            path.add(targetPosition);
        }*/
        if (path.size() >1) {
            return path.subList(1, path.size());
        }
        else{
            return new ArrayList<>();
        }
    }

    Coords getCurrentProspectivePosition(){
        return path.get(path.size() -1);
    }

    void continueMove(final Dungeon.cardinalDirections direction){
        if (direction == Dungeon.cardinalDirections.NONE){
            return;
        }
        var pathLength = path.size();
        var prospective = applyDirection(path.get(pathLength-1), direction);
        var iter = path.listIterator(pathLength-1);
        if (iter.hasPrevious()){
            var i = iter.previous();
            if (i.equals(prospective)){
                //if (iter.hasPrevious()){
                    path.remove(path.get(pathLength-1));
                    return;
                //}
            }
        }
        if (pathLength<= mover.getMoveDistance() && dungeon.world.canMoveHere(dungeon.player,prospective)){
            path.add(prospective);
        }

    }

    Coords applyDirection(final Coords origin, final Dungeon.cardinalDirections direction) {
        var prospective = origin.deepCopy();
        switch (direction) {
            case NORTH:
                prospective.y++;
                break;
            case SOUTH:
                prospective.y--;
                break;
            case EAST:
                prospective.x++;
                break;
            case WEST:
                prospective.x--;
                break;
            case NONE:
                break;
        }
        return prospective;
    }

    void displayPath(SpriteBatch batch){
        for (var spot : path.subList(1,path.size())){
            pathOverlay.setPosition(spot.x*dungeon.world.tileSize, spot.y*dungeon.world.tileSize);
            pathOverlay.draw(batch);
        }
        if (!targetPosition.equals((Coords.nullCoords))){
            targetOverlay.setPosition(targetPosition.x*dungeon.world.tileSize, targetPosition.y*dungeon.world.tileSize);
            targetOverlay.draw(batch,.5f);
        }
    }

    public void highlightTarget(final Dungeon.cardinalDirections direction){
        Coords attackCoords = getCurrentProspectivePosition();
        if (direction == Dungeon.cardinalDirections.NONE){
            return;
        }
        if (!targetPosition.equals(Coords.nullCoords)){
            if (attackCoords.equals(applyDirection(targetPosition, direction))){
                targetPosition = Coords.nullCoords;
            }
        }
        else {
            var prospective = applyDirection(attackCoords, direction);
            var tile = dungeon.world.get(prospective);
            if (tile != null && tile.stream().anyMatch(mover::isEnemy)) {
                targetPosition = prospective;
            }
        }
    }
}
