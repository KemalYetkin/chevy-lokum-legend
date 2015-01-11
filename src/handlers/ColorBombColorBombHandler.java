package handlers;

import java.util.ArrayList;
import cas.Board;
import cas.Position;
import engines.GUIEngine;
import engines.GameEngine;
import occupiers.ColorBombLokum;
import occupiers.Lokum;

/**
 * 
 * @author atilberk
 * Class: Handles the swap of two ColorBombLokum
 */
public class ColorBombColorBombHandler implements SwapListener {

	public ColorBombColorBombHandler() {}

	@Override
	/**
	 * Adds all lokums on board to the explosion queue
	 * @param Swap Event
	 * @requires e.getLokum1() and e.getLokum2() are not null
	 * @ensures If the case, all lokums on Board are added to the explosion queue and and explosion triggered
	 */
	public void lokumSwapped(SwapEvent e) {
		if(e.getLokum1() instanceof ColorBombLokum && e.getLokum2() instanceof ColorBombLokum){
			
			Position p1 = e.getLokum1().getPosition();
			Position p2 = e.getLokum2().getPosition();
			GUIEngine.getInstance().addToAnimationQueue(null, p1, null, 2, GUIEngine.getInstance().getNextTag());
			GUIEngine.getInstance().addToAnimationQueue(null, p2, null, 2, GUIEngine.getInstance().getAnimTag());
			
			ArrayList<Lokum> lokumsToBeExploded = new ArrayList<Lokum>();	

			Board.getInstance().removeLokumAt(p1);
			Board.getInstance().removeLokumAt(p2);

			for(int i = 0; i < Board.ROW_NUMBER; i++){
				for(int j = 0; j < Board.COLUMN_NUMBER; j++){
					Lokum l = Board.getInstance().getLokumAt(new Position(i,j));
					if(l != null){
						lokumsToBeExploded.add(l);
					}
				}
			}
			SubscriptionKeeper.getInstance().explode(lokumsToBeExploded);
			GameEngine.getInstance().updateScoreBy((lokumsToBeExploded.size()^2)*100);
		}
	}
}
