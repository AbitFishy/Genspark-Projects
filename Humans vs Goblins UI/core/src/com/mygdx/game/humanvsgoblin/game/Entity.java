package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public interface Entity{
    String getName();
    Coords getCoords();
    void setCoords(Coords coords);
    int getMovePriority();
    int getMoveDistance();
    char symbol = '-';
    ConflictData conflict(Entity other, boolean attackerOrDefender);
    boolean isDead();
    boolean isInanimateObject();

    boolean isAlly(Entity other);
    boolean isEnemy(Entity other);
    boolean isBlocking(Entity other);
    boolean canShareTileWith(Entity other);

    String getSprite();
    void setSprite(String sprite);

    char getDisplayName();

    /*
    Coords coords = new Coords(0,0);
    int movePriority = 0;
    String name = "";
    */



}
