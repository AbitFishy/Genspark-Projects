package com.mygdx.game.humanvsgoblin.game;

public class FakeDefenseItem implements  DefenseItem{

    protected int defense =0;
    protected String name;
    protected Coords coords = Coords.nullCoords;
    protected int movePriority =0;
    protected int weight =0;
    protected int size =0;
    protected int useTime =0;
    protected String sprite;

    @Override
    public String toString(){
        return String.valueOf(displayChar);
    }

    @Override
    public int getDefense() {
        return defense;
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
        return 0;
    }

    @Override
    public ConflictData conflict(Entity other, boolean attackerOrDefender) {
        return new ConflictData(0,0,false, this);
    }

    @Override
    public boolean isDead() {
        return true;
    }

    @Override
    public boolean isInanimateObject() {
        return true;
    }

    @Override
    public boolean isAlly(Entity other) {
        return false;
    }

    @Override
    public boolean isEnemy(Entity other) {
        return false;
    }

    @Override
    public boolean isBlocking(Entity other) {
        return false;
    }

    @Override
    public boolean canShareTileWith(Entity other) {
        return true;
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

    protected char displayChar = '.';

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getUseTime() {
        return useTime;
    }

    /**
     * Used by an HasInventory to indicate it desires the item during attack
     * it is up to the item to determine if it gives itself away
     * by using the .storeItem(this) during defense
     */
    @Override
    public void isDesired() {

    }

/*    @Override
    public String toJson() {
        return toJsonObj().toString();
    }

    @Override
    public void toJson(Writer writer) throws IOException {
        toJsonObj().toJson(writer);
    }
    protected JsonObject toJsonObj() {
        JsonObject jobj = new JsonObject();
        jobj.put("defense", defense);
        jobj.put("name", name);
        jobj.put("coords", coords);
        jobj.put("movePriority", movePriority);
        jobj.put("weight", weight);
        jobj.put("size", size);
        jobj.put("useTime", useTime);
        jobj.put("sprite", sprite);
        return jobj;
    }*/
}
