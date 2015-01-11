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
 * Class: Generates a RegularLokum at the given position
 */
public class RegularGenerator implements GenerateListener {

	public RegularGenerator() {}

	@Override
	/**
	 * Generates a RegularLokum at given position
	 * @param GenerateEvent
	 * @requires e.getPosition() not null
	 * @ensures new RegularLokum is generated at e.position
	 */
	public void generate(GenerateEvent e) {
		// TODO Auto-generated method stub
		Position position = e.getPosition();
		Lokum regular = SquareOccupierFactory.getInstance().generateRandomRegularLokum();
		SLokum sl = new SLokum(regular.getDescription().getType(),((LokumDescription) regular.getDescription()).getColor());
		GUIEngine.getInstance().addToAnimationQueue(sl, null, position, 3, GUIEngine.getInstance().getNextTag());
		Board.getInstance().setLokum(regular,position);
	}
}
