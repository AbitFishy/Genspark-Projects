package com.mygdx.game.humanvsgoblin.game;

import java.util.ArrayList;

public class Actions {
    public Entity entity;
    public Coords origin;
    public Coords destination;
    public World.MoveOrAttack moa;

    public boolean moveSuccess = false;

    public boolean fightOccurred = false;
    public ArrayList<Fights> fightInfo = new ArrayList<>();
    public boolean moveBlocked = false;
    public Entity moveBlockedBy = null;

    public boolean pickupItemOpportunity = false;
    public Item itemToPickUp = null;

    Actions(Entity entity, Coords origin, Coords destination, World.MoveOrAttack moa){
        this.entity = entity;
        this.origin = origin;
        this.destination = destination;
        this.moa = moa;
    }
}
