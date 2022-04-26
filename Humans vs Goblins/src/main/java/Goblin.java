public class Goblin extends LivingCreature{
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
}
