package theHungerGames;

/**
 * Abstract class for a single action taken by an Animal
 * 
 * @author Peter Dong
 *
 */
abstract public class Turn {
	
	/**
	 * @return How much energy is consumed in this activity
	 */
	abstract protected double energyConsumption(Animal animal);
	
	/**
	 * @param animal The Animal that is performing the action
	 * @return Whether the action was successful
	 */
	public boolean turn(Animal animal) {
		double energy = energyConsumption(animal);
		
		if (animal.getEnergyReserve() < energy) {
			return false;
		}
		
		boolean result = doTurn(animal);
		
		if (result) {
			animal.removeEnergy(energy);
		}
		return result;
	}
	
	/**
	 * This method performs the action the Animal takes
	 * @param animal - the Animal that is performing the action
	 * @return whether the action was successful (defined individually for each inherited type)
	 */
	abstract protected boolean doTurn(Animal animal);
}
