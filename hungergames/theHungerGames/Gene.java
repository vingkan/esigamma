package theHungerGames;

public class Gene {

	private GeneType type;
	private double value;
	private double sd;

	public Gene(GeneType type, double value, double sd) {
		this.type = type;
		this.value = value;
		this.sd = sd;
		
		if (value < 0) {
			value = 0;
		} else if (value > 1) {
			value = 1;
		}
		if (sd < 0) {
			sd = 0;
		}
	}
	
	public Gene(GeneType type, double value) {
		this(type, value, 0);
	}
	
	public Gene(Gene other) {
		this.type = other.type;
		this.value = other.value;
		this.sd = other.sd;
	}
	
	public Gene mutatedVersion() {
		double newVal = value + Arena.getRandom().nextGaussian() * sd;
		if (newVal < 0) {
			newVal = 0;
		} else if (newVal > 1) {
			newVal = 1;
		}
		return new Gene(type, newVal, sd);
	}
	
	public GeneType getType() {
		return type;
	}
	
	public double getValue() {
		return value;
	}
}
