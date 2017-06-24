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
        for(Person p : people){
            p.doTurn(this);
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
    
    public int getTime(){
        return this.time;
    }
    
    public String getName(){
        return this.name;
    }
}