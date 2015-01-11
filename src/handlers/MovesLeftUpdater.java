package handlers;

import engines.GameEngine;

/**
 * @author atilberk
 */
public class MovesLeftUpdater implements SwapListener {

	/**
	 * Constructor
	 */
	public MovesLeftUpdater() {}

	/**
	 * Decrements the number of moves left by 1
	 * 
	 * @param SwapEvent
	 * @requires GameEngine.instance != null
	 * @requires GameEngine.instance.movesLeft != null
	 * @return 
	 */
	public void lokumSwapped(SwapEvent e) {
		GameEngine.getInstance().updateMovesLeftBy(1);
	}

}
