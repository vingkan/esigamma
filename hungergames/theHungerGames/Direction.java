package theHungerGames;

/**
 * A quick enum to send movement commands to the Animal.
 * @author Peter Dong
 */

public enum Direction {
	LEFT, RIGHT, UP, DOWN, NO_MOTION;
	
	static public Direction randomDirection() {
		int ran = Arena.getRandom().nextInt(4);
		if (ran == 0) {
			return Direction.DOWN;
		} else if (ran == 1) {
			return Direction.UP;
		} else if (ran == 2) {
			return Direction.LEFT;
		} else {
			return Direction.RIGHT;
		}
	}
}