package handlers;

import java.util.ArrayList;
import occupiers.Lokum;
import occupiers.SquareOccupier;
import cas.Board;
import engines.GameEngine;

public class Match3SDetector implements MatchListener {	

	public Match3SDetector() {}

	@Override
	/**
	 * Find matches of 3 consecutive lokums around the actor lokum
	 * @param MatchEvent e
	 * @requires e.getLokum is not null
	 * @ensures add the matched 3 lokums into the explosion list and triggers if exists
	 */
	public void match(MatchEvent e) {
		// 1 - LOOK FOR 3 lokums
		Lokum actor = e.getLokum();
		ArrayList<Lokum> lokumsMatched;
		if ((lokumsMatched = horizontalMatch(actor, Board.getInstance().getBoardMatrix())).size() < 2) {
			if ((lokumsMatched = verticalMatch(actor,Board.getInstance().getBoardMatrix())).size() < 2) {
				return; // no match
			}
		}

		lokumsMatched.add(actor);

		// 2 - CALL FOR SubsKeep.explode(...)
		SubscriptionKeeper.getInstance().explode(lokumsMatched);
		GameEngine.getInstance().updateScoreBy(Math.pow(2,SubscriptionKeeper.getInstance().getComboCount())*60);
	}

	public static ArrayList<Lokum> horizontalMatch(Lokum actor, SquareOccupier[][] lookup) {
		int actX = actor.getPosition().getX();
		int actY = actor.getPosition().getY();
		//System.out.println("horizontal\naktör: ("+actX+","+actY+")");

		ArrayList<Lokum> lokumsMatched = new ArrayList<Lokum>();
		int x = actX;
		while (--x >= 0 && actX - x < 3) {

			if (!(lookup[actY][x] instanceof Lokum)) {
				break;
			}
			Lokum neigbour = (Lokum) lookup[actY][x];

			if (neigbour == null) {
				//System.out.print("komşum boş -> ");
				//System.out.println("@ ("+x+","+actY+")");
				break;
			}
			if (!actor.getColor().equals(neigbour.getColor())){
				//System.out.print("komşum başka renk -> ");
				//System.out.println(neigbour.getColor()+" @ ("+x+","+actY+")");
				break;
			}
			lokumsMatched.add(neigbour);

		}
		x = actX;
		while (++x < Board.COLUMN_NUMBER && x - actX < 3) {

			if (!(lookup[actY][x] instanceof Lokum)) {
				break;
			}
			Lokum neigbour = (Lokum) lookup[actY][x];

			if (neigbour == null) {
				break;
			}
			if (!actor.getColor().equals(neigbour.getColor())){
				break;
			}
			lokumsMatched.add(neigbour);
		}
		return lokumsMatched;
	}

	public static ArrayList<Lokum> verticalMatch(Lokum actor, SquareOccupier[][] lookup) {
		int actX = actor.getPosition().getX();
		int actY = actor.getPosition().getY();

		ArrayList<Lokum> lokumsMatched = new ArrayList<Lokum>();
		int y = actY;

		while (--y >= 0 && actY - y < 3) {

			if (!(lookup[y][actX] instanceof Lokum)) {
				break;
			}
			Lokum neigbour = (Lokum) lookup[y][actX];

			if (neigbour == null) {
				break;
			}
			if (!actor.getColor().equals(neigbour.getColor())){
				break;
			}
			lokumsMatched.add(neigbour);
		}
		y = actY;

		while (++y < Board.ROW_NUMBER && y - actY < 3) {
			if (!(lookup[y][actX] instanceof Lokum)) {
				break;
			}
			Lokum neigbour = (Lokum) lookup[y][actX];

			if (neigbour == null){
				break;
			}
			if (!actor.getColor().equals(neigbour.getColor())){
				break;
			}
			lokumsMatched.add(neigbour);
		}
		return lokumsMatched;
	}
}
