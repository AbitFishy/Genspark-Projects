package com.mygdx.game.humanvsgoblin.game;


import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Inventory {
    Inventory(){
        this(10);
    }
    Inventory(int size){
        this.size=  size;
        stuff = new ArrayList<>(size);
    }
    int size;
    ArrayList<Item> stuff;

    boolean addItem(Item item){
        if (stuff.size() >= size){
            return false;
        }
        else{
            stuff.add(item);
            return true;
        }
    }

    <T extends Item> ArrayList<T> retrieveAllOfType(Class<T> itemClass){
        return stuff.stream().filter(i -> itemClass.isAssignableFrom(i.getClass())).map(s -> (T) s).collect(Collectors.toCollection(ArrayList::new));
        //return (ArrayList<T>) new ArrayList<T> (a);
                //var a = stuff.stream().filter(i -> itemClass.isAssignableFrom(i.getClass())).collect(Collectors.toList());
        //return (ArrayList<T>) new ArrayList<T> (a);
    }

    Item retrieveItem(String name){
        Item ret = null;
        var iter = stuff.listIterator();
        while (iter.hasNext()){
            var temp = iter.next();
            if (Objects.equals(temp.getName(), name)){
                iter.remove();
                ret = temp;
                iter = stuff.listIterator(stuff.size());
            }
        }
        return ret;
    }
/*
    @Override
    public String toJson() {
        return toJsonObj().toString();
    }

    @Override
    public void toJson(Writer writer) throws IOException {
        toJsonObj().toJson(writer);
    }
    protected JsonObject toJsonObj() {
        JsonObject jobj = new JsonObject();
        jobj.put("size",size);
        for (var item : stuff){
            jobj.put("item", item.toJson());
        }
        return jobj;
    }*/
}

