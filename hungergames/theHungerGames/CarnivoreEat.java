package theHungerGames;

/**
 * The Eat command for Carnivores, who need to specify an Herbivore to eat.
 * 
 * @author Peter Dong
 *
 */
public class CarnivoreEat extends Eat {

	private Animal prey;
 	
	public CarnivoreEat(Animal prey) {
		this.prey = prey;
	}

	static private final double ENERGY_SCALE = 10;
	static private final double ENERGY_BASE = 2;
	
	@Override
	protected double energyConsumption(Animal animal) {
		double disparity = Carnivore.speedDisparity(animal, prey);
		
		return ENERGY_BASE - ENERGY_SCALE * (disparity - 1);
	}

	@Override
	public boolean doTurn(Animal animal) {
		Carnivore myCarn = (Carnivore)animal;
		myCarn.setPrey(prey);
		return myCarn.eat();
	}
	
	

}
