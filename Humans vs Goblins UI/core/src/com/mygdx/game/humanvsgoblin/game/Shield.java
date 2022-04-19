package com.mygdx.game.humanvsgoblin.game;

public class Shield extends FakeDefenseItem {
    Shield(){
        this(10);
    }
    Shield(int defense){
        name = "Shield";
        this.defense = defense;
        displayChar = '⛨';
    }
}
