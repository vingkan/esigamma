package theHungerGames;

import java.awt.Color;

/**
 * A child class of Cell which includes food that grows back over time
 * 
 * @author Peter Dong
 */
public class FoodCell extends Cell {

	private double foodAmount;
	static private final double GROWTH_PER_TURN = 5;
	static private final double FOOD_MAX_BASE = 50;
	static private final double FOOD_MAX_SD = 3;
	static private final double GROWTH_SD = 1;
	
	private double foodMax;
	
	/**
	 * @param map - the Arena that the Cell belongs to
	 * @param x - the x coordinate of the Cell
	 * @param y - the y coordinate of the Cell
	 * @param food - the amount of food the Cell begins with
	 */
	public FoodCell(Arena map, int x, int y) {
		super(map, x, y);
		foodMax = Arena.getRandom().nextGaussian() * FOOD_MAX_SD + FOOD_MAX_BASE;
		if (foodMax < 0) {
			foodMax = 0;
		}
		foodAmount = foodMax / 2;
	}

	@Override
	public double howMuchFood() {
		return foodAmount;
	}
	
	@Override
	public double getMarking() {
		return 2 * super.getMarking() * (foodMax - foodAmount) / foodMax;
	}

	@Override
	public void beginningOfTurn() {
		double growth = Arena.getRandom().nextGaussian() * GROWTH_SD + GROWTH_PER_TURN;
		if (growth < 0) {
			growth = 0;
		}
		foodAmount += growth;
		if (foodAmount > foodMax) {
			foodAmount = foodMax;
		}
		super.beginningOfTurn();
	}

	@Override
	protected Color getColor() {
		double brightness = 1 - ((double)foodAmount / FOOD_MAX_BASE * 165 / 240);
		return Color.getHSBColor(75.0f / 240, 175.0f / 240, (float)(brightness));
	}

	@Override
	public double eatFood(double amount) {
		if (amount <= foodAmount) {
			foodAmount -= amount;
			return amount;
		} else {
			double temp = foodAmount;
			foodAmount = 0;
			return temp;
		}
	}
}
