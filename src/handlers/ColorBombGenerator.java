package handlers;

import cas.Board;
import cas.Position;
import engines.GUIEngine;
import occupiers.Lokum;
import occupiers.LokumDescription;
import occupiers.SquareOccupierFactory;
import scomponents.SLokum;

/**
 * 
 * @author atilberk
 * Class: Generates a ColorBombLokum at the given position
 */
public class ColorBombGenerator implements GenerateListener {

	public ColorBombGenerator() {}

	@Override
	/**
	 * Generates a ColorBombLokum at given position
	 * @param GenerateEvent
	 * @requires e.getPosition() not null
	 * @ensures new ColorBombLokum is generated at e.position
	 */
	public void generate(GenerateEvent e) {		
		Position position = e.getPosition();
		Lokum colorBomb = SquareOccupierFactory.getInstance().generateLokum("", "ColorBombLokum");
		SLokum sl = new SLokum(colorBomb.getDescription().getType(),((LokumDescription) colorBomb.getDescription()).getColor());
		GUIEngine.getInstance().addToAnimationQueue(sl, null, position, 3, GUIEngine.getInstance().getNextTag());
		Board.getInstance().setLokum(colorBomb,position);
	}
}
