package handlers;

import java.util.ArrayList;
import occupiers.ColorBombLokum;
import occupiers.Lokum;
import cas.Board;
import cas.Position;
import engines.GameEngine;

/**
 * 
 * @author atilberk
 * Class: Handles the swap of one ColorBombLokum and one wrapped lokum
 */
public class ColorBombExploder implements ExplodeListener {

	public ColorBombExploder() {}

	@Override
	/**
	 * @requires e.getLokum() is not null
	 * @ensures if colorbomb, all lokums with one random color is added to the explosion queue and the list triggered
	 */
	public void explode(ExplodeEvent e) {
		Lokum lokum = e.getLokum();
		if (lokum instanceof ColorBombLokum) {
			ArrayList<Lokum> lokumsToBeExploded = new ArrayList<Lokum>();
			ColorBombLokum cLokum = (ColorBombLokum) lokum;
			Position pos = cLokum.getPosition();
			String color = chooseRandomExplodeColor(pos);
			explodeListFiller(color,lokumsToBeExploded);
			Board.getInstance().removeLokumAt(pos);
			SubscriptionKeeper.getInstance().explode(lokumsToBeExploded);
				
			int comboCount = SubscriptionKeeper.getInstance().getComboCount();
			GameEngine.getInstance().updateScoreBy(Math.pow(2,comboCount)*Math.pow(lokumsToBeExploded.size(),2)*60);
		}
	}

	private void explodeListFiller(String color, ArrayList<Lokum> list){
		for (int i=0; i<Board.ROW_NUMBER; i++) {
			for (int j=0; j< Board.COLUMN_NUMBER; j++){
				Lokum l = Board.getInstance().getLokumAt(new Position(i,j));
				if (l != null && l.getColor().equals(color))
					list.add(l);
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


