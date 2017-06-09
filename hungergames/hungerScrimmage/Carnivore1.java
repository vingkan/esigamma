package hungerScrimmage;

import java.awt.Color;
import java.util.List;

import theHungerGames.*;

public class Carnivore1 extends Carnivore {

	static private Color color = new Color(159, 0, 0);
	
	@Override
	protected Color getColor() {
		return color;
	}

	@Override
	public String getName() {
		return "Vinesh Carnivore";
	}
	
	public static int homeX = 14;
	public static int homeY = 14;

	private boolean lastMovedVertically = true;

	@Override
	protected Turn userDefinedChooseMove() {
		Cell cell = getCell();
		int x = cell.getX();
		int y = cell.getY();
		if (x == homeX && y == homeY) {
			List<Animal> others = cell.getOtherAnimals(this);
			for (Animal ani : others) {
				if (checkMateability(ani)) {
					return new Mate(ani);
				} else if (isPrey(ani)) {
					return new CarnivoreEat(ani);
				}
			}
			return new Move(Direction.NO_MOTION);
		}
		else {
			Direction dir = Direction.randomDirection();
			if (x == homeX) {
				if (x < homeX) {
					dir = Direction.RIGHT;
				}
				else {
					dir = Direction.LEFT;
				}
			}
			else if (y == homeY) {
				if (y < homeY) {
					dir = Direction.DOWN;
				}
				else {
					dir = Direction.UP;
				}
			}
			else {
				if (x < homeX) {
					if (y < homeY) {
						// Home is Down and to the Right
						dir = Direction.DOWN;
					}
					else {
						// Home is Up and to the Right
						dir = Direction.RIGHT;
					}
				}
				else {
					if (y < homeY) {
						// Home is Down and to the Left
						dir = Direction.LEFT;
					}
					else {
						// Home is Up and to Right
						dir = Direction.UP;
					}
				}
			}
			return new Move(dir);
		}
	}

	@Override
	protected double getInitialGene(GeneType type) {
		double ranNum = getRandom().nextGaussian() * .1 + .5;
		
		switch(type) {
		case SIZE1:
			return ranNum;

		case SIZE2:
			return ranNum;

		case SPEED1:
			return ranNum;

		case SPEED2:
			return ranNum;

		case MARKINGS1:
			return ranNum;

		case MARKINGS2:
			return ranNum;

		case FERTILITY:
			return ranNum;

		default:
			throw new RuntimeException("Never reach here");

		}
	}

	@Override
	protected double getInitialSD(GeneType type) {
		switch(type) {
		case SIZE1:
			return .1;

		case SIZE2:
			return .1;

		case SPEED1:
			return .1;

		case SPEED2:
			return .1;

		case MARKINGS1:
			return .1;

		case MARKINGS2:
			return .1;

		case FERTILITY:
			return .1;

		default:
			throw new RuntimeException("Never reach here");

		}
	}

}
