package hungerScrimmage;

import java.awt.Color;
import java.util.List;
import java.util.*;

import theHungerGames.*;

public class Carnivore2 extends Carnivore {

	static private Color color = new Color(222,219,238);
	private int amount = 10;
	private Direction mrbean = Direction.DOWN;
	
	@Override
	protected Color getColor() {
		return color;
	}

	@Override
	public String getName() {
		return "Mr Bean";
	}

	@Override
	protected Turn userDefinedChooseMove() {

		
		Cell cell = getCell();
		List<Animal> others = getCell().getOtherAnimals(this);
		
		List<Animal> all = getArena().getAllAnimals();
		List<Animal> same = new ArrayList<Animal>();
		for (Animal ani : all) {
			if (this.checkType(ani)) {
				same.add(ani);
			}
		}
		if (amount == 0) {
			int randInt = (int)Math.round(getRandom().nextDouble());
			amount = randInt * same.size();
			mrbean = Direction.randomDirection();
		}
		
		for (Animal ani : others) {
			if (same.size() < 5) {
				if (checkMateability(ani)) {
					return new Mate(ani);
				} else if (isPrey(ani)) {
					return new CarnivoreEat(ani);
				}
			}
			else if (same.size() > 10) {
				if (isPrey(ani)) {
					return new CarnivoreEat(ani);
				}
			}
		}
		int xloc = cell.getX();
		int yloc = cell.getY();
		if (xloc + 1 == cell.getXSize()) {
			mrbean = Direction.LEFT;
		}
		else if (xloc - 1 == 0) {
			mrbean = Direction.RIGHT;
		}
		else if (yloc + 1 == cell.getYSize()) {
			mrbean = Direction.UP;
		}
		else if (yloc - 1 == 0) {
			mrbean = Direction.DOWN;
		}
		amount--;
		return new Move(mrbean);
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
