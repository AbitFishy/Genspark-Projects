package com.mygdx.game.humanvsgoblin.game;

public class Goblin extends LivingCreature {
    Goblin(){
        this(20,5,5,1,"Goblin",0,0,5);
    }

    Goblin(int x, int y){
        this(50, 10, 10, 1, "Goblin", x, y, 100);
    }

    Goblin(int health, int attack, int defense, int moveSpeed, String name, int x, int y, int movePriority) {
        super(health, attack, defense, moveSpeed, name, new Coords(x, y), movePriority);
        displayChar = '❡';
    }

    public Goblin(int health, int attack, int defense, int moveDistance, String name, Coords coords, int movePriority,
                 int moveSpeed, float attackBonus, float critDam, float critRate, int allegiance, int enemiesWith)
    {
        super(health,attack,defense,moveDistance,name, coords, movePriority);
        displayChar  = '❡';
        this.moveSpeed = moveSpeed;
        this. critDam = critDam;
        this.critRate = critRate;
        this.allegiance = allegiance;
        this.enemiesWith = enemiesWith;
        this.attackBonus = attackBonus;
    }
}
