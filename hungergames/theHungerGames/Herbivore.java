package theHungerGames;

/**
 * The basic class for grass-eating Animals that eat from FoodCells
 * 
 * @author Peter Dong
 *
 */
abstract public class Herbivore extends Animal {
	
	@Override
	final public double energyToEat() {
		return getGenotype().getGene(GeneType.SIZE1) + getGenotype().getGene(GeneType.SIZE2);
	}

	@Override
	final protected double doEating() {
		return getCell().eatFood(this.dailyEnergyMax());
	}
	

}
