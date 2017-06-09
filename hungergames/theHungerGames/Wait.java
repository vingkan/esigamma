package theHungerGames;

/**
 * @author Peter Dong
 * This is a Turn to just not do anything, perhaps to save energy
 */
public class Wait extends Turn {

	@Override
	protected double energyConsumption(Animal animal) {
		return 0;
	}

	@Override
	protected boolean doTurn(Animal animal) {
		return true;
	}

}
