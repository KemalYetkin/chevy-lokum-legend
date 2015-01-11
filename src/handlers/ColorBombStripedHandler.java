package handlers;

import java.util.ArrayList;
import occupiers.ColorBombLokum;
import occupiers.Lokum;
import occupiers.StripedLokum;
import cas.Board;
import cas.Position;
import engines.GUIEngine;

/**
 * 
 * @author atilberk
 * Class: Handles the swap of one ColorBombLokum and one striped lokum
 */
public class ColorBombStripedHandler implements SwapListener {

	public ColorBombStripedHandler() {}

	@Override
	/**
	 * Replaces all lokums with the same color as the swapped stripe with a striped lokum and adds those in the explosion queue
	 * @param Swap Event
	 * @requires e.getLokum1() and e.getLokum2() are not null
	 * @ensures If the case,  all lokums with the same color as the swapped stripe replaced with a striped lokum and those are added in the explosion queue and explosion is triggered
	 */
	public void lokumSwapped(SwapEvent e) {
		if((e.getLokum1() instanceof StripedLokum && e.getLokum2() instanceof ColorBombLokum) ||
				(e.getLokum1() instanceof ColorBombLokum && e.getLokum2() instanceof StripedLokum)){
			StripedLokum lok;
			if(e.getLokum1() instanceof StripedLokum){
				lok = (StripedLokum) e.getLokum1();
			}
			else{
				lok = (StripedLokum) e.getLokum2();
			}
			GenerateListener gl = new StripedGenerator();
			Position p1 = e.getLokum1().getPosition();
			Position p2 = e.getLokum2().getPosition();

			int tag = GUIEngine.getInstance().getNextTag();
			ArrayList<Lokum> lokumsToBeExploded = new ArrayList<Lokum>();			
			for(int y = 0; y < Board.ROW_NUMBER; y++){
				for(int x = 0; x < Board.COLUMN_NUMBER; x++){
					Lokum l = Board.getInstance().getLokumAt(new Position(x,y));
					if(l != null && l.getColor().equals(lok.getColor())){
						Position pos = l.getPosition();
						GUIEngine.getInstance().addToAnimationQueue(null, pos, null, 2, tag);
						Board.getInstance().removeLokumAt(pos);
						gl.generate(new GenerateEvent(pos,l.getColor()));
						lokumsToBeExploded.add(Board.getInstance().getLokumAt(pos));					
					}
				}
			}
			GUIEngine.getInstance().addToAnimationQueue(null, p1, null, 2, tag);
			GUIEngine.getInstance().addToAnimationQueue(null, p2, null, 2, tag);
			Board.getInstance().removeLokumAt(p1);
			Board.getInstance().removeLokumAt(p2);	
			SubscriptionKeeper.getInstance().explode(lokumsToBeExploded);				
		}
	}

}
