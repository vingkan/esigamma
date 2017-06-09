package theHungerGames;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is the basic Animal class that must live in an Arena.  Inherit from this class to overload essential functions.
 * 
 * @author Peter Dong
 */
abstract public class Animal implements Drawable {

	private Cell current;
	
	private boolean dead = false;
	private boolean performedAction = false;
	
	// This determines the width of the border around the animal when drawn
	static private final double sizeScale = .15;

	private int age = 0;
	
	private double energyReserve = 0;
	
	private int gestationElapsed = 0;
	private List<Animal> fetus = new ArrayList<>();
	
	private Genotype genotype;
	
	static public Animal makeRandomAnimal(Class<? extends Animal> type) {
		Animal newAnimal = createAnimal(type);
		newAnimal.addGenotype(newAnimal.randomGenotype());
		newAnimal.energyReserve = newAnimal.initialEnergy();
		newAnimal.randomAge();
		return newAnimal;
	}
	
	static private Animal createAnimal(Class<? extends Animal> type) {
		Animal newAnimal;
		try {
			newAnimal = type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Constructor cannot have any arguments!");
		}
		return newAnimal;
	}
	
	static Animal makeAnimal(Class<? extends Animal> type, Genotype gen) {
		Animal newAnimal = createAnimal(type);
		newAnimal.addGenotype(gen);
		newAnimal.energyReserve = newAnimal.initialEnergy();
		newAnimal.randomAge();
		return newAnimal;
	}
	

	private void addGenotype(Genotype genotype) {
		this.genotype = genotype;
	}
	
	final public double getSize() {
		return (getGenotype().getGene(GeneType.SIZE1) + getGenotype().getGene(GeneType.SIZE2)) / 2;
	}
	
	final public double getSpeed() {
		return (getGenotype().getGene(GeneType.SPEED1) + getGenotype().getGene(GeneType.SPEED2)) / 2;
	}
	
	final public double getMarkings() {
		return (getGenotype().getGene(GeneType.MARKINGS1) + getGenotype().getGene(GeneType.MARKINGS2)) / 2;
	}
	
	/**
	 * @return The time of gestation elapsed as a fraction of 1
	 */
	private double fetusAge() {
		return gestationElapsed / gestationTime(); 
	}

	static private final double METABOLISM_FETUS_FACTOR = .1;
	private double fetusMetabolism() {
		double total = 0;
		for (Animal ani : fetus) {
			total += ani.metabolicConsumption();
			ani.energyReserve += ani.metabolicConsumption() * fetusAge();
		}
		
		return total * fetusAge() * METABOLISM_FETUS_FACTOR;
	}

	private void randomAge() {
		age = (int)Math.round(getRandom().nextDouble() * this.ageOfSexualMaturity() * 2);
	}
	
	/**
	 * @return The amount of energy the animal consumes per turn just by its basal metabolic processes
	 */
	static private final double METABOLISM_SCALE = 10;
	static private final double METABOLISM_BASE = 1;

	private double metabolicConsumption() {
		final double speedFactor1 = .1;
		final double speedFactor2 = .4;
		final double sizeFactor1 = .2;
		final double sizeFactor2 = .3;
		return (getGenotype().getGene(GeneType.SPEED1) * speedFactor1 + getGenotype().getGene(GeneType.SPEED2) * speedFactor2
				+ getGenotype().getGene(GeneType.SIZE1) * sizeFactor1 + getGenotype().getGene(GeneType.SIZE2) * sizeFactor2) * METABOLISM_SCALE
				+ METABOLISM_BASE + fetusMetabolism();	
	}
	
	/**
	 * @return The maximum amount of energy an animal can store.
	 */
	static private final double MAX_ENERGY_STORAGE_SCALE = 10000;
	static private final double MAX_ENERGY_STORAGE_BASE = 4000;
	private double maxEnergyStorage() {
		final double sizeFactor1 = .8;
		final double sizeFactor2 = .2;
		return (int)Math.round((getGenotype().getGene(GeneType.SIZE1) * sizeFactor1 + getGenotype().getGene(GeneType.SIZE2) * sizeFactor2)
				* MAX_ENERGY_STORAGE_SCALE + MAX_ENERGY_STORAGE_BASE);		
	}
	
	/**
	 * @return How many days it takes to be able to mate
	 */
	static private final double SEX_MATURITY_SCALE = 50;
	static private final double SEX_MATURITY_BASE = 20;
	private int ageOfSexualMaturity() {
		final double sizeFactor1 = .2;
		final double sizeFactor2 = .5;
		final double fertilityFactor = .3;
		return (int)Math.round((getGenotype().getGene(GeneType.SIZE1) * sizeFactor1 + getGenotype().getGene(GeneType.SIZE2) * sizeFactor2 
				+ getGenotype().getGene(GeneType.FERTILITY) * fertilityFactor) * SEX_MATURITY_SCALE + SEX_MATURITY_BASE);		
	}
	
	/**
	 * @return How long it takes to bear a litter
	 */
	static private final double GESTATION_SCALE = 50;
	static private final double GESTATION_BASE = 10;
	private int gestationTime() {
		final double sizeFactor1 = .7;
		final double sizeFactor2 = .3;
		return (int)Math.round((getGenotype().getGene(GeneType.SIZE1) * sizeFactor1 + getGenotype().getGene(GeneType.SIZE2) * sizeFactor2)
				* GESTATION_SCALE + GESTATION_BASE);				
	}
	
	/**
	 * @return The size of a litter.  This may fluctuate if you want; it will be called once per mating session
	 */
	static private final double FERTILITY_SCALE = 9;
	private int litterSize() {
		return (int)Math.round(getGenotype().getGene(GeneType.FERTILITY) * FERTILITY_SCALE) + 1;
	}
	
	/**
	 * @return The amount of energy an Animal starts with
	 */
	private double initialEnergy() {
		return maxEnergyStorage() / 2;
	}
	
	/**
	 * @return The amount of energy this Animal would give a predator when devoured
	 */
	static private final double ENERGY_AS_FOOD_BASE = 100;
	static private final double ENERGY_AS_FOOD_SCALE = 5000;
	final double energyAsFood() {
		final double sizeFactor1 = .5;
		final double sizeFactor2 = .5;
		return (getGenotype().getGene(GeneType.SIZE1) * sizeFactor1 + getGenotype().getGene(GeneType.SIZE2) * sizeFactor2)
				* ENERGY_AS_FOOD_SCALE + ENERGY_AS_FOOD_BASE + energyReserve;		
	}
	
	/**
	 * @return The maximum amount of food an Animal can eat in a day
	 */
	private static final double MAX_ENERGY_SCALE = 50;
	private static final double MAX_ENERGY_BASE = 5;
	final double dailyEnergyMax() {
		final double speedFactor1 = .1;
		final double speedFactor2 = .1;
		final double sizeFactor1 = .5;
		final double sizeFactor2 = .2;
		return (getGenotype().getGene(GeneType.SPEED1) * speedFactor1 + getGenotype().getGene(GeneType.SPEED2) * speedFactor2
				+ getGenotype().getGene(GeneType.SIZE1) * sizeFactor1 + getGenotype().getGene(GeneType.SIZE2) * sizeFactor2) * MAX_ENERGY_SCALE
				+ MAX_ENERGY_BASE;
	}
	
	/**
	 * @return The amount of energy required to mate
	 */
	private static final double ENERGY_TO_MATE_SCALE = 3;
	private static final double ENERGY_TO_MATE_BASE = 1;
	final double energyToMate() {
		final double sizeFactor1 = .3;
		final double sizeFactor2 = .7;
		return (getGenotype().getGene(GeneType.SIZE1) * sizeFactor1 + getGenotype().getGene(GeneType.SIZE2) * sizeFactor2)
				* ENERGY_TO_MATE_SCALE + ENERGY_TO_MATE_BASE;
	}
	
	/**
	 * @return The amount of energy required to move to another location
	 */
	private static final double ENERGY_TO_MOVE_SCALE = 6;
	private static final double ENERGY_TO_MOVE_BASE = .5;
	final double energyToMove() {
		final double speedFactor1 = .3;
		final double speedFactor2 = .7;
		final double sizeFactor1 = .5;
		final double sizeFactor2 = .5;
		return (getGenotype().getGene(GeneType.SPEED1) * speedFactor1 + getGenotype().getGene(GeneType.SPEED2) * speedFactor2
				+ getGenotype().getGene(GeneType.SIZE1) * sizeFactor1 + getGenotype().getGene(GeneType.SIZE2) * sizeFactor2) * ENERGY_TO_MOVE_SCALE
				+ ENERGY_TO_MOVE_BASE;
	}
	/**
	 * @return The amount of energy required to forage for, catch, and eat food
	 */
	abstract protected double energyToEat();
	
	/**
	 * This allows Animals to move multiple times in each turn.  Return 1 for default behavior.
	 * @return number of moves per turn the Animal gets
	 */
	private static final double NTURNS_SCALE = 2;
	private int getNTurns() {
		final double factor1 = .3;
		final double factor2 = .7;
		return (int)Math.round((getGenotype().getGene(GeneType.SPEED1) * factor1
				+ getGenotype().getGene(GeneType.SPEED2) * factor2) * NTURNS_SCALE + 1);
	}
	
	/**
	 * This allows each animal to be its own color, or to determine its color dynamically.
	 * @return the color in which the Animal is drawn on the map. 
	 */
	abstract protected Color getColor();
	
	// This draws a box in the cell with a border, with a color determined by getColor()
	@Override
	final public void Draw(Graphics graph, int x, int y) {
		if (isDead()) {
			graph.setColor(getColor().darker());
		} else {
			graph.setColor(getColor());
		}
		
		int offset = (int)(getXSize() * sizeScale);
		
		if (isDead()) {
			graph.fillOval(x + offset, y + offset, getXSize() - (2 *offset - 1), getYSize() - (2 * offset - 1));	
		} else {
			graph.fillRect(x + offset, y + offset, getXSize() - (2 *offset - 1), getYSize() - (2 * offset - 1));
		}		
	}

	@Override
	final public int getXSize() {
		return current.getXSize();
	}

	@Override
	final public int getYSize() {
		return current.getYSize();
	}
	
	/**
	 * This gives the name of the species which inherits from Animal, for display purposes
	 * @return name of the species
	 */
	abstract public String getName();
	
	final protected Genotype getGenotype() {
		return genotype;
	}
		
	/**
	 * This returns the cell where the Animal is currently located 
	 * @return cell
	 */
	final public Cell getCell() {
		return current;
	}
	
	/**
	 * @return the current Arena in which the Animal resides
	 */
	final public Arena getArena() {
		return current.getMap();
	}

	/**
	 * This sets the Animal's position.  Should not usually be used except in initialization; move() is a better function for general use
	 * @param cell - the Animal's new location
	 */
	final void setCell(Cell cell) {
		current = cell;
	}
	
	/**
	 * Safely and properly moves the Animal to a new location in a way that keeps track of all the relevant pointing properties.
	 * @param newCell - the new location of the Animal
	 */
	final boolean move(Cell newCell) {
		if (newCell.isMoveable()) {
			current.move(this, newCell);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * The main function of an Animal - every turn, the Arena calls this on each Animal.  Each turn has a beginning,
	 * a middle which repeats Nturns times, and an end.
	 */
	final void doTurn() {
		beginningOfTurn();
		
		for (int iturn = 0; iturn < getNTurns(); ++iturn)
		{
			chooseMove();
		}
		endOfTurn();
		performedAction = true;
	}

	final void setPerformedAction(boolean performed) {
		performedAction = performed;
	}
	
	/**
	 * This makes sure an Animal dies if it is out of food.
	 */
	final void beginningOfTurn() {
		if (energyReserve < 0) {
			die();
		}		
	}

	/**
	 * This delivers a child if gestation time is complete, and otherwise passes control to the inherited class 
	 */
	final void chooseMove() {
		if (isPregnant() && gestationElapsed >= gestationTime()) {
			makeChild();
		} else {			
			Turn myturn = userDefinedChooseMove();
			if (myturn != null) {
				myturn.turn(this);
			}
		}
	}
	
	/**
	 * This class is where the inherited Animal decides what to do next.
	 * @return the Turn chosen by the inherited Animal
	 */
	abstract protected Turn userDefinedChooseMove();

	/**
	 * This increases age and gestation time and also reduces the energy of the Animal each turn.
	 */
	final void endOfTurn() {
		++age;		
		if (isPregnant()) {
			++gestationElapsed;
		}
		energyReserve -= metabolicConsumption();
	}
	
	/**
	 * @return the Animal's current amount of energy.
	 */
	final protected double getEnergyReserve() {
		return energyReserve;
	}
	
	/**
	 * Adds energy to the Animal
	 * @param energy - the amount of energy to add
	 */
	final void addEnergy(double energy) {
		energyReserve += energy;
		double maxEn = maxEnergyStorage();
		if (energyReserve > maxEn) {
			energyReserve = maxEn;
		}
	}
	
	final void removeEnergy(double energy) {
		energyReserve -= energy;
	}
	
	/**
	 * @return the Animal's current age, in days
	 */
	final protected int getAge() {
		return age;
	}
	
	/**
	 * @return the number of days this Animal has been with child
	 */
	final protected int getGestationTime() {
		return gestationElapsed;
	}
	
	/**
	 * @return whether the Animal is able to procreate
	 */
	final protected boolean sexuallyMature() {
		return age >= ageOfSexualMaturity();
	}
	
	/**
	 * Tells the Animal to eat, and makes sure food does not exceed the maximum possible
	 * @return whether the Animal actually ate anything.
	 */
	final boolean eat() {
		double food = doEating();
		
		addEnergy(food);
		
		return food > 0;
	}
	
	/**
	 * The Animal-specific method of eating, overridden by Carnivore and Herbivore
	 * @return the amount of food consumed by the Animal
	 */
	abstract protected double doEating();
	
	/**
	 * @return whether the Animal is male
	 */
	final public boolean isMale() {
		return genotype.getGene(GeneType.MALE) > 0;
	}
	
	/**
	 * @return whether the Animal is pregnant
	 */
	final public boolean isPregnant() {
		return !fetus.isEmpty();
	}
	
	/**
	 * Creates a new child
	 */
	private void makeChild() {
		for (Animal ani : fetus) {
			getCell().addAnimal(ani);
		}
		fetus.clear();
		gestationElapsed = 0;
	}
	
	/**
	 * Mates with another Animal to create an offspring 
	 * @param other - the other Animal
	 * @return whether mating was successful
	 */
	final boolean mate(Animal other) {
		if (!checkMateability(other)) {
			return false;
		}
		
		int nKids = litterSize();
		for (int i = 0; i < nKids; ++i) {
			fetus.add(createOffspring(other));
		}

		return true;
	}
	
	/**
	 * Checks whether another Animal can be mated with
	 * @param other - the other Animal
	 * @return whether it is possible to mate
	 */
	final protected boolean checkMateability(Animal other) {
		return sexuallyMature() && other.sexuallyMature() && !isPregnant() && !other.isPregnant()
				&& isMale() != other.isMale() && checkType(other)
				&& getCell().getX() == other.getCell().getX() && getCell().getY() == other.getCell().getY();
	}
	
	/**
	 * This determines whether the other animal is of the same type as the current one
	 * @param other - the other animal to compare to
	 * @return whether or not they are the same type of animal
	 */
	final protected boolean checkType(Animal other) {
		return other.getClass() == this.getClass();
	}
	
	/**
	 * Creates a new Animal as a result of mating
	 * @return the new Animal
	 */
	private Animal createOffspring(Animal other) {
		try {
			Genotype gen = new Genotype(getGenotype().meiosis(), other.getGenotype().meiosis());
			Animal returntype = this.getClass().newInstance();
			returntype.addGenotype(gen);
			returntype.energyReserve = 0;
			return returntype;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException ("This shouldn't be possible!");
			// The try-catch is necessary because of Java type-safety rules, even though this should be foolproof
		}
	}
	
	private Genotype randomGenotype() {
		List<Gene> fatherGenes = new ArrayList<>();
		List<Gene> motherGenes = new ArrayList<>();
		
		randomGenes(fatherGenes, false);
		randomGenes(motherGenes, true);
		
		GeneSet father = new GeneSet(fatherGenes);
		GeneSet mother = new GeneSet(motherGenes);
		return new Genotype(father, mother);
	}
	
	private void randomGenes(List<Gene> genes, boolean mother) {
		if (mother) {
			genes.add(new Gene(GeneType.MALE, 0));
		} else {
			genes.add(new Gene(GeneType.MALE, Arena.getRandom().nextBoolean() ? 0 : 1));
		}
		
		randomGenes(genes);
	}
	
	/**
	 * @return The proper genotype of this Animal, but with alleles randomized.
	 */
	private void randomGenes(List<Gene> genes) {
		genes.add(newGene(GeneType.SIZE1));
		genes.add(newGene(GeneType.SIZE2));
		genes.add(newGene(GeneType.SPEED1));
		genes.add(newGene(GeneType.SPEED2));
		genes.add(newGene(GeneType.MARKINGS1));
		genes.add(newGene(GeneType.MARKINGS2));
		genes.add(newGene(GeneType.FERTILITY));
	}
	
	private Gene newGene(GeneType type) {
		return new Gene(type, getInitialGene(type), getInitialSD(type));
	}
	
	abstract protected double getInitialGene(GeneType type);
	abstract protected double getInitialSD(GeneType type);
	
	/**
	 * Resets the Animal at the beginning of each turn.
	 */
	final void reset() {
		performedAction = false;
	}
	
	/**
	 * Call this function to kill the Animal.
	 * The Arena will automatically remove any dead Animals at the beginning of the next turn.
	 */
	final void die() {
		dead = true;
	}
	
	/**
	 * @return whether the Animal is dead. 
	 */
	final public boolean isDead() {
		return dead;
	}
	
	/**
	 * This moves the Animal one square in the indicated direction, if possible.
	 * @param dir - The direction that the Animal is moving  
	 * @return - Whether the move was actually possible
	 */
	final boolean makeMove(Direction dir) {
		if (dir == null) return false;
		switch(dir) {

		case LEFT:
			if (current.getX() > 0) {
				return move(current.getMap().getCell(current.getX() - 1, current.getY()));
			} else {
				return false;
			}

		case UP:
			if (current.getY() > 0) {
				return move(current.getMap().getCell(current.getX(), current.getY() - 1));
			} else {
				return false;
			}

		case RIGHT:
			if (current.getX() < current.getMap().getXDim() - 1) {
				return move(current.getMap().getCell(current.getX() + 1, current.getY()));
			} else {
				return false;
			}

		case DOWN:
			if (current.getY() < current.getMap().getYDim() - 1) {
				return move(current.getMap().getCell(current.getX(), current.getY() + 1));
			} else {
				return false;
			}
		case NO_MOTION:
			return false;

		default:
			throw new RuntimeException("Reached unreachable code in makeMove()!");

		}
	}
	
	/**
	 * This is needed to avoid double-counting Animals that move during their turn.
	 * @return whether the Animal has already performed its Action this turn.
	 */
	final public boolean performedAction() {
		return performedAction;
	}
	
	/**
	 * @return the random number generator used for this class
	 */
	static protected Random getRandom() {
		return Arena.getRandom();
	}
}
