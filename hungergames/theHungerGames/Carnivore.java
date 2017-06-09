package theHungerGames;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for carnivorous Animals that eat other Animals
 * @author Peter Dong
 *
 */
abstract public class Carnivore extends Animal {

	private Animal prey = null;
	
	@Override
	// This is not called by Carnivore for eating, so it doesn't need to return anything
	final public double energyToEat() {
		throw new RuntimeException("You should never end up here!");
	}

	/**
	 * Sets the prey that the Carnivore will eat
	 * @param target - the target Animal
	 */
	final public void setPrey(Animal target) {
		prey = target;
	}
	
	/**
	 * @param target The Animal to examine
	 * @return Whether this Animal is a prey of this Carnivore
	 */
	final protected boolean isPrey(Animal target) {
		return target instanceof Herbivore;
	}

	static private final double MAGIC_TOXIC_NUMBER = .3;
	static private final double TOXIC_TOLERANCE = .05;
	static private final double SIZE_LEEWAY = -.1;
	@Override
	final protected double doEating() {
		if (prey == null || !isPrey(prey)
				|| prey.getCell().getX() != getCell().getX() || prey.getCell().getY() != getCell().getY()) {
			return 0;
		}
		
		// Find the animal
		double hide = hideability(prey);
		double see = vision(this);
		if (hide > see) {
			double diff = hide - see;
			if (getRandom().nextDouble() < diff) {
				return 0; // The animal successfully hid
			}
		}

		// Check for the pack
		List<Animal> others = getCell().getOtherAnimals(this);
		List<Animal> predators = new ArrayList<>();
		predators.add(this);
		for (Animal ani : others) {
			if (checkType(ani) && !ani.performedAction()) {
				predators.add(ani);
			}
		}

		// Can we even do this?
		double sizeDisp = sizeDisparity(predators, prey);
		if (sizeDisp < SIZE_LEEWAY) {
			die(); // If you try for too big a prey, it kills you
			return 0;
		}
		
		// The chase
		double speedDisp = speedDisparity(predators, prey);
		double totalDisp = sizeDisp * .5 + speedDisp;
		
		if (getRandom().nextDouble() * 3 - 1.5 < totalDisp) {
		// Caught it!
		double nutrition = prey.energyAsFood();
		prey.die();

		nutrition /= predators.size();
		
		for (Animal ani : predators) {
			if (ani != this) {
				ani.setPerformedAction(true);
				ani.addEnergy(nutrition);
			}
		}

		// Is it toxic?
		double toxicity = getGenotype().getGene(GeneType.MARKINGS1) * prey.getGenotype().getGene(GeneType.MARKINGS2);
		if (Math.abs(toxicity - MAGIC_TOXIC_NUMBER) < TOXIC_TOLERANCE) {
			nutrition = -nutrition;
		}

		prey = null;
		return nutrition;		
		} else {
			return 0; // It got away
		}
	}
	
	/**
	 * @param carnivore
	 * @param herbivore
	 * @return A number from -1 to 1.  Negative means the herbivore is faster; positive means the carnivore is faster
	 */
	static double speedDisparity(List<Animal> carnivore, Animal herbivore) {
		double carniSpeed = 0;
		for (Animal ani : carnivore) {
			carniSpeed += getSpeed(ani);
		}
		double herbiSpeed = getSpeed(herbivore);
		
		return carniSpeed - herbiSpeed;
	}
	
	static double speedDisparity(Animal carnivore, Animal herbivore) {
		double carniSpeed = getSpeed(carnivore);
		double herbiSpeed = getSpeed(herbivore);
		
		return carniSpeed - herbiSpeed;		
	}
	
	static private double hideability(Animal prey) {
		final double sizeFactor1 = .8;
		final double sizeFactor2 = .2;
		return (prey.getGenotype().getGene(GeneType.SIZE1) * sizeFactor1
				+ prey.getGenotype().getGene(GeneType.SIZE2) * sizeFactor2) * camouflage(prey);
	}
	
	static private double vision(Animal predator) {
		final double speedFactor = .8;
		final double markingFactor2 = .2;
		return (predator.getGenotype().getGene(GeneType.SPEED2) * speedFactor
				+ predator.getGenotype().getGene(GeneType.MARKINGS2) * markingFactor2) * camouflage(predator);
	}
	
	static private double sizeDisparity(List<Animal> carnivore, Animal herbivore) {
		double carniSize = 0;
		for (Animal ani : carnivore) {
			carniSize += getSize(ani);
		}
		double herbiSize = getSize(herbivore);
		
		return carniSize - herbiSize;		
	}
	
	static private double getSpeed(Animal ani) {
		final double speedFactor1 = .8;
		final double speedFactor2 = .2;
		return ani.getGenotype().getGene(GeneType.SPEED1) * speedFactor1 + ani.getGenotype().getGene(GeneType.SPEED2) * speedFactor2;
	}
	
	static private double getSize(Animal ani) {
		final double sizeFactor1 = .4;
		final double sizeFactor2 = .6;
		return ani.getGenotype().getGene(GeneType.SIZE1) * sizeFactor1 + ani.getGenotype().getGene(GeneType.SIZE2) * sizeFactor2;
	}
	
	static private double camouflage(Animal ani) {
		final double markingFactor1 = .7;
		final double markingFactor2 = .3;
		double markings = ani.getGenotype().getGene(GeneType.MARKINGS1) * markingFactor1 + ani.getGenotype().getGene(GeneType.MARKINGS2) * markingFactor2;
		double markDiff = Math.abs(markings - ani.getCell().getMarking());
		return markDiff + .5;
	}

}
