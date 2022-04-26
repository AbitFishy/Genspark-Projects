package com.mygdx.game.humanvsgoblin.game;

public class LivingCreature implements Creature, HasInventory{



    public LivingCreature(){
        this(0,0,0,0,"",new Coords(-1,-1),-1);
    }
    public LivingCreature(int health, int attack, int defense,
        int moveDistance, String name, Coords coords, int movePriority){

        this.health = health;
        this.attack = attack;
        this.defense = defense;
        //this.moveSpeed = moveSpeed;
        this.moveDistance = moveDistance;
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

    protected int health;
    protected int attack;
    protected int defense;
    protected String name;
    protected Coords coords;
    protected int movePriority;
    protected Inventory inventory;
    protected float attackBonus = 1.5f;
    //protected Sprite sprite = new Sprite();
    protected String sprite;
    protected int moveDistance;
    protected int moveSpeed = 0;
    protected float critDam = 1.5f;
    protected float critRate= .09f;

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
    public int getMoveDistance() {
        return moveDistance;
    }

    @Override
    public ConflictData conflict(Entity other, boolean attackerOrDefender) {
        if (other instanceof LivingCreature){
            return attackLivingCreature((LivingCreature)other, attackerOrDefender);
        }
        if (other instanceof Item){
            return attackItem((Item)other, attackerOrDefender);
           //return new ConflictData(0,0,false);
        }
        return new ConflictData(0,0,false);
    }

    @Override
    public boolean isDead() {
        return health <= 0;
    }

    @Override
    public boolean isInanimateObject() {
        return false;
    }

    int allegiance;
    int enemiesWith;

    @Override
    public boolean isAlly(Entity other) {
        if (other instanceof LivingCreature){
            return  (allegiance | ((LivingCreature)other).allegiance) != 0;
        }
        return false;
    }

    @Override
    public boolean isEnemy(Entity other) {
        if (other instanceof LivingCreature){
            return  (enemiesWith | ((LivingCreature)other).enemiesWith) != 0;
        }
        return false;
    }

    @Override
    public boolean isBlocking(Entity other) {
         if (other instanceof LivingCreature){
             return !isAlly(other);
         }
         else {
             return true;
         }
    }

    @Override
    public boolean canShareTileWith(Entity other) {
        if (other instanceof LivingCreature){
            if (this.equals(other)){
                return true;
            }
            return false;
        }
         return !(other.isBlocking(this));

    }

    @Override
    public String getSprite() {
        return sprite;
    }

    @Override
    public void setSprite(String sprite) {
        this.sprite = sprite;
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

    protected <T extends LivingCreature>  ConflictData attackLivingCreature(T g, boolean attackerOrDefender){
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
        return new ConflictData(damageOutput,receivedDamage, true);
        //System.out.println(name + (!attackerOrDefender ? " counter" : " " ) + "attacks for " + damageOutput + " points of damage");
        //System.out.println(g.getName() + " defends and takes " + receivedDamage + " points of damage");
    }

    protected float getDamage(int bonus, float attackBonus, float critDam, float critRate) {
        return (attack + bonus) * attackBonus * Dungeon.getCrit(critDam, critRate);
    }

    protected int getAttackBonus() {
        var attackItems = inventory.retrieveAllOfType(AttackItem.class);
        int bonus = attackItems.stream().map(AttackItem::getAttack).reduce(Integer::sum).orElse(0);
        return bonus;
    }

    protected int getDefenseBonus() {
        var defenseItems = inventory.retrieveAllOfType(DefenseItem.class);
        int bonus = defenseItems.stream().map(DefenseItem::getDefense).reduce(Integer::sum).orElse(0);
        return bonus;
    }

    protected <T extends Item> ConflictData attackItem(T item, boolean attackerOrDefender){
        //if (Dungeon.promptForItem(item, this) ){
        var cd = new ConflictData(0,0, false);
        cd.item = item;
        if (attackerOrDefender) {
            item.isDesired();
        }
        //}
        else{
            storeItem(item);
        }
        return cd;
    }

/*    @Override
    public String toJson() {
        return toJsonObj().toString();
    }

    @Override
    public void toJson(Writer writer) throws IOException {
        toJsonObj().toJson(writer);
    }

    protected JsonObject toJsonObj(){
        JsonObject jobj = new JsonObject();
        jobj.put("displayChar",displayChar);
        jobj.put("allegiance", allegiance);
        jobj.put("enemiesWith",enemiesWith);
        jobj.put("health", health);
        jobj.put("attack", attack);
        jobj.put("defense", defense);
        jobj.put("name", name);
        jobj.put("coords", coords);
        jobj.put("movePriority", movePriority);
        jobj.put("inventory", inventory);
        jobj.put("attackBonus", attackBonus);
        jobj.put("sprite", sprite);
        jobj.put("moveDistance", moveDistance);
        jobj.put("moveSpeed", moveSpeed );
        jobj.put("critDam",  critDam) ;
        jobj.put("criteRate",  critRate);

        return jobj;
    }*/
}
