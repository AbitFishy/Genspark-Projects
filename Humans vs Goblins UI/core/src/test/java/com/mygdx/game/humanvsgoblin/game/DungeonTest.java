package com.mygdx.game.humanvsgoblin.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DungeonTest {

    HeadlessApplication ha;

    @BeforeEach
    void setUp() {
        ha = new HeadlessApplication(new ApplicationAdapter(){        });
        Gdx.gl = Gdx.gl20 = Mockito.mock(GL20.class);
        Dungeon.testMode = true;
        Dungeon.testRandoms.clear();
    }

    @AfterEach
    void tearDown() {
        ha.exit();
        ha = null;
    }

    @Test
    void makeConfigFile() {
        String fileName = "configTest.json";
        assertDoesNotThrow(() -> Files.deleteIfExists(Path.of(fileName)));

        Dungeon dg = new Dungeon();
        dg.makeConfigFile(fileName);
        dg.configGame(fileName);

        assertEquals(150, dg.getPlayer().getHealth());
        assertEquals(new Coords(5,5), dg.getPlayer().getCoords());

        assertEquals(1, dg.getEnemies().size());
        assertEquals(new Coords(5,6), dg.getEnemies().get(0).getCoords());

        assertEquals(108, dg.getSpriteManager().getSprites().size());

        assertDoesNotThrow(() -> Files.deleteIfExists(Path.of(fileName)));
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
}