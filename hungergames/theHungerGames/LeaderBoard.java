package theHungerGames;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

/**
 * The LeaderBoard class creates colored bars which show which Animal is winning the competition.
 * 
 * @author Peter Dong
 *
 */
public class LeaderBoard extends JPanel implements ActionListener {
	
	public static Map<String,Integer> scores = new HashMap<String,Integer>();

	/**
	 * This small class implements a sort specific to Animal names, based on a map of the rankings passed to it
	 * in the constructor.
	 * @author Peter Dong
	 *
	 */
	private class AnimalSort implements Comparator<String> { 
		
		private Map<String, Integer> mymap;
		
		/**
		 * @param themap - the map of number of Animals for each Animal name that will be used to implement the sort.
		 */
		public AnimalSort(Map<String, Integer> themap) {
			mymap = themap;
		}

		@Override
		public int compare(String arg0, String arg1) {
			int num0 = mymap.get(arg0);
			int num1 = mymap.get(arg1);

			// descending order
			if (num0 > num1) {
				return -1;
			} else if (num0 < num1) {
				return 1;
			} else {
				if (turnMap.containsKey(arg0) && turnMap.containsKey(arg1)) {

					int turn0 = turnMap.get(arg0);
					int turn1 = turnMap.get(arg1);

					if (turn0 > turn1) {
						return -1;
					} else if (turn0 < turn1) {
						return 1;
					} else {
						return 0;
					}
				} else {
					return 0;

				}
			}
		}

	}

	private Arena arena;
	
	
	/**
	 * This is the size, in pixels, of the board
	 */
	private static final int X_SIZE = 1000;
	private static final int Y_SIZE = 600;
	
	private int turnCount = 0;
	private Map<String, Integer> turnMap = new HashMap<>();
	
	/**
	 * We initialize the names with those already existing, so the Animals should already be added to the Arena
	 * @param arena - the Arena that this is based on
	 */
	public LeaderBoard(Arena arena) {
		this.arena = arena;
		setSize(X_SIZE, Y_SIZE);
		setBackground(Color.white);
	}
	
	/**
	 * Required to keep Eclipse from complaining
	 */
	private static final long serialVersionUID = 7612897060865914777L;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		myPaint(g);
	}
	
	/**
	 * Paint function which draws the LeaderBoard
	 * @param g - the Graphics object to paint with
	 */
	private void myPaint(Graphics g) {
		List<String> herbivores = new ArrayList<>();
		List<String> carnivores = new ArrayList<>();
		
		for (String ani : arena.getAnimalCount().keySet()) {
			if (arena.isHerbivore(ani)) {
				herbivores.add(ani);
			} else {
				carnivores.add(ani);
			}
		}
		
		final int leftEdge = 30;
		
		drawLeaderBars(g, (int)(leftEdge), herbivores);
		drawLeaderBars(g, (int)(X_SIZE * .5), carnivores);
		
		++turnCount;
	}
	
	/**
	 * The function which draws the text and colored bars for the LeaderBoard
	 * 
	 * @param g - the Graphics object
	 * @param xleft - the leftmost border of the region to draw in
	 * @param animals - the list of animals to draw here
	 */
	private void drawLeaderBars(Graphics g, int xleft, List<String> animals) {
		final int lineHeight = 30;
		final float fontSize = 18;
		final int textOffset = 10;

		g.setFont(g.getFont().deriveFont(fontSize));
		
		Map<String, Integer> themap = arena.getAnimalCount();		
		Collections.sort(animals, new AnimalSort(themap));
		
		int currentY = 0;
		
		for (String ani : animals) {
			g.setColor(arena.getAnimalColor(ani));
			int textHeight = currentY + lineHeight - textOffset;
			
			g.drawString(ani, xleft, textHeight);
			
			double maxSize = (X_SIZE * .25) * (1 - OFFSET);
			double widthRatio = (double)(themap.get(ani)) / getMax();
			int width = (int)(widthRatio * maxSize);
			int height = (int)(lineHeight * (1 - OFFSET));
			int drawPosition = (int)(maxSize + xleft);
			if (widthRatio > 0) {			
				g.fillRect(drawPosition, currentY, width, height);
				
				Color current = arena.getAnimalColor(ani);
				final int maxColor = 255;
				Color inverse = new Color(maxColor - current.getRed(), maxColor - current.getGreen(), maxColor - current.getBlue());
				g.setColor(inverse);
				g.drawString(themap.get(ani).toString(), drawPosition, textHeight);
				
			} else {
				if (!turnMap.containsKey(ani)) {
					turnMap.put(ani, turnCount);
				}
				String output = "EXTINCT after " + turnMap.get(ani) + " turns";
				
				g.drawString(output, drawPosition, currentY + height);			
			}
			
			currentY += lineHeight;
		}
	}
	
	/**
	 * This is the border that the program leaves on the left and right sides, as a percentage
	 */
	static private final double OFFSET = .1;
	
	/**
	 * @return the maximum number of any one Animal on the board at the time
	 */
	private int getMax() {
		int response = 0;
		for (Integer i : arena.getAnimalCount().values()) {
			if (i > response) {
				response = i;
			}
		}
		
		return response;
	}
	
}
