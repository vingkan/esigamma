package theHungerGames;

import java.util.List;

public class GeneSet {

	List<Gene> list;
	
	public GeneSet(List<Gene> genelist) {
		this.list = genelist;
	}
	
	List<Gene> allGenesInOrder() {
		return list;
	}

}
