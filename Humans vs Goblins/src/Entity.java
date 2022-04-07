public interface Entity {
    String getName();
    Coords getCoords();
    void setCoords(Coords coords);
    int getMovePriority();
    char symbol = '-';
    void conflict(Entity other, boolean attackerOrDefender);
    boolean isDead();

    char getDisplayName();

    /*
    Coords coords = new Coords(0,0);
    int movePriority = 0;
    String name = "";
    */



}
class Coords {
    Coords(){
        x = y = z = 0;
    }
    Coords(int x, int y){
        this.x = x;
        this.y = y;
        this.z = 0;
    }
    Coords(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Coords deepCopy(){
        return new Coords(x,y,z);
    }
    int x, y, z;
}