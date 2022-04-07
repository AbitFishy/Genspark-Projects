import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

class HumanTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void attackItem() {
        LivingCreature lc = new Human();
        String userInput = "y\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.testMode = true;
        Dungeon.br = new BufferedReader(new InputStreamReader(in));
        Sword s = new Sword();
        lc.attackItem(s,true);
        s.conflict(lc,false);
        assertEquals(1, lc.inventory.retrieveAllOfType(AttackItem.class).size(),"Inventory missing sword s");

    }
    @Test
    void attackItem2() {
        LivingCreature lc = new Human();
        String userInput = "n\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.testMode = true;
        Dungeon.br = new BufferedReader(new InputStreamReader(in));
        Sword s = new Sword();
        lc.attackItem(s,true);
        s.conflict(lc,false);
        assertEquals(0, lc.inventory.retrieveAllOfType(AttackItem.class).size(),"Inventory shouldn't have sword s");

    }
}