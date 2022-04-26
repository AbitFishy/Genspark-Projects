package com.mygdx.game.humanvsgoblin.game;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class EntityFactory {
    public EntityFactory(SpriteManager spriteManager){
        this.spriteManager = spriteManager;
    }

    HashMap<String,String>  sprites = new HashMap<>();
    SpriteManager spriteManager;

    public void registerEntity(String name, String spriteFilepath){
        sprites.put(name, spriteFilepath);
        spriteManager.setUnusedSprite(spriteFilepath);
    }

    public Entity getNewEntity(String name) {
        Entity entity = null;
        switch (name) {
            case "Player":
            case "Human":
                entity = new Human();
                break;
            case "Goblin":
                entity = new Goblin();
                break;
            case "Sword":
                entity = new Sword();
                break;
            case "PassableLand":
                entity = new PassableLand();
                break;
            default:
                entity = new EmptyItem();

        }
        entity.setSprite(sprites.get(name));
        return entity;
    }
}
