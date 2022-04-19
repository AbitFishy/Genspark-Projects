

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.humanvsgoblin.game.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {
        Json json = new Json();
        HashMap<String, ArrayList<Entity>> hm = new HashMap<>();
        Sprite sprite;

        var tex = new Texture("mapTile_022.png");
        ArrayList<Entity> grounds = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                sprite = new Sprite(tex);
                PassableLand pl = new PassableLand(sprite, new Coords(i,j));
                grounds.add(pl);
            }
        }

        ArrayList<Entity> players = new ArrayList<>();
        var player = new Human(150,10, 10, 1, "Player",
                new Coords(5,5),100,100, 1.5f,
        1.5f, .09f, 1, 1);
        player.setSprite(new Sprite(new Texture("knight.png")));
        players.add(player);

        ArrayList<Entity> enemies = new ArrayList<>();
        Goblin goblin = new Goblin(50, 10, 13, 1 , "Goblin",
                new Coords(0,0), 2, 30, 1.5f,
                1.5f,.11f, 2, 2);
        goblin.setSprite(new Sprite(new Texture("goblin.png")));
        enemies.add(goblin);

        hm.put("player", players);
        hm.put("enemies", enemies);
        hm.put("ground", grounds);
        String j = json.toJson(hm);
        var fh = Gdx.files.getFileHandle("config.json", Files.FileType.Internal);
        var str = fh.write(false, j.length());
        str.write(j.getBytes(StandardCharsets.UTF_8));
    }
}
