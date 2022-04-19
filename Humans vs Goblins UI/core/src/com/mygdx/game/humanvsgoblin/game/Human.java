package com.mygdx.game.humanvsgoblin.game;

public class Human extends LivingCreature {
    public Human() {
        this(50, 10, 10, 1, "Player",0, 0, 100);
    }
    public Human(int x, int y){
        this(50, 10, 10, 1, "Player", x, y, 100);
    }
    public Human(int health, int attack, int defense, int moveDistance, String name, int x, int y, int movePriority){
        super(health,attack,defense,moveDistance,name, new Coords(x,y), movePriority);
        displayChar = '☃';
    }
    public Human(int health, int attack, int defense, int moveDistance, String name, Coords coords, int movePriority,
                 int moveSpeed, float attackBonus, float critDam, float critRate, int allegiance, int enemiesWith)
    {
        super(health,attack,defense,moveDistance,name, coords, movePriority);
        displayChar  = '☃';
        this.moveSpeed = moveSpeed;
        this. critDam = critDam;
        this.critRate = critRate;
        this.allegiance = allegiance;
        this.enemiesWith = enemiesWith;
        this.attackBonus = attackBonus;
    }
           /* if (!attackerOrDefender){
                var defenseItems = inventory.retrieveAllOfType(DefenseItem.class);
                int bonus = defenseItems.stream().map(DefenseItem::getDefense).reduce(0,Integer::sum);

            }*/

    @Override
    protected <T extends Item> ConflictData attackItem(T item, boolean attackerOrDefender) {
/*        if (Dungeon.promptForItem(item, this) ) {
            if (attackerOrDefender) {
                item.isDesired();
            } else {
                storeItem(item);
            }
        }*/
        return new ConflictData(0,0,false,item);
    }
}
