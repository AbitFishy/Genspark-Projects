package com.mygdx.game.humanvsgoblin.game;

public interface HasInventory {
    boolean storeItem(Item item);
    Item retrieveItem(String itemName);
}
