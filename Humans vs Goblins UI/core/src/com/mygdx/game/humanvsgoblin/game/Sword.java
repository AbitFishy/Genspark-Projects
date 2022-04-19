package com.mygdx.game.humanvsgoblin.game;

public class Sword extends FakeAttackItem {
    Sword(){
        this(10);
    }
    Sword(int Attack){
        name = "Sword";
        attack = Attack;
        displayChar = '⚔';
    }
}
