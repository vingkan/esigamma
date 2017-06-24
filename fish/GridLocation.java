import java.util.*;

public class GridLocation extends Location {
    
    private String name = "Home";
    private int x;
    private int y;

    public GridLocation(String name, int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    public boolean isAdjacentTo(GridLocation loc){
        int diffX = Math.abs(loc.getX() - this.getX());
        int diffY = Math.abs(loc.getY() - this.getY());
        return diffX <= 1 && diffY <= 1;
    }
    
    public static List<Location> getAdjacentLocations(List<Location> list, GridLocation loc){
        List<Location> res = new ArrayList<Location>();
        for(Location lc : list){
            if(lc instanceof GridLocation){
                if(loc.isAdjacentTo((GridLocation)lc)){
                    res.add(lc);
                }
            }
        }
        return res;
    }
    
    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public String getName(){
        return this.name;
    }

}