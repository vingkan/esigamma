package hungerScrimmage;

import java.awt.Color;
import java.util.List;

import theHungerGames.*;

public class Carnivore3 extends Carnivore {

	static private Color color = new Color(226, 204, 4);
	
	@Override
	protected Color getColor() {
		return color;
	}

	@Override
	public String getName() {
		return "Carnivore 3";
	}

	@Override
	protected Turn userDefinedChooseMove() {
		List<Animal> others = getCell().getOtherAnimals(this);
		for (Animal ani : others) {
			if (checkMateability(ani)) {
				return new Mate(ani);
			} else if (isPrey(ani)) {
				return new CarnivoreEat(ani);
			}
		}

		return new Move(Direction.randomDirection());
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
