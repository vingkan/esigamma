package theHungerGames;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

public class TimelineBoard extends JPanel implements ActionListener {

	private List<Timeline> timelines = new ArrayList<>();
	private double timeIncrement;
	private double time = 0;
	
	private static final int X_SIZE = 1000;
	private static final int Y_SIZE = 600;
	
	private static final double BORDER = .05;
	
	private Arena arena;
	
	private int maximum = 0;
	private int maxNameLength = 0;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2894866921233873348L;

	/**
	 * @param arena - this must have been filled with Animals before calling this function
	 */
	public TimelineBoard(Arena arena, double timeIncrement) {
		this.timeIncrement = timeIncrement;
		this.arena = arena;

		setSize(X_SIZE, Y_SIZE);
		setBackground(Color.WHITE);
		
		fillTimelines();
	}
	
	private void fillTimelines() {
		for (String str : arena.getAnimalCount().keySet()) {
			addTimeline(new Timeline(str, arena.getAnimalColor(str)));
		}
	}
	
	public void addTimeline(Timeline timeline) {
		timelines.add(timeline);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		updateTimelines();
		repaint();
	}
	
	private void updateTimelines() {
		Map<String, Integer> mymap = arena.getAnimalCount();	
		for (Entry<String, Integer> entry : mymap.entrySet()) {
			Timeline myTimeline = getTimeline(entry.getKey());
			myTimeline.addPoint(entry.getValue());
		}
		setMax();
		time += timeIncrement;
	}
	
	private Timeline getTimeline(String name) {
		for (Timeline timeline : timelines) {
			if (timeline.getName() == name) {
				return timeline;
			}
		}

		return null;
	}

	@Override
	public void paint(Graphics arg0) {
		super.paint(arg0);
		setMax();
		
		Rectangle grid = new Rectangle((int)(BORDER * X_SIZE), (int)(BORDER * Y_SIZE), (int)(X_SIZE * (1 - 2 * BORDER)),
				(int)(Y_SIZE * (1 - 4 * BORDER)));

		
		for (Timeline timeline : timelines) {
			timeline.paint(arg0, grid, maximum);
		}

		drawLegend(arg0);
		drawAxes(arg0, grid);
	}


	private static int LETTER_SIZE = 15;
	private static int LETTER_HEIGHT = 20;
	
	private void drawLegend(Graphics arg0) {		
		int leftEdge = (int)(X_SIZE * (1 - 2 * BORDER) - LETTER_SIZE * maxNameLength);
		double topPosition = BORDER * Y_SIZE;
		arg0.setFont(new Font(arg0.getFont().getFontName(), Font.PLAIN, (int)(LETTER_HEIGHT * .9)));
		
		for (Timeline timeline : timelines) {
			arg0.setColor(timeline.getColor());
			arg0.drawString(timeline.getName(), leftEdge, (int)(topPosition + LETTER_HEIGHT));
			arg0.fillRect(leftEdge - 2 * LETTER_SIZE, (int)topPosition, LETTER_SIZE, LETTER_HEIGHT);
			topPosition += LETTER_HEIGHT;
		}	
	}
	
	static private NumberFormat df = new DecimalFormat("0.##");
	static private int AXIS_TITLE_OFFSET = 30;
	static private int AXIS_LABEL_OFFSET = 12;
	
	private void drawAxes(Graphics arg0, Rectangle grid) {
		double increment = increment(maximum);
		arg0.setColor(Color.BLACK);
		arg0.drawLine((int)grid.getMinX(), (int)grid.getMinY(), (int)grid.getMinX(), (int)grid.getMaxY());
		
		for (double counter = 0; counter < maximum; counter += increment) {
			int positionY = (int)(grid.getMaxY() - counter / maximum * grid.getHeight());
			arg0.drawString(Integer.toString((int)counter), (int)grid.getMinX(), positionY);
		}
		
		double xInc = increment(time);
		arg0.drawLine((int)grid.getMinX(), (int)grid.getMaxY(), (int)grid.getMaxX(), (int)grid.getMaxY());
		arg0.drawString("Time (days)", (int)(grid.getWidth() / 2), (int)grid.getMaxY() + AXIS_TITLE_OFFSET);
		
		for (double counter = xInc; counter < time; counter += xInc) {
			int positionX = (int)(counter / time * grid.getWidth());
			arg0.drawString(df.format(counter), positionX, (int)grid.getMaxY() + AXIS_LABEL_OFFSET);
		}
				
	}


	private double increment(double max) {
		if (max == 0) {
			return 0;
		}
		int exponent = (int)Math.floor(Math.log10(max));
		double base = Math.pow(10, exponent);
		int mantissa = (int)(max / base);
		
		if (mantissa > 5) {
			return base;
		} else {
			return (double)base / 5;
		}
	}

	
	private void setMax() {
		for (Timeline timeline : timelines) {
			int checkMax = timeline.getMax();
			if (checkMax > maximum) {
				maximum = checkMax;
			}
		}
	}

}
