package com.mygdx.game.humanvsgoblin.game;

import java.util.ArrayList;

public class GameSaveFile {
    ArrayList<Entity> players = new ArrayList<>();
    ArrayList<Entity> enemies = new ArrayList<>();
    ArrayList<Entity> grounds = new ArrayList<>();
    ArrayList<Entity> items   = new ArrayList<>();

    void addPlayer(Entity e){
        players.add(e);
    }
    void addEnemy(Entity e){
        enemies.add(e);
    }
    void addGround(Entity e){
        grounds.add(e);
    }
    void addItem(Entity e){
        items.add(e);
    }

    public void setEnemies(ArrayList<Entity> enemies) {
        this.enemies = enemies;
    }

    public void setGrounds(ArrayList<Entity> grounds) {
        this.grounds = grounds;
    }

    public void setItems(ArrayList<Entity> items) {
        this.items = items;
    }

    public void setPlayers(ArrayList<Entity> players) {
        this.players = players;
    }

    public ArrayList<Entity> getPlayers() {
        return players;
    }

    public ArrayList<Entity> getEnemies() {
        return enemies;
    }

    public ArrayList<Entity> getGrounds() {
        return grounds;
    }

    public ArrayList<Entity> getItems() {
        return items;
    }

    public void setSpriteFilePaths(){
        findFileName(players);
        findFileName(enemies);
        findFileName(grounds);
        findFileName(items);
    }

    private void findFileName(ArrayList<Entity> entities) {
        entities.forEach(p -> {
            var s = p.getSprite();
            int end = s.indexOf("::");
            p.setSprite( s.substring(0,end));
        });
    }


}
