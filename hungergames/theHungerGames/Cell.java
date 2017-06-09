package theHungerGames;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * A class for the basic unit of the Arena, a Cell.
 * Animals can live in the Cell, and food can be found there.
 * 
 * @author Peter Dong
 * 
 */
public class Cell implements Drawable {

	private int x;
	private int y;
	
	private List<Animal> list = new LinkedList<Animal>();
	
	private Arena map;

	private double markingScore;
	
	/**
	 * This is the hard-wired size of each Cell.
	 */
	static private int SIZE = 0;
	
	/**
	 * This sets the universal size of each Cell.
	 * @param size - the size of each cell
	 */
	static public void setSize(int size) {
		SIZE = size;
	}
	
	/**
	 * @param map - the Arena the Cell belongs to
	 * @param x - the position of the Cell
	 * @param y - the position of the Cell
	 */
	public Cell(Arena map, int x, int y) {
		if (SIZE <= 0) {
			SIZE = 1; // A default, but not a good one.  Arena should normally set this value
		}
		this.map = map;
		this.x = x;
		this.y = y;
		
		chooseRandomMarking();
	}
	
	static private final double MARKING_MEAN = 0.5;
	static private final double MARKING_SD = 0.05;
	protected void chooseRandomMarking() {
		markingScore = Arena.getRandom().nextGaussian() * MARKING_SD + MARKING_MEAN;
	}
	
	public double getMarking() {
		return markingScore;
	}
	
	// This draws the Cell and all the Animals inside the Cell.
	@Override
	public void Draw(Graphics graph, int x, int y) {
		
		graph.setColor(getColor());
		
		graph.fillRect(x, y, getXSize(), getYSize());
		
		for (Animal ian : list) {
			ian.Draw(graph, x, y);
		}
		graph.setColor(Color.gray);
		graph.drawRect(x, y, getXSize(), getYSize());
	}
	
	/**
	 * @return the Color of the Cell
	 */
	protected Color getColor() {
		double brightness = 1 - (165 / 240);
		return Color.getHSBColor(75.0f / 240, 175.0f / 240, (float)(brightness));
	}

	@Override
	public int getXSize() {
		return SIZE;
	}

	@Override
	public int getYSize() {
		return SIZE;
	}

	/**
	 * @return the x position of the Cell
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return the y position of the Cell
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * @return the Arena to which the Cell belongs
	 */
	public Arena getMap() {
		return map;
	}
	
	/**
	 * Actions the Cell performs at the beginning of each turn.
	 * In particular, it removes dead Animals.
	 */
	public void beginningOfTurn() {
		Iterator<Animal> it = list.iterator();
		
		while (it.hasNext()) {
			Animal ian = it.next();
			ian.reset();
			if (ian.isDead()) {
				it.remove();
			}			
	
		}
	}
	
	/**
	 * The main work of the Cell, which is to tell each Animal to complete each turn
	 */
	public void doTurn() {
		boolean allAnimalsDone = false;
		
		while (!allAnimalsDone) {
			ListIterator<Animal> it = list.listIterator();
			allAnimalsDone = true;
			
			while (it.hasNext()) {
				Animal ian = it.next();
				
				if (!ian.performedAction()) {
					allAnimalsDone = false;
					ian.doTurn();
					break;
				}
			}
		}
	}
	
	/**
	 * @param map - the Map that is counting Animals for the Arena's countAnimals() function.
	 * The Map just adds the number of Animals of each type in each Cell.
	 */
	public void countAnimals(Map<String, Integer> map) {
		for (Animal ian : list) {
			if (!map.containsKey(ian.getName())) {
				map.put(ian.getName(), 1);
			}
			else {
				map.put(ian.getName(), map.get(ian.getName()) + 1);
			}
		}
	}
	
	/**
	 * Adds an Animal to the Cell.
	 * 
	 * @param an the Animal to add
	 */
	public void addAnimal(Animal an) {
		list.add(an);
		an.setCell(this);
	}
	
	/**
	 * Removes an Animal from a Cell.
	 * 
	 * @param an the Animal to remove
	 */
	public void removeAnimal(Animal an) {
		if (list.remove(an) == false) {
			throw new RuntimeException("Attempted to remove animal that wasn't there!");
		}
	}
	
	/**
	 * This returns all Animals in the cell except one, which can be useful.
	 * 
	 * @param first - the Animal we already know about
	 * @return a List of all other Animals in the Cell except first. 
	 */
	public List<Animal> getOtherAnimals(Animal first) {
		List<Animal> response = new LinkedList<Animal>();
		
		for (Animal ian : list) {
			if (ian != first) {
				response.add(ian);
			}
		}
		
		return response;
	}
	
	/**
	 * This safely and properly moves an Animal from one Cell to another.
	 * 
	 * @param an - the Animal to move
	 * @param newCell - the Cell to move the Animal to
	 */
	public void move(Animal an, Cell newCell) {
		newCell.addAnimal(an);
		removeAnimal(an);
		an.setCell(newCell);
	}
	
	public boolean isMoveable() {
		return true;
	}
	
	/**
	 * Added primarily to make overloading more convenient.
	 * 
	 * @return how much food the Cell has.
	 */
	public double howMuchFood() {
		return 0;
	}
	
	/**
	 * Added primarily to make overloading more convenient.
	 * 
	 * @param amount - the amount of food the Animal requests from the Cell. 
	 * @return How much food the Animal actually gets.
	 */	
	public double eatFood(double amount) {
		return 0;
	}
}
