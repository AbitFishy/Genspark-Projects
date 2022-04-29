package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {
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
    void move() {
        String fileName = "config.json";
        Dungeon dg = new Dungeon();
        var queue = new LinkedList<Double>();
        for (int i = 10; i >= 0; i--) {
            queue.add(.25);
        }
        Dungeon.testRandoms = queue;
        dg.configGame(fileName);
        dg.infoBox = new EmptyInfoBox();

        Goblin goblin = assertDoesNotThrow( ()-> (Goblin)dg.getEnemies().get(0));
        int health = goblin.getHealth();

        DecideMove dm = new DecideMove(dg,dg.player);
        dm.newMove(dg.player.getCoords());
        dm.continueMove(Dungeon.cardinalDirections.NORTH);
        dm.highlightTarget(Dungeon.cardinalDirections.NORTH);

        var path = dm.getPath();
        Coords lastCoord = dg.player.getCoords();
        for (var c : path){
            dg.world.moveEntity(dg.player, lastCoord, c, World.MoveOrAttack.MOVE);
            lastCoord = c;
        }
        if (dm.targetPosition != Coords.nullCoords) {
            dg.world.moveEntity(dg.player, lastCoord, dm.targetPosition, World.MoveOrAttack.ATTACK);
        }

        var targetPos = dm.targetPosition;

        dg.world.move();

        while (dg.hasNextAction()) {
            dg.showNextAction();
        }

        assertTrue(dg.world.get(5,6).contains(dg.player));
        assertNotEquals( health, dg.enemies.get(0).health);

        dg.resetActions();
        dm.newMove(lastCoord);

        goblin.receiveDamage(100000); //to ensure the goblin is dead
        dg.player.receiveHealth(100000); //The player otherwise dies this round, whoops
        dg.world.moveEntity(dg.player, lastCoord, targetPos, World.MoveOrAttack.ATTACK);
        dg.world.move();

        dg.hasNextAction();
        dg.showNextAction();
        dg.acceptItem();
        assertEquals("Sword", dg.getPlayer().retrieveItem("Sword").getName());

        assertFalse(dg.world.get(goblin.getCoords()).contains(goblin));

    }
}