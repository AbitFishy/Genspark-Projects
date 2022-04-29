package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EmptyInfoBox extends InfoBox{
    public EmptyInfoBox() {
        super(new SpriteManager());
    }
    @Override
    public void setText(String text) {

    }
    @Override
    public void draw(SpriteBatch batch){

    }
}
