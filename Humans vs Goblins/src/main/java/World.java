import java.util.*;

public class World {


    final int x;
    final int y;

    World(int x, int y){
//        var sg = new Entity[x][];
 //       for (var e: sg)
//        {
 //           e = new Entity[y];
//        }

        grid = new Entity[x][y];
        toBeMoved = new ArrayList<>();
        this.x = x;
        this.y = y;
    }


    public String drawGrid(){
        StringBuilder sb = new StringBuilder();
        for (int j = y - 1; j >= 0; j--){
            for (int i = 0; i< x; i++){
                sb.append(Objects.nonNull(grid[i][j] ) ? grid[i][j].toString() : " ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    public Entity entityAt(int x, int y){
        return grid[x][y];
    }
    public void moveEntity(Coords fromCoords, Coords toCoords){
        if (fromCoords != null){
            toBeMoved.add(new Pair(get(fromCoords),toCoords));
        }

    }
    public void addEntity(Entity entity, Coords toCoords){
        if (entity != null){
            toBeMoved.add(new Pair(entity,toCoords));
        }
    }
    public boolean validSpace(Coords coords){
        return coords.x >= 0 && coords.x < x && coords.y >= 0 && coords.y < y;
    }
    public boolean validSpace(int vx, int vy){
        return validSpace(new Coords(vx,vy));
    }

    Entity[][] grid;
    ArrayList<Pair<Entity,Coords>> toBeMoved;
/*
        if ((grid[toX][toY] == null) ){
            if (grid[fromX][fromY] != null){
                grid[toX][toY] = grid[fromX][fromY];
                grid[fromX][fromY] = null;
                return true;
            }
        }
        else {
            if (grid[fromX][fromY] != null){
                Dungeon.Conflict(grid[fromX][fromY],grid[toX][toY]);
            }
        }
        return false;
    }
    */
    Entity get(Coords coords){
        return get(coords.x,coords.y);
    }
    Entity get(int x, int y){
        if (x >= 0 && x < this.x
        &&  y >= 0 && y < this.y)
        {
            return grid[x][y];
        }
        else{
            return null;
        }
    }
    void set(Coords coords, Entity entity){
        set(coords.x,coords.y ,entity);
        if (entity != null) {
            entity.setCoords(coords);
        }
    }
    void set(int x, int y, Entity entity){
        if (x >= 0 && x < this.x
                &&  y >= 0 && y < this.y)
        {
            grid[x][y] = entity;
        }
    }

    HashSet<Entity> move(){
        //toBeMoved.sort(Comparator.comparingInt(lhs -> lhs.first.getMovePriority()));

        toBeMoved.sort((lhs,rhs) -> {
            return -1 * Integer.compare(lhs.first.getMovePriority(), rhs.first.getMovePriority());
        });

        HashSet<Entity> dead = new HashSet<>();

        for (var iter = toBeMoved.listIterator(); iter.hasNext(); ){
            Pair<Entity, Coords> p = iter.next();
            iter.remove();
            Entity e1 = p.first;
            Entity e2 = get(p.second);
            if (dead.contains(e1)){
                continue;
            }
            if (dead.contains(e2)){
                e2 = null;
            }

            if (e2 == null){
                set(e1.getCoords().x,e1.getCoords().y, null);
                set(p.second,e1);
            }
            else{
                switch (Dungeon.Conflict(e1,e2)) {
                    case defenderDied :
                        set(p.second.x,p.second.y,e1);
                        Item drop = Dungeon.getDrop(get(e1.getCoords()));
                        set(e1.getCoords().x,e1.getCoords().y,null);
                        if (drop != null){
                            iter.add(new Pair(drop,p.second));
                            iter.previous();
                        }
                        dead.add(e2);
                        break;
                    case bothLived:
                        break;
                    case bothDied:
                        set(p.second, null);
                        dead.add(e1);
                        dead.add(e2);
                        //fallthrough
                    case attackerDied :
                        set(e1.getCoords(),null);
                        dead.add(e1);

                }
            }
        }
        return dead;
    }
}
