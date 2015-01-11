package handlers;

import java.util.ArrayList;
import occupiers.Lokum;
import occupiers.StripedLokum;
import cas.Board;
import cas.Position;
import engines.GUIEngine;
import engines.GameEngine;

/**
 * 
 * @author atilberk
 * Class: Handles explosion of the StripedLokum
 */
public class StripedExploder implements ExplodeListener {
	public StripedExploder() {} // nothing to do

	@Override
	/**
	 * @requires e.getLokum() is not null
	 * @ensures if striped, e.getLokum is removed, score and combocount updated and the casuality lokums are added to the explosion queue 
	 */
	public void explode(ExplodeEvent e) {
		Lokum lokum = e.getLokum();
		if (lokum instanceof StripedLokum) {
			ArrayList<Lokum> lokumsToBeExploded = new ArrayList<Lokum>();
			StripedLokum stripedLokum = (StripedLokum) lokum;
			Position pos = stripedLokum.getPosition();
			GUIEngine.getInstance().addToAnimationQueue(null, pos, null, 2, GUIEngine.getInstance().getNextTag());
			String direction = stripedLokum.getDirection();
			boolean horizontal = direction.equals("horizontal");					
			for (int i = 0; i < (horizontal ? Board.COLUMN_NUMBER: Board.ROW_NUMBER); i++) {
				Lokum l = Board.getInstance().getLokumAt(horizontal ? new Position(i,pos.getY()) : new Position(pos.getX(),i));
				if (l != null)
					lokumsToBeExploded.add(l);
			}
			Board.getInstance().removeLokumAt(pos);
			SubscriptionKeeper.getInstance().explode(lokumsToBeExploded);
			int comboCount = SubscriptionKeeper.getInstance().getComboCount();
			GameEngine.getInstance().updateScoreBy(Math.pow(2,comboCount)*(lokumsToBeExploded.size()-1)*60);
		}
	}
}
