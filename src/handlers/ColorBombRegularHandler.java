package handlers;

import java.util.ArrayList;
import cas.Board;
import cas.Position;
import engines.GUIEngine;
import engines.GameEngine;
import occupiers.ColorBombLokum;
import occupiers.Lokum;
import occupiers.RegularLokum;

/**
 * 
 * @author atilberk
 * Class: Handles the swap of one ColorBombLokum and one regular lokum
 */
public class ColorBombRegularHandler implements SwapListener {

	public ColorBombRegularHandler(){}

	@Override
	/**
	 * Adds all lokums with the same color as the regular lokum on board to the explosion queue
	 * @param Swap Event
	 * @requires e.getLokum1() and e.getLokum2() are not null
	 * @ensures If the case, all lokums with the same color as the regular on Board are added to the explosion queue and and explosion triggered
	 */
	public void lokumSwapped(SwapEvent e) {
		if((e.getLokum1() instanceof RegularLokum && e.getLokum2() instanceof ColorBombLokum) ||
				(e.getLokum1() instanceof ColorBombLokum && e.getLokum2() instanceof RegularLokum)){
			RegularLokum lok;
			if(e.getLokum1() instanceof RegularLokum){
				lok = (RegularLokum) e.getLokum1();
			}
			else{
				lok = (RegularLokum) e.getLokum2();
			}
			Position p1 = e.getLokum1().getPosition();
			Position p2 = e.getLokum2().getPosition();
			GUIEngine.getInstance().addToAnimationQueue(null, p1, null, 2, GUIEngine.getInstance().getNextTag());
			GUIEngine.getInstance().addToAnimationQueue(null, p2, null, 2, GUIEngine.getInstance().getAnimTag());
			Board.getInstance().removeLokumAt(p1);
			Board.getInstance().removeLokumAt(p2);

			ArrayList<Lokum> lokumsToBeExploded = new ArrayList<Lokum>();	

			for(int i = 0; i < Board.ROW_NUMBER; i++){
				for(int j = 0; j < Board.COLUMN_NUMBER; j++){
					Lokum l = Board.getInstance().getLokumAt(new Position(i,j));
					if(l != null && l.getColor().equals(lok.getColor())){
						lokumsToBeExploded.add(l);
					}
				}
			}
			SubscriptionKeeper.getInstance().explode(lokumsToBeExploded);
			GameEngine.getInstance().updateScoreBy((lokumsToBeExploded.size()^2)*60);
		}

	}

}
