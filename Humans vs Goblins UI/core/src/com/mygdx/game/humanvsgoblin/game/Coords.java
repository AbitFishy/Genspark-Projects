package com.mygdx.game.humanvsgoblin.game;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

public class Coords implements Comparable<Coords> {
    public Coords() {
        x = y = z = 0;
    }

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Coords(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coords deepCopy() {
        return new Coords(x, y, z);
    }

    protected int x, y, z;
    public static final Coords nullCoords = new Coords(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

    @Override
    public int compareTo(@NotNull final Coords other) {
            if (this.x == other.x){
                if (this.y == other.y){
                    if (this.z == other.z){
                        return 0;
                    }
                    return Integer.compare(this.z,other.z);
                }
                return Integer.compare(this.y, other.y);
            }
            return Integer.compare(this.x, other.x);

    }

    @Override
    public boolean equals(Object object){
        if (object == this) {
            return true;
        }
        if (!(object instanceof Coords)) {
            return false;
        }
        Coords c = (Coords) object;
        return x == c.x
                && y == c.y
                && z == c.z;
    }

    @Override
    public int hashCode() {
        // return current object's id as hashCode
        return (x+1)*(y+10)*(100+z);
    }
/*    @Override
    public String toJson() {
        return null;
    }

    @Override
    public void toJson(Writer writer) throws IOException {

    }
    protected JsonObject toJsonObj() {
        JsonObject jobj = new JsonObject();
        jobj.put("x",x);
        jobj.put("y",y);
        jobj.put("z",z);
        return jobj;
    }*/
}

