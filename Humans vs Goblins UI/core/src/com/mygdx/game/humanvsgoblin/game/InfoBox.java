package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.graphics.Color.RED;

public class InfoBox {
    //Viewport viewport;
    //Stage stage;

    float xScale = 1.f;
    float yScale = 1.f;

    float xOffset = 0;
    float yOffset = 640;

    float height = 60;
    float width = 640;
    BitmapFont font = new BitmapFont();
    String displayText = "";
    SpriteManager sm = null;
    String sprite = "blueoverlay.png";

    String cursorSprite = "";
    String selectionSprite = "";
    String backgroundSprite = "blank.png";
    //Table table;
    Label label;

/*    SpriteDrawable cursorDraw = null;
    SpriteDrawable selectDraw = null;
    SpriteDrawable backgrDraw = null;*/
    TextArea textArea = null;

    //Texture texture;
    //Pixmap pixmap;

    public InfoBox(SpriteBatch batch){

        //pixmap = new Pixmap((int)(width*xScale), (int)(height*yScale), Pixmap.Format.RGBA8888);
        //pixmap.setColor(255,255,255,0);
        //pixmap.fillRectangle(0, 0, (int)(width*xScale), (int)(height*yScale));
        //texture = new Texture(pixmap);

    }

    public InfoBox(SpriteManager spriteManager, SpriteBatch batch){
        //viewport = new FitViewport(640,640,new OrthographicCamera());
        //stage = new Stage(viewport, batch);
/*        table = new Table();
        table.top();
        table.setX(0,700);*/

        sm = spriteManager;
        this.cursorSprite = spriteManager.setSpriteFromFilename(sprite);
        this.selectionSprite = sm.setSpriteFromFilename(sprite);
        this.backgroundSprite = sm.setSpriteFromFilename(backgroundSprite);

        label = new Label("", new Label.LabelStyle(font, RED));
        label.setPosition(0,690);

        textArea = new TextArea("", new TextField.TextFieldStyle(font, RED, new SpriteDrawable(sm.retrieveSprite(cursorSprite)),
                new SpriteDrawable(sm.retrieveSprite(selectionSprite)), new SpriteDrawable(sm.retrieveSprite(backgroundSprite))));

        textArea.setPosition(0,640);
        textArea.setSize(width*xScale, height * yScale);
        textArea.setVisible(true);
        textArea.setTouchable(Touchable.disabled);
        textArea.setLayoutEnabled(false);


    }

    public void setText(String text) {

        displayText = text;
        textArea.setText(displayText);
        //label.setText(displayText);
        //table.add(label);
    }

    public void draw(SpriteBatch batch){
        //label.draw(batch, 1);
        //table.draw(batch, 0.f);
        textArea.draw(batch,1);
        //batch.draw(texture,xOffset,yOffset+height);
        //font.setColor(0,0,50,0);
       //pixmap.drawPixmap(pixmap, (int)xOffset, (int)(yOffset+height));
        //font.draw(batch, displayText, xOffset , yOffset + height/2);
    }
}
