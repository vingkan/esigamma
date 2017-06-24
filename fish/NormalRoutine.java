import java.util.*;

public class NormalRoutine extends Routine {
    
    public NormalRoutine(){
        
    }
    
    public Location getNextLocation(Person person, City city){
        List<Location> locs = city.getLocations();
        GridLocation current = (GridLocation)person.getLocation();
        List<Location> adj = GridLocation.getAdjacentLocations(locs, current);
        return adj.get(0);
    }
    
}