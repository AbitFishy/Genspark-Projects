public interface Item extends Entity {
    int getWeight();
    int getSize();
    int getUseTime();


    /**
     * Used by an HasInventory to indicate it desires the item during attack
     * it is up to the item to determine if it gives itself away
     * by using the .storeItem(this) during defense
     */
    void isDesired();
}
