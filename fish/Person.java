public class Person {
    
    Routine routine;
    Location location;
    
    public Person(Routine routine, Location location){
        this.routine = routine;
        this.location = location;
    }
    
    public void doTurn(City city){
        Location loc = routine.getNextLocation(this, city);
        this.location = loc;
    }
    
    public void setRoutine(Routine routine){
        this.routine = routine;
    }

    public void setLocation(Location loc){
        this.location = loc;
    }
    
    public Location getLocation(){
        return this.location;
    }
    
}