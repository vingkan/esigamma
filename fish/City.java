import java.util.*;

public class City {
    
    private int time = 1;
    private String name;
    private List<Location> locations;
    private List<Person> people;
    
    public City(String name){
        this.name = name;
        this.locations = new ArrayList<Location>();
        this.people = new ArrayList<Person>();
    }
    
    public void doTurn(){
        for(Person p : this.getPeople()){
            p.doTurn(this);
        }
        HashMap<Location, List<Person>> map = city.getPeopleByLocation();
        for(Map.Entry<Location, List<Person>> entry : map.entrySet()){
            Location loc = entry.getKey();
            List<Person> people = entry.getValue();
        }
        this.time++;
    }
    
    public void addLocation(Location loc){
        this.locations.add(loc);
    }
    
    public List<Location> getLocations(){
        return this.locations;
    }
    
    public void addPerson(Person person){
        this.people.add(person);
    }
    
    public List<Person> getPeople(){
        return this.people;
    }
    
    public HashMap<Location, List<Person>> getPeopleByLocation(){
        HashMap<Location, List<Person>> map = new HashMap<Location, List<Person>>();
        for(Person person : this.getPeople()){
            Location loc = person.getLocation();
            if(map.containsKey(loc)){
                map.get(loc).add(person);
            }
            else{
                List<Person> list = new ArrayList<Person>();
                list.add(person);
                map.put(loc, list);
            }
        }
        return map;
    }
    
    public int getTime(){
        return this.time;
    }
    
    public String getName(){
        return this.name;
    }
}