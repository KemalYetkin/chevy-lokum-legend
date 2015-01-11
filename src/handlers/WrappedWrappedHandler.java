package handlers;

import java.util.ArrayList;
import occupiers.Lokum;
import occupiers.WrappedLokum;
import cas.Board;
import cas.Position;
import engines.GUIEngine;
import engines.GameEngine;

/**
 * 
 * @author atilberk
 * Class: Handles the swap of two wrapped lokums
 */
public class WrappedWrappedHandler implements SwapListener {

	public WrappedWrappedHandler() {}

	@Override
	/**
	 * Adds lokums on the 9x9 windows with centers of wrapped lokums on board to the explosion queue
	 * @param Swap Event
	 * @requires e.getLokum1() and e.getLokum2() are not null
	 * @ensures If the case, all lokums on the 5x5 windows with centers of wrapped lokums on board  are added to the explosion queue and and explosion triggered
	 */
	public void lokumSwapped(SwapEvent e) {
		if((e.getLokum1() instanceof WrappedLokum && e.getLokum2() instanceof WrappedLokum)){				
			Position p1 = e.getLokum1().getPosition();
			Position p2 = e.getLokum2().getPosition();
			ArrayList<Lokum> lokumsToBeExploded = new ArrayList<Lokum>();			
			explodeListFiller(p1, p2, lokumsToBeExploded);
			explodeListFiller(p2, p1, lokumsToBeExploded);
			SubscriptionKeeper.getInstance().explode(lokumsToBeExploded);
			Board.getInstance().fillDown(GUIEngine.getInstance().getNextTag());
			p1 = e.getLokum1().getPosition();
			p2 = e.getLokum2().getPosition();
			GUIEngine.getInstance().addToAnimationQueue(null, p1, null, 2, GUIEngine.getInstance().getNextTag());
			GUIEngine.getInstance().addToAnimationQueue(null, p2, null, 2, GUIEngine.getInstance().getNextTag());
			lokumsToBeExploded = new ArrayList<Lokum>();
			explodeListFiller(p1, p2,lokumsToBeExploded);
			explodeListFiller(p2, p1,lokumsToBeExploded);			
			Board.getInstance().removeLokumAt(p1);
			Board.getInstance().removeLokumAt(p2);
			SubscriptionKeeper.getInstance().explode(lokumsToBeExploded);
			GameEngine.getInstance().updateScoreBy(3600);
		}
	}

	private void explodeListFiller(Position pos1, Position pos2, ArrayList<Lokum> list){
		for (int i=-2; i<=2; i++) {
			for (int j=-2; j<=2; j++){
				if(pos1.getX()+i>=0 && pos1.getX()+i< Board.ROW_NUMBER){
					if(pos1.getY()+j>=0 && pos1.getY()+j< Board.COLUMN_NUMBER){
						Position p = new Position(pos1.getX()+i,pos1.getY()+j);
						Lokum l = Board.getInstance().getLokumAt(p);
						if (l != null && !pos1.equals(p) && !pos2.equals(p) && !list.contains(l)){
							list.add(l);
						}
					}
				}
			}
		}
	}
}
