package handlers;

import java.util.ArrayList;
import occupiers.Lokum;
import cas.Board;
import cas.Position;
import engines.GameEngine;

public class Match5SDetector implements MatchListener {

	public Match5SDetector() {}

	@Override
	/**
	 * Find matches of 5 consecutive lokums around the actor lokum
	 * @param MatchEvent e
	 * @requires e.getLokum is not null
	 * @ensures add the matched 5 lokums into the explosion list and triggers if exists
	 */
	public void match(MatchEvent e) {
		//  1 - LOOK FOR 5 lokums
		Lokum actor = e.getLokum();
		ArrayList<Lokum> lokumsMatched;
		if ((lokumsMatched = horizontalMatch(actor)).size() < 4) {
			if((lokumsMatched = verticalMatch(actor)).size() < 4 ) {
				return; //no match
			}
		}
		lokumsMatched.add(actor);

		//  2 - CALL FOR SubsKeep.explode(...)
		Position actPos = actor.getPosition();
		SubscriptionKeeper.getInstance().explode(lokumsMatched);

		//  3 - GENERATE COLOR BOMB LOKUM AT THE ACTOR LOKUM'S POSITION
		GenerateListener gl = new ColorBombGenerator();
		gl.generate(new GenerateEvent(actPos, actor.getColor()));
		GameEngine.getInstance().updateScoreBy(Math.pow(2,SubscriptionKeeper.getInstance().getComboCount())*200);
	}

	private ArrayList<Lokum> horizontalMatch(Lokum actor) {
		int actX = actor.getPosition().getX();
		int actY = actor.getPosition().getY();

		ArrayList<Lokum> lokumsMatched = new ArrayList<Lokum>();
		int x = actX;
		while (--x >= 0 && actX - x < 3) {
			Lokum neigbour = Board.getInstance().getLokumAt(new Position(x, actY));
			if (neigbour == null)
				break;
			if (!actor.getColor().equals(neigbour.getColor()))
				break;
			lokumsMatched.add(neigbour);
		}
		x = actX;
		while (++x < Board.COLUMN_NUMBER && x - actX < 3) {
			Lokum neigbour = Board.getInstance().getLokumAt(new Position(x, actY));
			if (neigbour == null)
				break;
			if (!actor.getColor().equals(neigbour.getColor()))
				break;
			lokumsMatched.add(neigbour);
		}
		return lokumsMatched;
	}

	private ArrayList<Lokum> verticalMatch(Lokum actor) {
		int actX = actor.getPosition().getX();
		int actY = actor.getPosition().getY();

		ArrayList<Lokum> lokumsMatched = new ArrayList<Lokum>();
		int y = actY;
		while (--y >= 0 && actY - y < 3) {
			Lokum neigbour = Board.getInstance().getLokumAt(new Position(actX, y));
			if (neigbour == null)
				break;
			if (!actor.getColor().equals(neigbour.getColor()))
				break;
			lokumsMatched.add(neigbour);
		}
		y = actY;
		while (++y < Board.ROW_NUMBER && y - actY < 3) {
			Lokum neigbour = Board.getInstance().getLokumAt(new Position(actX, y));
			if (neigbour == null)
				break;
			if (!actor.getColor().equals(neigbour.getColor()))
				break;
			lokumsMatched.add(neigbour);
		}
		return lokumsMatched;
	}
}
