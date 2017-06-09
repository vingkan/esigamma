package theHungerGames;

/**
 * Eating for Herbivores, which has to be distinguished from Carnivores because Herbivores do not require a prey. 
 * 
 * @author Peter Dong
 *
 */
public class HerbivoreEat extends Eat {

	@Override
	public boolean doTurn(Animal animal) {
		animal.eat();
		return true;
	}

}
