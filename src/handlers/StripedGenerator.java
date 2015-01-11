package handlers;

import occupiers.Lokum;
import occupiers.LokumDescription;
import occupiers.SquareOccupierFactory;
import occupiers.StripedLokum;
import scomponents.SLokum;
import cas.Board;
import cas.Position;
import engines.GUIEngine;

/**
 * 
 * @author atilberk
 * Class: Generates a StripedLokum at the given position
 */
public class StripedGenerator implements GenerateListener {

	public StripedGenerator() {} // nothing to do

	@Override
	/**
	 * Generates a StripedLokum at given position
	 * @param GenerateEvent
	 * @requires e.getPosition() not null
	 * @ensures new StripedLokum is generated at e.position
	 */
	public void generate(GenerateEvent e) {	
		Position position = e.getPosition();
		String color = e.getColor();
		String direction = e.getDetail();
		Lokum striped = SquareOccupierFactory.getInstance().generateLokum(color, "StripedLokum");
		((StripedLokum) striped).setDirection(direction);
		SLokum sl = new SLokum(striped.getDescription().getType(),((LokumDescription) striped.getDescription()).getColor());
		GUIEngine.getInstance().addToAnimationQueue(sl, null, position, 3, GUIEngine.getInstance().getNextTag());
		Board.getInstance().setLokum(striped,position);
	}
}
