import java.util.*;

/*
 * F.I.S.H.
 * The Fictional Infection Simulation Host
 */
public class Main {

    public static void main(String[] args){
        
        City city = new City("Scise City");
        
        for(int x = 0; x < 5; x++){
            for(int y = 0; y < 5; y++){
                GridLocation home = new GridLocation("G" + x + "/" + y, x, y);
                for(int p = 0; p < 4; p++){
                    Routine routine = new NormalRoutine();
                    Person person = new Person(routine, home);
                    city.addPerson(person);
                }
                city.addLocation(home);
            }
        }
        
        Main.printCitySummary(city);
        Main.printLocationDetails(city);
        
        for(int i = 0; i < 2; i++){
            city.doTurn();
            Main.printLocationDetails(city);
        }
        
    }
    
    public static void printCitySummary(City city){
        int people = city.getPeople().size();
        int locations = city.getLocations().size();
        System.out.println(city.getName() + " Summary:");
        System.out.println(people + " people");
        System.out.println(locations + " locations");
    }

    public static void printLocationDetails(City city){
        HashMap<Location, List<Person>> map = city.getPeopleByLocation();
        System.out.println(city.getName() + " at Time: " + city.getTime());
        for(Map.Entry<Location, List<Person>> entry : map.entrySet()){
            Location lc = entry.getKey();
            System.out.println(lc.getName() + ": " + entry.getValue().size());
        }
    }

}