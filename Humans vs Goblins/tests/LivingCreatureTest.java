import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class LivingCreatureTest {

    LivingCreature lc = new LivingCreature(30,25, 10, 2, "Test Creature", new Coords(3,8),4);
    @BeforeEach
    void setUp() {
        Dungeon.testMode = true;
        Dungeon.testRandoms.clear();
        lc.inventory = new Inventory();

    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void attackLivingCreature() {
        Dungeon.testRandoms.add(.5);
        Dungeon.testRandoms.add(0.);
        LivingCreature defender =  new LivingCreature();
        defender.receiveHealth(40);
        defender.critRate = 0.f;
        defender.critDam = 1f;
        lc.attackLivingCreature(defender, true);
        assertEquals(3,defender.getHealth());
    }

    @Test
    void attackLivingCreature2() {
        Dungeon.testRandoms.add(.5);
        Dungeon.testRandoms.add(0.03);
        Dungeon.testRandoms.add(.4);
        LivingCreature defender =  new LivingCreature(0,20,10,1,"Boo",new Coords(1,1),100);
        defender.receiveHealth(40);
        lc.attackLivingCreature(defender, true);
        assertEquals(13,defender.getHealth());
    }

    @Test
    void attackLivingCreature3() {
        Dungeon.testRandoms.add(.03);
        Dungeon.testRandoms.add(0.823);
        Dungeon.testRandoms.add(.4);
        LivingCreature defender =  new LivingCreature(0,20,10,1,"Boo",new Coords(1,1),100);
        defender.receiveHealth(40);
        defender.attackLivingCreature(lc, false); //counter attack
        assertEquals(-15,lc.getHealth());
    }

    @Test
    void getAttackBonus1() {
        assertEquals(0, lc.getAttackBonus());
    }

    @Test
    void getAttackBonus2() {
        lc.inventory.addItem(new Sword(10));
        assertEquals(10, lc.getAttackBonus());
    }
    @Test
    void getAttackBonus3() {
        lc.inventory.addItem(new Shield(10));
        assertEquals(0,lc.getAttackBonus());
    }
    @Test
    void getAttackBonus4() {
        lc.inventory.addItem(new Sword(10));
        assertEquals(10, lc.getAttackBonus());
        lc.inventory.addItem(new Shield(10));
        assertEquals(10,lc.getAttackBonus());
    }
    @Test
    void getAttackBonus5() {
        lc.inventory.addItem(new Sword(10));
        lc.inventory.addItem(new Shield(10));
        lc.inventory.addItem(new Sword(10));
        lc.inventory.addItem(new Sword(30));
        assertEquals(50,lc.getAttackBonus());
    }

    @Test
    void getDefenseBonus() {
        assertEquals(0 ,lc.getDefenseBonus());

        lc.inventory.addItem(new Shield(10));
        assertEquals(10,lc.getDefenseBonus());
        lc.inventory.addItem(new Sword(10));
        assertEquals(10,lc.getDefenseBonus());
    }

    @Test
    void attackItem() {
        String userInput = "y\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Sword s = new Sword();
        lc.attackItem(s,true);
        s.conflict(lc,false);
        assertEquals(1, lc.inventory.retrieveAllOfType(AttackItem.class).size(),"Inventory missing sword s");
    }

}