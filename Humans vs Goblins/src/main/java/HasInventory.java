public interface HasInventory {
    boolean storeItem(Item item);
    Item retrieveItem(String itemName);
}
