package handlers;

import cas.Board;
import cas.Position;
import engines.GUIEngine;
import engines.GameEngine;
import occupiers.Lokum;
import occupiers.LokumDescription;
import occupiers.SquareOccupierFactory;
import scomponents.SLokum;

/**
 * 
 * @author atilberk
 * Class: Generates a WrappedLokum at the given position
 */
public class WrappedGenerator implements GenerateListener {

	public WrappedGenerator() {}

	@Override
	/**
	 * Generates a WrappedLokum at given position
	 * @param GenerateEvent
	 * @requires e.getPosition() not null
	 * @ensures new WrappedLokum is generated at e.position
	 */
	public void generate(GenerateEvent e) {
		Position position = e.getPosition();
		String color = e.getColor();
		Lokum wrapped = SquareOccupierFactory.getInstance().generateLokum(color, "WrappedLokum");
		SLokum sl = new SLokum(wrapped.getDescription().getType(),((LokumDescription) wrapped.getDescription()).getColor());
		GUIEngine.getInstance().addToAnimationQueue(sl, null, position, 3, GUIEngine.getInstance().getNextTag());
		Board.getInstance().setLokum(wrapped,position);
		GameEngine.getInstance().updateScoreBy(200);
	}

}
