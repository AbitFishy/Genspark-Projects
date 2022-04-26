public class LivingCreature implements Creature, HasInventory{

    LivingCreature(){
        this(0,0,0,0,"",new Coords(-1,-1),-1);
    }
    LivingCreature(int health, int attack, int defense,
        int moveSpeed, String name, Coords coords, int movePriority){

        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.moveSpeed = moveSpeed;
        this.movePriority = movePriority;
        this.name = name;
        this.coords = coords;
        inventory = new Inventory(10);

        if (name.length() > 1){
            displayChar = name.charAt(0);
        }
        else{
            displayChar = 'ⱘ';
        }
    }

    int health;
    int attack;
    int defense;
    String name;
    Coords coords;
    int movePriority;
    Inventory inventory;
    float attackBonus = 1.5f;


    int moveSpeed = 0;
    float critDam = 1.5f;
    float critRate= .09f;

    @Override
    public String toString(){
        return String.valueOf(displayChar);
    }

    @Override
    public int getHealth() {
        return health;
    }

    /**
     * @param damage how much damage the creature is given
     * @return how much damage the creature actually took
     */
    @Override
    public int receiveDamage(int damage) {
        int totDef = getDefenseBonus() + defense;

        int damageTaken = Dungeon.calcDamageTaken(damage, totDef);
        health -= damageTaken;
        return damageTaken;
    }

    @Override
    public void receiveHealth(int health) {
        this.health += health;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public int getMoveSpeed() {
        return moveSpeed;
    }

    @Override
    public float getCritDam() {
        return critDam;
    }

    @Override
    public float getCritRate() {
        return critRate;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Coords getCoords() {
        return coords;
    }

    @Override
    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    @Override
    public int getMovePriority() {
        return movePriority;
    }

    @Override
    public void conflict(Entity other, boolean attackerOrDefender) {
        if (other instanceof LivingCreature){
            attackLivingCreature((LivingCreature)other, attackerOrDefender);
        }
        if (other instanceof Item){
            attackItem((Item)other, attackerOrDefender);
        }
    }

    @Override
    public boolean isDead() {
        return health <= 0;
    }

    @Override
    public char getDisplayName() {
        return displayChar;
    }

    protected char displayChar;

    @Override
    public boolean storeItem(Item item) {
        return inventory.addItem(item);
    }

    @Override
    public Item retrieveItem(String itemName) {
        return inventory.retrieveItem(itemName);
    }

    protected <T extends LivingCreature>  void attackLivingCreature(T g, boolean attackerOrDefender){
        int damageOutput = 0;
        int receivedDamage = 0;
        int bonus = getAttackBonus();
        if (attackerOrDefender) {
            damageOutput = (int) getDamage(bonus, attackBonus, critDam, critRate);
        }
        else{
       // if (!attackerOrDefender){
            damageOutput = (int)(getDamage(bonus, 1, critDam/2, critRate/2));

        }
        receivedDamage  = g.receiveDamage(damageOutput);
        System.out.println(name + (!attackerOrDefender ? " counter" : " " ) + "attacks for " + damageOutput + " points of damage");
        System.out.println(g.getName() + " defends and takes " + receivedDamage + " points of damage");
    }

    float getDamage(int bonus, float attackBonus, float critDam, float critRate) {
        return (attack + bonus) * attackBonus * Dungeon.getCrit(critDam, critRate);
    }

    int getAttackBonus() {
        var attackItems = inventory.retrieveAllOfType(AttackItem.class);
        int bonus = attackItems.stream().map(AttackItem::getAttack).reduce(Integer::sum).orElse(0);
        return bonus;
    }

    int getDefenseBonus() {
        var defenseItems = inventory.retrieveAllOfType(DefenseItem.class);
        int bonus = defenseItems.stream().map(DefenseItem::getDefense).reduce(Integer::sum).orElse(0);
        return bonus;
    }

    protected <T extends Item> void attackItem(T item, boolean attackerOrDefender){
        //if (Dungeon.promptForItem(item, this) ){
        if (attackerOrDefender) {
            item.isDesired();
        }
        //}
        else{
            storeItem(item);
        }
    }

}
