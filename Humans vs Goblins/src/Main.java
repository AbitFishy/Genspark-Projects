import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Dungeon dg=  new Dungeon();
        dg.configGame();
        dg.play();
    }


}

class Pair<First,Second>{
    Pair(First one, Second two){
        first= one;
        second = two;
    }
    public First first;
    public Second second;
}