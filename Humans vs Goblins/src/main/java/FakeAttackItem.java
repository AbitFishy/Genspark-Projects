public class FakeAttackItem implements AttackItem {

    String name = "Sword Illusion";
    int attack = 0;
    int movePriority = 0;
    int weight =0;
    int size =0;
    int useTime =0;

    Coords coords = new Coords(-1,-1);


    @Override
    public String toString(){
        return String.valueOf(displayChar);
    }

    @Override
    public int getAttack() {
        return attack;
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
        if (!attackerOrDefender){
            if (desired){
                if (other instanceof HasInventory){
                    ((HasInventory) other).storeItem(this);
                    desired = false;
                }
            }
        }
    }

    @Override
    public boolean isDead() {
        return true;
    }

    @Override
    public char getDisplayName() {
        return displayChar;
    }

    char displayChar = '\'';

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
        desired = true;
    }
    boolean desired = false;
}
