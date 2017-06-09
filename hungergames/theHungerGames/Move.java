package theHungerGames;

/**
 * A Turn which causes a move in a given Direction
 * 
 * @author Peter Dong
 *
 */
public class Move extends Turn {
	
	private Direction dir;

	public Move(Direction dir) {
		this.dir = dir;
	}

	@Override
	public boolean doTurn(Animal animal) {
		return animal.makeMove(dir);
	}

	@Override
	protected double energyConsumption(Animal animal) {
		return animal.energyToMove();
	}

}
