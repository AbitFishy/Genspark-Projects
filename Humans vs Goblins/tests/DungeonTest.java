import org.junit.Assert;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DungeonTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        Dungeon.testMode = true;
        Dungeon.testRandoms.clear();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void configGame() {
        Dungeon d = new Dungeon();
        assertThrowsExactly(RuntimeException.class, ()-> d.configGame("NoFile.txt"));
    }

    @org.junit.jupiter.api.Test
    void configGame2() {
        Dungeon d = new Dungeon();
        assertThrowsExactly(RuntimeException.class, ()-> d.configGame("game files/NoPlayer.txt"));
        d.world.move();
        assertEquals(new Goblin().getName(), d.world.entityAt(0,6).getName());
        assertEquals(new Goblin().getName(), d.world.entityAt(0,0).getName());
        assertEquals(new Goblin().getName(), d.world.entityAt(2,8).getName());
    }
    @org.junit.jupiter.api.Test
    void configGame3() {
        Dungeon d = new Dungeon();
        assertDoesNotThrow(()-> d.configGame());
        d.world.move();
        assertEquals(new Goblin().getName(), d.world.entityAt(0,0).getName());
        assertEquals(new Human().getName(), d.world.entityAt(5,5).getName());
    }


    @org.junit.jupiter.api.Test
    void play() {
        Dungeon.testRandoms.addAll(Arrays.asList(.253,.3942,.083,.401734,.61743,.0013,.09381,.19838,.00134,.2223,.003931,.3784,.9412,.53992,.98282,.4608523,
                .253,.3942,.083,.401734,.61743,.0013,.09381,.19838,.00134,.2223,.003931,.3784,.9412,.53992,.98282,.4608523,
                .253,.3942,.083,.401734,.61743,.0013,.09381,.19838,.00134,.2223,.003931,.3784,.9412,.53992,.98282,.4608523,
                .253,.3942,.083,.401734,.61743,.0013,.09381,.19838,.00134,.2223,.003931,.3784,.9412,.53992,.98282,.4608523,
                .253,.3942,.083,.401734,.61743,.0013,.09381,.19838,.00134,.2223,.003931,.3784,.9412,.53992,.98282,.4608523));
        String userInput = "s\ns\ns\ns\ns\nw\nw\nw\nw\nw\nw\nw\nw\ny\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader(new InputStreamReader(System.in));
        Dungeon.testMode = true;
        Dungeon d = new Dungeon();
        d.configGame();
        d.play();
        assertTrue(d.win);
        assertNotNull(d.player.retrieveItem("Sword"));
    }

    @org.junit.jupiter.api.Test
    void play2() {
        Dungeon.testRandoms.addAll(Arrays.asList(.253,.3942,.083,.401734,.61743,.0013,.09381,.19838,.00134,.2223,.003931,.3784,.9412,.53992,.98282,.4608523,
                .253,.3942,.083,.401734,.61743,.0013,.09381,.19838,.00134,.2223,.003931,.3784,.9412,.53992,.98282,.4608523,
                .253,.3942,.083,.401734,.61743,.0013,.09381,.19838,.00134,.2223,.003931,.3784,.9412,.53992,.98282,.4608523,
                .253,.3942,.083,.401734,.61743,.0013,.09381,.19838,.00134,.2223,.003931,.3784,.9412,.53992,.98282,.4608523,
                .253,.3942,.083,.401734,.61743,.0013,.09381,.19838,.00134,.2223,.003931,.3784,.9412,.53992,.98282,.4608523));
        String userInput = "s\ns\ns\ns\ns\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\nw\ny\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader(new InputStreamReader(System.in));
        Dungeon.testMode = true;
        Dungeon d = new Dungeon();
        d.configGame();
        var goblin = d.enemies.get(0);
        goblin.health = 10000;
        goblin.attack = 60;
        goblin.defense = 1000;
        d.play();
        assertFalse(d.win);
        assertNull(d.player.retrieveItem("Sword"));
    }

    @org.junit.jupiter.api.Test
    void getCrit() {
        Dungeon.testRandoms.add(.5);
        assertEquals(2,Dungeon.getCrit(1,.7f));
    }
    @org.junit.jupiter.api.Test
    void getCrit2() {
        Dungeon.testRandoms.add(.5);
        assertEquals(1,Dungeon.getCrit(1,.3f));
    }

    @org.junit.jupiter.api.Test
    void getDrop() {
        Entity goblin = new Goblin();
        Item sword = new Sword();
        Dungeon.testRandoms.add(.39);
        assertEquals(sword.getName(), Dungeon.getDrop(goblin).getName());
    }
    @org.junit.jupiter.api.Test
    void getDrop2() {
        Item sword = new Sword();
        assertNull(Dungeon.getDrop(sword));
    }

    @org.junit.jupiter.api.Test
    void calcDamageTaken() {
        Dungeon.testRandoms.add(2./41f);
        assertEquals (0, Dungeon.calcDamageTaken(40,40));

        Dungeon.testRandoms.add(.6/41.);
        Dungeon.testRandoms.add(.25);
        assertEquals(10, Dungeon.calcDamageTaken(40,40));

        Dungeon.testRandoms.add(.5);
        assertEquals(1, Dungeon.calcDamageTaken(41,40) );

        Dungeon.testRandoms.add(41.);
        Dungeon.testRandoms.add(40.);
        assertEquals(1,1);
    }


    @org.junit.jupiter.api.Test
    void getYesOrNo() {
        String userInput = "y\nn\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertTrue( Dungeon.getYesOrNo());
    }

    @org.junit.jupiter.api.Test
    void getYesOrNo2() {
        String userInput = "ufewa\nfda;hfa\ne8as;odfh\n`\n;\n\nz\n08\n..\n2qrlh\n5678\n9876t\nf;ahf\nq2ph3u;w\nfqp89whfsd\npf8hi;ofa\ny78puhw\no78guihl\nogyuil\ntoygilh\nfaeen\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertThrowsExactly(RuntimeException.class, () -> Dungeon.getYesOrNo());
    }

    @org.junit.jupiter.api.Test
    void getYesOrNo3() {
        String userInput = "ufewa\nfda;hfa\ne8as;odfh\n`\n;\n\nz\n08\n..\n2qrlh\n5678\n9876t\nf;ahf\nq2ph3u;w\nfqp89whfsd\npf8hi\n;ofa\ny78p\nuhw\no78guihl\nNo\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertFalse(Dungeon.getYesOrNo());
    }

    @org.junit.jupiter.api.Test
    void getNSWE() {
        String userInput = "ufewa\nfda;hfa\ne8as;odfh\n`\n;\n\nz\n08\n..\n2qrlh\n5678\n9876t\nf;ahf\nq2ph3u;w\nfqp89whfsd\npf8hi\n;ofa\ny78p\nuhw\no78guihl\nN\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertEquals(Dungeon.cardinalDirections.NORTH, Dungeon.getNSWE("Where to go?", true, true, true, true));
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertEquals(Dungeon.cardinalDirections.NORTH, Dungeon.getNSWE("Where to go?", true, false, true, true));
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertEquals(Dungeon.cardinalDirections.NORTH, Dungeon.getNSWE("Where to go?", true, true, false, true));
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertEquals(Dungeon.cardinalDirections.NORTH, Dungeon.getNSWE("Where to go?", true, true, true, false));
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertEquals(Dungeon.cardinalDirections.NORTH, Dungeon.getNSWE("Where to go?", true, false, false, true));
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertEquals(Dungeon.cardinalDirections.NORTH, Dungeon.getNSWE("Where to go?", true, false, true, false));
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertEquals(Dungeon.cardinalDirections.NORTH, Dungeon.getNSWE("Where to go?", true, true, false, false));
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertEquals(Dungeon.cardinalDirections.NORTH, Dungeon.getNSWE("Where to go?", true, false, false, false));
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertThrowsExactly(RuntimeException.class, () -> Dungeon.getNSWE("Where to go?", false, true, true, true));
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertThrowsExactly(RuntimeException.class, () -> Dungeon.getNSWE("Where to go?", false, false, false, false));
        in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        Dungeon.br = new BufferedReader( new InputStreamReader(System.in));
        assertNotEquals(Dungeon.cardinalDirections.SOUTH, Dungeon.getNSWE("Where to go?", true, false, false, false));
    }

}