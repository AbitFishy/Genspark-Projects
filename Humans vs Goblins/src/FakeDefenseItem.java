public class FakeDefenseItem implements  DefenseItem{

    int defense =0;
    String name;
    Coords coords;
    int movePriority =0;
    int weight =0;
    int size =0;
    int useTime =0;

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
    public void conflict(Entity other, boolean attackerOrDefender) {

    }

    @Override
    public boolean isDead() {
        return true;
    }

    @Override
    public char getDisplayName() {
        return displayChar;
    }

    char displayChar = '.';

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
}
