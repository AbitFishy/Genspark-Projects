import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void drawGrid() {
        World w = new World(10,10);
        for (int j = 0; j < 10; j++){
            for (int i = 0; i < 10; i++){
                w.set(i,j,new Goblin());
            }
        }
        assertEquals("❡❡❡❡❡❡❡❡❡❡\n" +
                "❡❡❡❡❡❡❡❡❡❡\n" +
                "❡❡❡❡❡❡❡❡❡❡\n" +
                "❡❡❡❡❡❡❡❡❡❡\n" +
                "❡❡❡❡❡❡❡❡❡❡\n" +
                "❡❡❡❡❡❡❡❡❡❡\n" +
                "❡❡❡❡❡❡❡❡❡❡\n" +
                "❡❡❡❡❡❡❡❡❡❡\n" +
                "❡❡❡❡❡❡❡❡❡❡\n" +
                "❡❡❡❡❡❡❡❡❡❡\n", w.drawGrid());
    }
    @Test
    void drawGrid2() {
        World w = new World(10,10);
        for (int j = 0; j < 10; j++){
            for (int i = 0; i < 10; i++){
                if (i == j){
                    w.set(i,j,new Human());
                }else {
                    w.set(i, j, new Goblin());
                }
            }
        }
        assertEquals(
                "❡❡❡❡❡❡❡❡❡☃\n" +
                        "❡❡❡❡❡❡❡❡☃❡\n" +
                        "❡❡❡❡❡❡❡☃❡❡\n" +
                        "❡❡❡❡❡❡☃❡❡❡\n" +
                        "❡❡❡❡❡☃❡❡❡❡\n" +
                        "❡❡❡❡☃❡❡❡❡❡\n" +
                        "❡❡❡☃❡❡❡❡❡❡\n" +
                        "❡❡☃❡❡❡❡❡❡❡\n" +
                        "❡☃❡❡❡❡❡❡❡❡\n" +
                        "☃❡❡❡❡❡❡❡❡❡\n", w.drawGrid());
    }


    @Test
    void move() {
         ArrayList<Double> rand = (ArrayList<Double>) Arrays.stream(".38\n.8641\n.1685\n.4835\n.8641\n.38\n.8641\n.1685\n.4835\n.8641\n.38\n.8641\n.1685\n.4835\n.8641\n.38\n.8641\n.1685\n.4835\n.8641\n"
                 .split("\n")).map(Double::valueOf).collect(Collectors.toList());

        World w = new World(10,10);
        Entity gb = new Goblin(20, 30, 30, 1, "", 3,3, 40);
        w.addEntity(gb, gb.getCoords());
        LivingCreature lc = new LivingCreature(40,30,30,1,"",new Coords(4,4), 50);
        w.addEntity(lc, lc.getCoords());
        Dungeon.testMode = true;
        Dungeon.testRandoms.addAll(rand);
        w.move();
        w.moveEntity(lc.getCoords(),gb.getCoords());
        w.move();

        assertEquals( gb.getName(), w.entityAt(3,3).getName());
        assertNull( w.entityAt(4,4));
        assertNotNull(lc.retrieveItem("Sword"));

    }
}