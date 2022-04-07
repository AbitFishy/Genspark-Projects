import java.util.ArrayList;
import java.util.Arrays;
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

                var a = stuff.stream().filter(i -> itemClass.isAssignableFrom(i.getClass())).collect(Collectors.toList());
        return (ArrayList<T>) new ArrayList<Item> (a);
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
}

