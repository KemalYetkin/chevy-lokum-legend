package handlers;

import java.util.ArrayList;
import cas.Board;
import cas.Position;
import engines.GameEngine;
import occupiers.Lokum;

public class Match4SDetector implements MatchListener {

	public Match4SDetector() {} // I love empty constructors <3

	@Override
	/**
	 * Find matches of 4 consecutive lokums around the actor lokum
	 * @param MatchEvent e
	 * @requires e.getLokum is not null
	 * @ensures add the matched 4 lokums into the explosion list and triggers if exists
	 */
	public void match(MatchEvent e) {
		//  1 - LOOK FOR 4 lokums
		Lokum actor = e.getLokum();
		String matchDirection;
		ArrayList<Lokum> lokumsMatched;
		if ((lokumsMatched = horizontalMatch(actor)).size() < 3) {
			if((lokumsMatched = verticalMatch(actor)).size() < 3 ) {
				return; //no match
			}
			matchDirection = "vertical";
		} else {
			matchDirection = "horizontal";
		}

		lokumsMatched.add(actor);

		//  2 - CALL FOR SubsKeep.explode(...)
		Position actPos = actor.getPosition();
		SubscriptionKeeper.getInstance().explode(lokumsMatched);

		//  3 - GENERATE STRIPED LOKUM AT THE ACTOR LOKUM'S POSITION
		System.out.print("lokums generated");
		GenerateListener gl = new StripedGenerator();
		gl.generate(new GenerateEvent(actPos, actor.getColor(), matchDirection));
		GameEngine.getInstance().updateScoreBy(Math.pow(2,SubscriptionKeeper.getInstance().getComboCount())*120);
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
