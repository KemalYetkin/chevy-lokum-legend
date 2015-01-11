package handlers;

import occupiers.Lokum;
import occupiers.TimeLokum;
import cas.Board;
import cas.Position;
import engines.GUIEngine;
import engines.GameEngine;

/**
 * 
 * @author atilberk
 * Class: Handles explosion of the StripedLokum
 */
public class TimeLokumExploder implements ExplodeListener {

	public TimeLokumExploder() {}

	@Override
	/**
	 * @requires e.getLokum() is not null
	 * @ensures if time lokum, e.getLokum is removed, score and combocount and remaining time is updated
	 */
	public void explode(ExplodeEvent e) {
		Lokum lokum = e.getLokum();
		if (lokum instanceof TimeLokum) {
			Position pos = lokum.getPosition();
			GUIEngine.getInstance().addToAnimationQueue(null, pos, null, 2, GUIEngine.getInstance().getNextTag());
			Board.getInstance().removeLokumAt(pos);
			GameEngine.getInstance().increaseRemainingTimeBy(((TimeLokum) lokum).getBonusTime());
			SubscriptionKeeper.getInstance().setTimeBonusCount(SubscriptionKeeper.getInstance().getTimeBonusCount()+1);
		}
	}

}
