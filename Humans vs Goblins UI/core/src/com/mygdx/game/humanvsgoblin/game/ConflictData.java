package com.mygdx.game.humanvsgoblin.game;

public class ConflictData {
    ConflictData(int given, int taken, boolean actual) {
        this(given,taken,actual, null);
    }
        ConflictData(int given, int taken, boolean actual, Item item){
        damageGiven = given;
        damageTaken = taken;
        actualFight=  actual;
        this.item = item;

    }
    int damageGiven;
    int damageTaken;
    boolean actualFight;

    Item item;
}
