package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class PassableLand implements Entity {

    public PassableLand(Sprite sprite, Coords coords){
        this.sprite = sprite;
        this.coords = coords;
    }

    @Override
    public String getName() {
        return "Land";
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

    protected Sprite sprite = new Sprite();
    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public char getDisplayName() {
        return ' ';
    }
}
