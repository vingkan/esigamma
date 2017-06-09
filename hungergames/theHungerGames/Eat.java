package theHungerGames;

abstract public class Eat extends Turn {

	@Override
	protected double energyConsumption(Animal animal) {
		return animal.energyToEat();
	}

}
