package theHungerGames;

import java.awt.Color;

public class WallCell extends Cell {

	public WallCell(Arena map, int x, int y) {
		super(map, x, y);
	}

	@Override
	public boolean isMoveable() {
		return false;
	}

	@Override
	protected Color getColor() {
		return Color.DARK_GRAY;
	}

	
}
