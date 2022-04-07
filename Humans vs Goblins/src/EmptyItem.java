public class EmptyItem implements Item {
    @Override
    public String getName() {
        return "null";
    }

    @Override
    public String toString(){
        return String.valueOf(displayChar);
    }

    @Override
    public Coords getCoords() {
        return null;
    }

    @Override
    public void setCoords(Coords coords) {

    }

    @Override
    public int getMovePriority() {
        return 0;
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

    char displayChar = 'n';

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public int getUseTime() {
        return 0;
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
