package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class PassableLand implements Entity {
    public PassableLand(){
        this.sprite = null;
        this.coords = null;
    }
    public PassableLand(String sprite, Coords coords){
        this.sprite = sprite;
        this.coords = coords;
    }

    @Override
    public String getName() {
        return "PassableLand";
    }

    @Override
    public Coords getCoords() {
        return coords;
    }
    protected Coords coords = new Coords();
    @Override
    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    @Override
    public int getMovePriority() {
        return 0;
    }

    @Override
    public int getMoveDistance() {
        return 0;
    }

    @Override
    public ConflictData conflict(Entity other, boolean attackerOrDefender) {
        return new ConflictData(0,0,false);
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public boolean isInanimateObject() {
        return true;
    }

    @Override
    public boolean isAlly(Entity other) {
        return false;
    }

    @Override
    public boolean isEnemy(Entity other) {
        return false;
    }

    @Override
    public boolean isBlocking(Entity other) {
        return false;
    }

    @Override
    public boolean canShareTileWith(Entity other) {
        return true;
    }

    protected String sprite ;
    @Override
    public String getSprite() {
        return sprite;
    }

    @Override
    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    @Override
    public char getDisplayName() {
        return '_';
    }
}
