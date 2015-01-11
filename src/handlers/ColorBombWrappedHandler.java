package handlers;

import java.util.ArrayList;
import occupiers.ColorBombLokum;
import occupiers.Lokum;
import occupiers.WrappedLokum;
import scomponents.SLokum;
import cas.Board;
import cas.Position;
import engines.GUIEngine;


/**
 * 
 * @author atilberk
 * Class: Handles the swap of one ColorBombLokum and one wrapped lokum
 */
public class ColorBombWrappedHandler implements SwapListener {

	public ColorBombWrappedHandler() {}

	@Override
	/**
	 * Adds all lokums in the two colors (one as the same as the wrapped's) on the on board to the explosion queue
	 * @param Swap Event
	 * @requires e.getLokum1() and e.getLokum2() are not null
	 * @ensures If the case, all lokums in the two colors (one as the same as the wrapped's) on the on board are added to the explosion queue and and explosion triggered
	 */
	public void lokumSwapped(SwapEvent e) {
		if((e.getLokum1() instanceof WrappedLokum && e.getLokum2() instanceof ColorBombLokum) ||
				(e.getLokum1() instanceof ColorBombLokum && e.getLokum2() instanceof WrappedLokum)){
			WrappedLokum wlok;
			ColorBombLokum cblok;
			if(e.getLokum1() instanceof WrappedLokum){
				wlok = (WrappedLokum) e.getLokum1();
				cblok = (ColorBombLokum) e.getLokum2();
			}
			else{
				wlok = (WrappedLokum) e.getLokum2();
				cblok = (ColorBombLokum) e.getLokum1();
			}				
			Position p1 = wlok.getPosition();
			SLokum sl1 = GUIEngine.getInstance().getPlayGUI().getBoardGUI().getSLokumAt(p1);
			if (sl1 != null)
				GUIEngine.getInstance().getPlayGUI().getBoardGUI().removeSLokumAt(p1);
			ArrayList<Lokum> lokumsToBeExploded = new ArrayList<Lokum>();		
			Board.getInstance().removeLokumAt(p1);	

			explodeListFiller(wlok.getColor(), lokumsToBeExploded);
			SubscriptionKeeper.getInstance().explode(lokumsToBeExploded);
			Board.getInstance().fillDown(GUIEngine.getInstance().getNextTag());
			SLokum sl2 = GUIEngine.getInstance().getPlayGUI().getBoardGUI().getSLokumAt(cblok.getPosition());
			if (sl2 != null)
				GUIEngine.getInstance().getPlayGUI().getBoardGUI().removeSLokumAt(cblok.getPosition());
			String color = chooseRandomExplodeColor(cblok.getPosition());
			Board.getInstance().removeLokumAt(cblok.getPosition());	
			explodeListFiller(color,lokumsToBeExploded);
			SubscriptionKeeper.getInstance().explode(lokumsToBeExploded);		
			Board.getInstance().fillDown(GUIEngine.getInstance().getNextTag());
		}
	}

	private void explodeListFiller(String color, ArrayList<Lokum> lokumsToBeExploded){
		for(int i = 0; i < Board.ROW_NUMBER; i++){
			for(int j = 0; j < Board.COLUMN_NUMBER; j++){
				Lokum l = Board.getInstance().getLokumAt(new Position(i,j));
				if(l != null && l.getColor().equals(color)){				
					lokumsToBeExploded.add(Board.getInstance().getLokumAt(l.getPosition()));						
				}
			}
		}
	}

	private String chooseRandomExplodeColor(Position pos){
		ArrayList<Lokum> lokumList = new ArrayList<Lokum>();
		for (int i=-1; i<=1; i++) {
			for (int j=-1; j<=1; j++){
				if(pos.getX()+i>=0 && pos.getX()+i< Board.ROW_NUMBER){
					if(pos.getY()+j>=0 && pos.getY()+j< Board.COLUMN_NUMBER){
						Lokum l = Board.getInstance().getLokumAt(new Position(pos.getX()+i,pos.getY()+j));
						if (l != null)
							lokumList.add(l);
					}
				}
			}
		}
		int chosenLocation = (int) Math.floor(Math.random()*lokumList.size());
		return lokumList.get(chosenLocation).getColor();
	}

}
