package handlers;

import cas.Board;
import cas.Position;
import occupiers.Lokum;
import occupiers.RegularLokum;
import engines.GUIEngine;

/**
 * 
 * @author atilberk
 * Class: Handles explosion of the StripedLokum
 */
public class RegularLokumExploder implements ExplodeListener {

	public RegularLokumExploder() {}

	@Override
	/**
	 * @requires e.getLokum() is not null
	 * @ensures if regular lokum, e.getLokum is removed, score and combocount is updated
	 */
	public void explode(ExplodeEvent e) {
		Lokum lokum = e.getLokum();
		if (lokum instanceof RegularLokum) {
			Position pos = lokum.getPosition();
			System.out.println("SLokum at "+pos);
			if (pos != null) {
				GUIEngine.getInstance().addToAnimationQueue(null, pos, null, 2, GUIEngine.getInstance().getNextTag());
				Board.getInstance().removeLokumAt(pos);
			}
		}
	}
}
