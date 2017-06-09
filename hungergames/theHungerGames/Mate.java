package theHungerGames;

/**
 * A Turn which mates with a given Animal
 * 
 * @author Peter Dong
 *
 */
public class Mate extends Turn {

	private Animal mate;
	
	public Mate(Animal mate) {
		this.mate = mate;
	}

	@Override
	public boolean doTurn(Animal animal) {
		return animal.mate(mate);
	}

	@Override
	protected double energyConsumption(Animal animal) {
		return animal.energyToMate();
	}

}
