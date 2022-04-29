package com.mygdx.game.humanvsgoblin.game;

public interface HasInventory {
    void storeItem(Item item);
    Item retrieveItem(String itemName);
}
