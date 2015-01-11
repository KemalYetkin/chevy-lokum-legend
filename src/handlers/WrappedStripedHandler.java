package handlers;

import java.util.ArrayList;
import cas.Board;
import cas.Position;
import engines.GUIEngine;
import occupiers.Lokum;
import occupiers.StripedLokum;
import occupiers.WrappedLokum;

/**
 * 
 * @author atilberk
 * Class: Handles the swap of two wrapped lokums
 */
public class WrappedStripedHandler implements SwapListener {

	public WrappedStripedHandler() {}

	@Override
	/**
	 * Adds lokums on the three row and three column of the stripedlokum on the on board to the explosion queue
	 * @param Swap Event
	 * @requires e.getLokum1() and e.getLokum2() are not null
	 * @ensures If the case, all lokums on the three row and three column of the stripedlokum on the on board are added to the explosion queue and and explosion triggered
	 */
	public void lokumSwapped(SwapEvent e) {
		if((e.getLokum1() instanceof WrappedLokum && e.getLokum2() instanceof StripedLokum) ||
				(e.getLokum1() instanceof StripedLokum && e.getLokum2() instanceof WrappedLokum)){
			Position pos;
			if (e.getLokum1() instanceof WrappedLokum) {
				pos = e.getLokum1().getPosition();		
			} else {
				pos = e.getLokum2().getPosition();	
			}
			ArrayList<Lokum> lokumsToBeExploded = new ArrayList<Lokum>();
			explodeListFiller(pos, lokumsToBeExploded);
			SubscriptionKeeper.getInstance().explode(lokumsToBeExploded);		
			Board.getInstance().fillDown(GUIEngine.getInstance().getNextTag());
		}
	}

	private void explodeListFiller(Position pos, ArrayList<Lokum> list){
		GenerateListener gl = new StripedGenerator();
		Position p;
		if(pos.getX()-1 >=0 && pos.getY()-1 >=0){
			p = new Position (pos.getX()-1,pos.getY()-1);
			Board.getInstance().removeLokumAt(p);
			GUIEngine.getInstance().addToAnimationQueue(null, p, null, 2, GUIEngine.getInstance().getNextTag());
			gl.generate(new GenerateEvent(p,"red"));
			((StripedLokum) Board.getInstance().getLokumAt(p)).setDirection("vertical");	
			list.add(Board.getInstance().getLokumAt(p));	
		}

		if(pos.getY()-1 >=0 && pos.getX()+1 <Board.COLUMN_NUMBER){
			p = new Position (pos.getX()+1,pos.getY()-1);
			Board.getInstance().removeLokumAt(p);
			GUIEngine.getInstance().addToAnimationQueue(null, p, null, 2, GUIEngine.getInstance().getNextTag());
			gl.generate(new GenerateEvent(p,"red"));
			((StripedLokum) Board.getInstance().getLokumAt(p)).setDirection("vertical");	
			list.add(Board.getInstance().getLokumAt(p));	
		}

		p = new Position (pos.getX(),pos.getY());
		Board.getInstance().removeLokumAt(p);
		GUIEngine.getInstance().getPlayGUI().getBoardGUI().removeSLokumAt(p);
		gl.generate(new GenerateEvent(p,"red"));
		((StripedLokum) Board.getInstance().getLokumAt(p)).setDirection("vertical");	
		list.add(Board.getInstance().getLokumAt(p));

		if(pos.getX()+1 <Board.COLUMN_NUMBER  && pos.getY()+1 < Board.ROW_NUMBER){
			p = new Position (pos.getX()+1,pos.getY()+1);
			Board.getInstance().removeLokumAt(p);
			GUIEngine.getInstance().addToAnimationQueue(null, p, null, 2, GUIEngine.getInstance().getNextTag());
			gl.generate(new GenerateEvent(p,"red"));
			((StripedLokum) Board.getInstance().getLokumAt(p)).setDirection("vertical");	
			list.add(Board.getInstance().getLokumAt(p));
		}

		if(pos.getY()-1 >= 0){
			p = new Position (pos.getX(),pos.getY()-1);
			Board.getInstance().removeLokumAt(p);
			GUIEngine.getInstance().addToAnimationQueue(null, p, null, 2, GUIEngine.getInstance().getNextTag());
			gl.generate(new GenerateEvent(p,"red"));
			((StripedLokum) Board.getInstance().getLokumAt(p)).setDirection("horizontal");	
			list.add(Board.getInstance().getLokumAt(p));
		}

		if(pos.getX()-1 >= 0){
			p = new Position (pos.getX()-1,pos.getY());
			Board.getInstance().removeLokumAt(p);
			GUIEngine.getInstance().addToAnimationQueue(null, p, null, 2, GUIEngine.getInstance().getNextTag());
			gl.generate(new GenerateEvent(p,"red"));
			((StripedLokum) Board.getInstance().getLokumAt(p)).setDirection("horizontal");	
			list.add(Board.getInstance().getLokumAt(p));
		}

		if (pos.getY()+1 < Board.ROW_NUMBER){
			p = new Position (pos.getX(),pos.getY()+1);
			Board.getInstance().removeLokumAt(p);
			GUIEngine.getInstance().addToAnimationQueue(null, p, null, 2, GUIEngine.getInstance().getNextTag());
			gl.generate(new GenerateEvent(p,"red"));
			((StripedLokum) Board.getInstance().getLokumAt(p)).setDirection("horizontal");	
			list.add(Board.getInstance().getLokumAt(p));
		}
	}
}

