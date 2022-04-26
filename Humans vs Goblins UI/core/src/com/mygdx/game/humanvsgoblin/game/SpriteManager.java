package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;

public class SpriteManager {

    public SpriteManager(){
        defaultSprite = "question.png";
        textures.put(defaultSprite, new Texture(defaultSprite));
        sprites.put(defaultSprite, new Sprite(textures.get(defaultSprite)));
    }

    protected HashMap<String, Sprite> sprites = new HashMap<>();
    protected HashMap<String, Texture> textures = new HashMap<>();
    //TODO textures to become regions of sprite sheet
    protected String defaultSprite = "";
    protected int count = 0;
    /**
     * @param filename filename of texture
     * @return a unique string built from the filename and an identifier
     */
    public String setSpriteFromFilename(final String filename){
        String id = filename + "::" + Integer.toHexString(count++);
        if (!textures.containsKey(filename)) {
            textures.put(filename, new Texture(filename));
            sprites.put(filename, new Sprite(textures.get(filename)));
        }
        sprites.put(id, new Sprite(textures.get(filename)));
        return id;
    }

    public Sprite retrieveSprite(String strSprite){
        var s = sprites.get(strSprite);
        if (s == null){
            s = sprites.get(defaultSprite);
        }
        return s;
    }

    public void setUnusedSprite(String spriteFilepath) {
        if (!textures.containsKey(spriteFilepath)) {
            textures.put(spriteFilepath, new Texture(spriteFilepath));
            sprites.put(spriteFilepath, new Sprite(textures.get(spriteFilepath)));
        }
    }
}
