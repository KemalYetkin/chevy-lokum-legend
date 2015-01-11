package scomponents;

import java.awt.Color;
import java.util.*;

/**
 * Color class for the GUI
 * @author atilberk
 *
 */
@SuppressWarnings("serial")
public class SColor extends Color {

	/**
	 * Constant color values to be used in the game
	 */
	public static final SColor backgroundColor = new SColor(254, 42, 100);
	public static final SColor boardBackgroundColor = new SColor(225,239,240);
	public static final SColor panelBackgroundColor = new SColor(254, 42, 100);
	public static final SColor buttonColor = new SColor(254, 42, 100);
	public static final SColor buttonDisabledColor = new SColor(254, 203, 203);

	/**
	 * Returns a random color for the pieces
	 * @return random color
	 */
	public static final SColor getRandomPieceColor() {

		ArrayList<SColor> pieceColorSet = new ArrayList<SColor>();

		pieceColorSet.add(new SColor(241, 196, 15));	//yellow
		pieceColorSet.add(new SColor(230, 126, 34));	//orange
		pieceColorSet.add(new SColor(231, 76, 60));		//red
		pieceColorSet.add(new SColor(46, 204, 113));	//green
		pieceColorSet.add(new SColor(52, 152, 219));	//blue
		pieceColorSet.add(new SColor(155, 89, 182));	//purple

		Random rgen = new Random(System.currentTimeMillis());

		return pieceColorSet.get(rgen.nextInt(pieceColorSet.size()));
	}

	/**
	 * Constructor
	 * @param r
	 * @param g
	 * @param b
	 */
	public SColor(int r, int g, int b) {
		super(r, g, b);
	}

	/**
	 * Constructor
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public SColor(int r, int g, int b, int a) {
		super(r, g, b, a);
	}

}
