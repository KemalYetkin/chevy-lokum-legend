package handlers;

import cas.Board;
import cas.Position;
import engines.GUIEngine;
import engines.GameEngine;
import occupiers.Lokum;
import occupiers.LokumDescription;
import occupiers.SquareOccupierFactory;
import scomponents.SLokum;

public class RegularGenerator implements GenerateListener {

	public RegularGenerator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generate(GenerateEvent e) {
		// TODO Auto-generated method stub
		Position position = e.getPosition();
		Lokum regular = SquareOccupierFactory.getInstance().generateRandomRegularLokum();
		SLokum sl = new SLokum(regular.getDescription().getType(),((LokumDescription) regular.getDescription()).getColor());
		GUIEngine.getInstance().getPlayGUI().getBoardGUI().setSLokum(sl, position);
		Board.getInstance().setLokum(regular,position);
	}

}
