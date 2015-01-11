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
 * Class: Handles explosion of the WrappedLokum
 */
public class WrappedExploder implements ExplodeListener {

	public WrappedExploder() {}

	@Override
	/**
	 * @requires e.getLokum() is not null
	 * @ensures if wrapped, e.getLokum is removed, score and combocount updated and the casuality lokums are added to the explosion queue 
	 */
	public void explode(ExplodeEvent e) {
		Lokum lokum = e.getLokum();
		if (lokum instanceof WrappedLokum) {
			ArrayList<Lokum> lokumsToBeExploded = new ArrayList<Lokum>();
			WrappedLokum wLokum = (WrappedLokum) lokum;
			Position pos = wLokum.getPosition();
			Board.getInstance().removeLokumAt(pos);
			explodeListFiller(pos,lokumsToBeExploded);
			SubscriptionKeeper.getInstance().explode(lokumsToBeExploded);
			GUIEngine.getInstance().addToAnimationQueue(null, pos, null, 2, GUIEngine.getInstance().getNextTag());
			int comboCount = SubscriptionKeeper.getInstance().getComboCount();
			GameEngine.getInstance().updateScoreBy(Math.pow(2,comboCount)*1080);
		}
	}

	private void explodeListFiller(Position pos, ArrayList<Lokum> list){
		ArrayList <Lokum> specialLok = new ArrayList<Lokum>();
		for (int i=-1; i<=1; i++) {
			for (int j=-1; j<=1; j++){
				if(pos.getX()+i>=0 && pos.getX()+i<Board.ROW_NUMBER){
					if(pos.getY()+j>=0 && pos.getY()+j<Board.COLUMN_NUMBER){
						Position p = new Position(pos.getX()+i,pos.getY()+j);
						Lokum l = Board.getInstance().getLokumAt(p);
						if (l != null && !pos.equals(p) && !list.contains(l)){
							if (l.isSpecialLokum()){
								specialLok.add(l);
							} else {
								list.add(l);
							}
						}
					}
				}
			}
		}
		for (int i=0; i<specialLok.size(); i++){
			list.add(specialLok.get(i));
		}
	}
}
