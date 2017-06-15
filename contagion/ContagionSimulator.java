import java.util.*;

public class ContagionSimulator {
    
    public static void main(String[] args) {
        System.out.println("Started");
        int turns = Integer.parseInt(args[0]);
        ContagionSimulator sim = new ContagionSimulator(10, 10);
        System.out.println("--- INITIAL ---");
        Actor[][] initGrid = sim.getCurrentGrid();
        ContagionSimulator.printGrid(initGrid);
        sim.simulateTurns(turns);
        System.out.println("--- HISTORY ---");
        int t = 0;
        List<Actor[][]> history = sim.getHistory();
        for(Actor[][] grid : history){
            ContagionSimulator.printGrid(grid);
            System.out.println("--- T = " + t + " ---");
            t++;
        }
        System.out.println("--- LOG ---");
        ContagionSimulator.printHistory(history);
    }
    
    private enum ActorState {
        
        SUSCEPTIBLE("S"), INFECTED("I"), RECOVERED("R");
        
        private String symbol;
        
        ActorState(String symbol) {
            this.symbol = symbol;
        }
        
        @Override
        public String toString() {
            return symbol;
        }
    }
    
    private class Actor {
        
        private ActorState state;
        
        Actor() {
            this.state = ActorState.SUSCEPTIBLE;
        }
        
        Actor(ActorState state) {
            this.state = state;
        }
        
        public ActorState getState() {
            return this.state;
        }
        
        public void setState(ActorState state) {
            this.state = state;
        }
        
        @Override
        public String toString() {
            return state.toString();
        }
        
    }
    
    private List<Actor[][]> history = new ArrayList<Actor[][]>();
    private Actor[][] grid;
    
    ContagionSimulator(int xSize, int ySize) {
        this.grid = new Actor[xSize][ySize];
        for(int x = 0; x < grid.length; x++){
            Actor[] col = grid[x];
            for(int y = 0; y < col.length; y++){
                if(x == 0 && y == 2){
                    grid[x][y] = new Actor(ActorState.INFECTED);
                }
                else{
                    grid[x][y] = new Actor();
                }
            }
        }
    }
    
    public void simulateTurns(int turns) {
        Actor[] firstCol = grid[0];
        for(int t = 0; t < turns; t++){
            Actor[][] newGrid = new Actor[grid.length][firstCol.length];
            for(int x = 0; x < grid.length; x++){
                for(int y = 0; y < firstCol.length; y++){
                    List<Actor> ns = getNeighbors(x, y);
                    boolean infect = false;
                    Actor a = grid[x][y];
                    for(Actor n : ns){
                        if(n.getState() == ActorState.INFECTED){
                            infect = true;
                            break;
                        }
                    }
                    if(infect){
                        newGrid[x][y] = new Actor(ActorState.INFECTED);
                    }
                    else{
                        newGrid[x][y] = a;
                    }
                }
            }
            history.add(grid);
            this.grid = newGrid;
        }
        history.add(grid);
    }
    
    public List<Actor> getNeighbors(int x, int y) {
        List<Actor> res = new ArrayList<Actor>();
        Actor[] firstCol = grid[0];
        for(int cx = x-1; cx <= x+1; cx++){
            for(int cy = y-1; cy <= y+1; cy++){
                boolean notSelf = !(cx == x && cy == y);
                boolean inX = (cx >= 0 && cx < grid.length);
                boolean inY = (cy >= 0 && cy < firstCol.length);
                if(notSelf && inX && inY){
                    Actor a = grid[cx][cy];
                    res.add(a);
                }
            }
        }
        return res;
    }
    
    public List<Actor[][]> getHistory() {
        return this.history;
    }
    
    public Actor[][] getCurrentGrid() {
        return this.grid;
    }
    
    public static void printGrid(Actor[][] grid) {
        Actor[] firstCol = grid[0];
        for(int y = 0; y < firstCol.length; y++){
            String rowStr = "";
            for(int x = 0; x < grid.length; x++){
                Actor a = grid[x][y];
                rowStr += a;
            }
            System.out.println(rowStr);
        }
    }
    
    public static void printHistory(List<Actor[][]> history) {
        int t = 0;
        Actor[][] firstGrid = history.get(0);
        Actor[] firstCol = firstGrid[0];
        System.out.println(firstGrid.length + "," + firstCol.length);
        for(Actor[][] grid : history){
            String str = t + ",";
            for(int y = 0; y < firstCol.length; y++){
                for(int x = 0; x < grid.length; x++){
                    Actor a = grid[x][y];
                    str += a;
                }
            }
         
            System.out.println(str);
            t++;
        }
    }
    
}