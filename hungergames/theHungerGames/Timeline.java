package theHungerGames;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Timeline implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2721365553382126027L;
	
	private String name;
	private Color color;
	private List<Integer> timeline = new ArrayList<>();
	
	public Timeline(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void addPoint(int population) {
		timeline.add(population);
	}
	
	public int getSize() {
		return timeline.size();
	}
	
	public int getMax() {
		int max = 0;
		for (Integer ii : timeline) {
			if (ii > max) {
				max = ii;
			}
		}
		return max;
	}
	
	static private int circleRadius = 5;

	public void paint(Graphics g, Rectangle grid, int max) {
		g.setColor(getColor());
		
		double divisionSize = (double)grid.getWidth() / getSize();
		double currentLocation = grid.getMinX();
		Point formerLocation = new Point(0, 0);
		
		for (Integer point : timeline) {
			double x = currentLocation;
			double y = grid.getMaxY() - (double)(point) / max * grid.getHeight();

			g.fillOval((int)(x - circleRadius), (int)(y - circleRadius), 2 * circleRadius, 2 * circleRadius);
			if (currentLocation != (int)grid.getMinX()) {
				g.drawLine((int)formerLocation.getX(), (int)formerLocation.getY(), (int)x, (int)y);
			}
			
			formerLocation.setLocation(x, y);
			currentLocation += divisionSize;
		}
	}
}
