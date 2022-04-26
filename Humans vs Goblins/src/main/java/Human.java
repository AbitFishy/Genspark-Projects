public class Human extends LivingCreature {
    Human() {
        this(50, 10, 10, 1, "Player",0, 0, 100);
    }
    Human(int x, int y){
        this(50, 10, 10, 1, "Player", x, y, 100);
    }
    Human(int health, int attack, int defense, int moveSpeed, String name, int x, int y, int movePriority){
        super(health,attack,defense,moveSpeed,name, new Coords(x,y), movePriority);
        displayChar = '☃';
    }
           /* if (!attackerOrDefender){
                var defenseItems = inventory.retrieveAllOfType(DefenseItem.class);
                int bonus = defenseItems.stream().map(DefenseItem::getDefense).reduce(0,Integer::sum);

            }*/

    @Override
    protected <T extends Item> void attackItem(T item, boolean attackerOrDefender) {
        if (Dungeon.promptForItem(item, this) ){
            if (attackerOrDefender) {
                item.isDesired();
            }
            else{
                storeItem(item);
            }
        }
    }
}
