package theHungerGames;

/**
 * Allows you to move toward or away from another Animal
 * 
 * @author Peter Dong
 *
 */
public class MoveToward extends Move {
	
	/**
	 * @param current The Cell of the Animal that is going to do the Move
	 * @param target The Cell that it is trying to move toward or away from
	 * @param toward A boolean that is true if you want the current Animal to move closer to the target, and false otherwise
	 */
	public MoveToward(Cell current, Cell target, boolean toward) {
		super(findDirection(current, target, toward));
	}

	static private Direction findDirection(Cell current, Cell target, boolean toward) {
		int xDiff = current.getX() - target.getX();
		int yDiff = current.getY() - target.getY();

		if (toward) {
			if (Math.abs(xDiff) > Math.abs(yDiff)) {
				if (xDiff < 0) {
					return Direction.RIGHT;
				} else {
					return Direction.LEFT;
				}
			} else {
				if (yDiff < 0) {
					return Direction.DOWN;
				} else {
					return Direction.UP;
				}
			}
		} else {
			if (Math.abs(xDiff) < Math.abs(yDiff)) {
				if (xDiff < 0) {
					return Direction.LEFT;
				} else {
					return Direction.RIGHT;
				}
			} else {
				if (yDiff < 0) {
					return Direction.UP;
				} else {
					return Direction.DOWN;
				}
			}
		}
	}
}
