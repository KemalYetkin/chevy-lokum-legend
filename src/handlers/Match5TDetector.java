package handlers;

import java.util.ArrayList;
import occupiers.Lokum;
import cas.Board;
import cas.Position;
import engines.GameEngine;

public class Match5TDetector implements MatchListener {

	public Match5TDetector() {}

	@Override
	/**
	 * Find matches of 5 lokums forming T L or + shape around the actor lokum
	 * @param MatchEvent e
	 * @requires e.getLokum is not null
	 * @ensures add the matched 5 lokums forming T L or + shape into the explosion list and triggers if exists
	 */
	public void match(MatchEvent e) {
		//  1 - LOOK FOR 5 lokums
		Lokum actor = e.getLokum();
		ArrayList<Lokum> lokumsMatched;
		if ((lokumsMatched = crossMatch(actor)).size() < 4) {
			return;
		}
		lokumsMatched.add(actor);

		//  2 - CALL FOR SubsKeep.explode(...)
		Position actPos = actor.getPosition();
		SubscriptionKeeper.getInstance().explode(lokumsMatched);

		//  3 - GENERATE WRAPPED LOKUM AT THE ACTOR LOKUM'S POSITION
		GenerateListener gl = new WrappedGenerator();
		gl.generate(new GenerateEvent(actPos, actor.getColor()));
		GameEngine.getInstance().updateScoreBy(Math.pow(2,SubscriptionKeeper.getInstance().getComboCount())*200);
	}

	private ArrayList<Lokum> crossMatch(Lokum actor) {
		int actX = actor.getPosition().getX();
		int actY = actor.getPosition().getY();

		ArrayList<Lokum> lokumsMatchedH = new ArrayList<Lokum>();
		int x = actX;
		while (--x >= 0 && actX - x < 3) {
			Lokum neigbour = Board.getInstance().getLokumAt(new Position(x, actY));
			if (neigbour == null)
				break;
			if (!actor.getColor().equals(neigbour.getColor()))
				break;
			lokumsMatchedH.add(neigbour);
		}
		x = actX;
		while (++x < Board.COLUMN_NUMBER && x - actX < 3) {
			if (lokumsMatchedH.size() >= 2)
				break;
			Lokum neigbour = Board.getInstance().getLokumAt(new Position(x, actY));
			if (neigbour == null)
				break;
			if (!actor.getColor().equals(neigbour.getColor()))
				break;
			lokumsMatchedH.add(neigbour);
		}

		ArrayList<Lokum> lokumsMatchedV = new ArrayList<Lokum>();
		int y = actY;
		while (--y >= 0 && actY - y < 3) {
			Lokum neigbour = Board.getInstance().getLokumAt(new Position(actX, y));
			if (neigbour == null)
				break;
			if (!actor.getColor().equals(neigbour.getColor()))
				break;
			lokumsMatchedV.add(neigbour);
		}
		y = actY;
		while (++y < Board.ROW_NUMBER && y - actY < 3) {
			if (lokumsMatchedV.size() >= 2)
				break;
			Lokum neigbour = Board.getInstance().getLokumAt(new Position(actX, y));
			if (neigbour == null)
				break;
			if (!actor.getColor().equals(neigbour.getColor()))
				break;
			lokumsMatchedV.add(neigbour);
		}
		ArrayList<Lokum> lokumsMatched = new ArrayList<Lokum>();
		lokumsMatched.addAll(lokumsMatchedH);
		lokumsMatched.addAll(lokumsMatchedV);
		return lokumsMatched;
	}
}
