package theHungerGames;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.Iterator;

/**
 * @author Peter Dong
 * 
 * This is a container that holds the Cells in the simulation and governs their general behavior.
 * It calls the Cells each turn and tells them to do their work.
 *
 */
public final class Arena implements Drawable {

	private int xsize;
	private int ysize;
	
	private int ndays = 0;

	private Map<Coord, Cell> map = new TreeMap<Coord, Cell>();

	private FileWriter file;

//	static private int randomSeed = 0;
	static private Random ran = new Random();
	/**
	 * @return the random number generator that all classes should use
	 */
	static public Random getRandom() {
		return ran;
	}
	static public void setSeed(long seed) {
		ran = new Random(seed);
	}

	private boolean printout = false;

	// This is used for the countAnimals function
	private Map<String, Integer> aniMap = new HashMap<String, Integer>();
	
	private List<Animal> allAnimals = new ArrayList<>(); 

	private Map<String, Color> colorMap = new HashMap<String, Color>();

	private Map<String, Integer> finalMap = new TreeMap<>();

	private List<String> herbivoreNames = new ArrayList<>();

	public boolean isHerbivore(String name) {
		return herbivoreNames.contains(name);
	}

	/**
	 * @author Peter Dong
	 * 
	 * This is a small private class that is used to easily store coordinates for a 2 x 2 grid.
	 * The cool thing is, it doesn't even need accessors for this usage.
	 */
	private class Coord implements Comparable<Coord> {

		private int x;
		private int y;

		public Coord(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object arg0) {
			if (arg0 == this)
				return true;

			if (arg0 == null || !(arg0 instanceof Coord)) {
				return false;
			}
			Coord icoor = (Coord)arg0;
			return (x == icoor.x && y == icoor.y);
		}

		@Override
		public int compareTo(Coord icoord) {
			if (x == icoord.x) {
				if (y == icoord.y) {
					return 0;
				}
				else if (y < icoord.y) {
					return -1;
				}
				else {
					return 1;
				}
			}
			else if (x < icoord.x) {
				return -1;
			}
			else {
				return 1;
			}
		}
	}

	/**
	 * @param xsize - the size of the arena, in Cells
	 * @param ysize - the size of the arena, in Cells
	 */
	public Arena(int xsize, int ysize, int cellSize) {
		this.xsize = xsize;
		this.ysize = ysize;
		Cell.setSize(cellSize);
		initialize();
	}

	/**
	 * Tells the Arena whether or not to print the status to the standard output
	 * @param print - whether or not to print out the status
	 */
	public void setPrintout(boolean print) {
		printout = print;
	}

	/**
	 * This attaches a file to the Arena for output
	 * @param file - a FileWriter object, ready to use
	 */
	public void setFile(FileWriter file) {
		this.file = file;
	}

	@Override
	public void Draw(Graphics graph, int x, int y) {
		for (Coord icoord : map.keySet()) {
			map.get(icoord).Draw(graph, x + icoord.x * map.get(icoord).getXSize(),
					y + icoord.y * map.get(icoord).getYSize());
		}
	}

	@Override
	public int getXSize() { // This assumes that all cells are the same size!
		return getCell(0, 0).getXSize() * (getXDim() + 1);
	}

	@Override
	public int getYSize() {
		return getCell(0, 0).getYSize() * (getYDim() + 2);
	}

	/**
	 * @return the number of Cells in the Arena, in the x direction
	 */
	public int getXDim() {
		return xsize;
	}

	/**
	 * @return the number of Cells in the Arena, in the y direction
	 */
	public int getYDim() {
		return ysize;
	}

	/**
	 * This is called by the constructor to fill the map with default Cells
	 */
	protected void initialize() {
		for (int ix = 0; ix < xsize; ++ix) {
			for (int iy = 0; iy < ysize; ++iy) {
				map.put(new Coord(ix, iy), new Cell(this, ix, iy));
			}
		}
	}

	public int getNDays() {
		return ndays;
	}
	
	/**
	 * Use this function to change the Cells used with inherited ones.
	 * 
	 * @param x - the position of the Cell
	 * @param y - the position of the Cell
	 * @param newCell - the new Cell to replace the old one
	 */
	public void changeCell(int x, int y, Cell newCell) {
		Coord coord = new Coord(x, y);
		map.put(coord, newCell);
	}

	/**
	 * Adds an Animal to a particular location
	 * 
	 * @param x - the location of the Animal
	 * @param y - the location of the Animal
	 * @param an - the Animal to be added
	 */
	void addAnimal(int x, int y, Animal an) {
		getCell(x, y).addAnimal(an);

		if (!colorMap.containsKey(an)) {
			colorMap.put(an.getName(), an.getColor());
			if (an instanceof Herbivore) {
				herbivoreNames.add(an.getName());
			}
			updateMap();
		}
	}

	/**
	 * Adds an Animal to a random location on the map.
	 * 
	 * @param an - the Animal to be added
	 */
	public void addRandomAnimal(Animal an) {
		int x = ran.nextInt(getXDim());
		int y = ran.nextInt(getYDim());
		while(getCell(x, y) instanceof WallCell){
			x = ran.nextInt(getXDim());
			y = ran.nextInt(getYDim());
		}
		addAnimal(x, y, an);
	}
	
	public void addRandomTeamAnimal(Animal an, int team) {
		int x = ran.nextInt(getXDim());
		int y = ran.nextInt(getYDim());
		switch(team){
		case 1:
			while(!(x < 31 && y < 31)){
				x = ran.nextInt(getXDim());
				y = ran.nextInt(getYDim());
			}
			break;
		case 2:
			while(!(x > 31 && y < 31)){
				x = ran.nextInt(getXDim());
				y = ran.nextInt(getYDim());
			}
			break;
		case 3:
			while(!(x < 31 && y > 31)){
				x = ran.nextInt(getXDim());
				y = ran.nextInt(getYDim());
			}
			break;
		case 4:
			while(!(x > 31 && y > 31)){
				x = ran.nextInt(getXDim());
				y = ran.nextInt(getYDim());
			}
			break;
		}
		while(getCell(x, y) instanceof WallCell){
			x = ran.nextInt(getXDim());
			y = ran.nextInt(getYDim());
		}
		addAnimal(x, y, an);
	}
	
	public void addRandomForeignAnimal(Animal an, boolean foreign) {
		int x = ran.nextInt(getXDim());
		int y = ran.nextInt(getYDim());
		if(foreign){
			while(!(x > 31)){
				x = ran.nextInt(getXDim());
			}
		}
		if(!foreign){
			while(!(x < 31)){
				x = ran.nextInt(getXDim());
			}
		}
		while(getCell(x, y) instanceof WallCell){
			x = ran.nextInt(getXDim());
			y = ran.nextInt(getYDim());
		}
		addAnimal(x, y, an);
	}

	/**
	 * @param x - the location of the Cell
	 * @param y - the location of the Cell
	 * @return - the Cell at that location
	 */
	public Cell getCell(int x, int y) {
		return map.get(new Coord(x, y));
	}

	/**
	 * The primary method of the Arena, called each turn by the Viewer.
	 * It calls each Cell in turn.
	 */
	boolean doTurn() {
		for (Cell icell : map.values()) {
			icell.beginningOfTurn();
		}

		for (Cell icell : map.values()) {
			icell.doTurn();
		}

		updateMap();
		updateFinal();

		++ndays;
		
		if(ndays == 100){
			for (Map.Entry<Coord, Cell> entry: map.entrySet()){
				if(entry.getValue() instanceof WallCell){
					changeCell(entry.getValue().getX(), entry.getValue().getY(), new FoodCell(this, entry.getValue().getX(), entry.getValue().getY()));
				}
			}
		}
		
		if (printout) {
			System.out.println(countAnimals());
		}

		if (checkStillGoing()) {
			return true;
		} else {
			outputFinal();
			return false;
		}
	}

	static final int UPPER_LIMIT = 200000;

	private boolean checkStillGoing() {
		if (allAnimals.size() <= 0) {
			return false;
		} else {
			for (Integer i : aniMap.values()) {
				if (i > UPPER_LIMIT) {
					return false;
				}
			}
		}
		return true;
	}

	private void updateFinal() {
		for (Entry<String, Integer> ent : aniMap.entrySet()) {
			if (finalMap.containsKey(ent.getKey())) {
				finalMap.put(ent.getKey(), finalMap.get(ent.getKey()) + ent.getValue());
			} else {
				finalMap.put(ent.getKey(), ent.getValue());
			}
		}
	}

	private void outputFinal() {
		if (file != null) {
			for (Entry<String, Integer> ent : finalMap.entrySet())
				try {
					file.write(ent.getKey() + '\t' + ent.getValue() + '\t');
				} catch (IOException e) {
					// If there is no valid file attached, just stop
					return;
				}		
			try {
				file.write('\n');
			} catch (IOException e) {
				return;
			}
		}
	}

	void close() {
		if (file != null) {
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return A String listing the number of each different type of Animal,
	 * for easy display by the Viewer 
	 */
	public String countAnimals() {
		String response = "";

		List<String> myset = new LinkedList<String>(aniMap.keySet());
		Collections.sort(myset);
		Iterator<String> it = myset.iterator();

		while (it.hasNext()) {
			String thisOne = it.next();
			response = response.concat(thisOne + " " + aniMap.get(thisOne) + "   "); 
		}

		return response;
	}

	/**
	 * This function updates the map used by getAnimalCount() and countAnimals()
	 */
	private void updateMap() {
		for (String str : aniMap.keySet()) {
			aniMap.put(str, 0);
		}

		allAnimals.clear();
		for (Cell icell : map.values()) {
			icell.countAnimals(aniMap);
			allAnimals.addAll(icell.getOtherAnimals(null));
		}
	}
	
	private class SortDistance implements Comparator<Animal> {

		private Animal centralAnimal;
		
		public SortDistance(Animal center) {
			centralAnimal = center;
		}
		
		@Override
		public int compare(Animal arg0, Animal arg1) {
			double d1 = distance2(arg0);
			double d2 = distance2(arg1);
			if (d1 > d2) {
				return 1;
			} else if (d2 > d1) {
				return -1;
			} else {
				return 0;
			}
		}
		
		private double distance2(Animal ani) {
			double deltaX = ani.getCell().getX() - centralAnimal.getCell().getX();
			double deltaY = ani.getCell().getY() - centralAnimal.getCell().getY();
			return deltaX * deltaX + deltaY * deltaY;
		}
		
	}
	
	public List<Animal> getAllAnimals() {
		return allAnimals;
	}
	
	/**
	 * Returns a list of all the Animals in the Arena, sorted with the closest ones first
	 * @param exceptThis The Animal who is calling this function
	 * @return A list of all Animals in the Arena except for the exceptThis Animal
	 */
	public List<Animal> getAllAnimals(Animal exceptThis) {
		List<Animal> response = new ArrayList<>(allAnimals);
		
		response.remove(exceptThis);
		
		Collections.sort(response, new SortDistance(exceptThis));
		
		return response;
	}

	/**
	 * Gives access to count of each Animal, mainly for LeaderBoard
	 * @return the map of how many of each type of Animal
	 */
	public Map<String, Integer> getAnimalCount() {
		return aniMap;
	}

	/**
	 * Returns the color associated with a given Animal.
	 * @param animal - the Animal to find the color of
	 * @return the Animal's color
	 */
	public Color getAnimalColor(String animal) {
		return colorMap.get(animal);
	}

}
