package frames;

import java.awt.Dimension;
import javax.swing.JPanel;
import cas.Board;
import cas.Position;
import engines.GUIEngine;
import engines.GameEngine;
import occupiers.LokumDescription;
import occupiers.SquareOccupierDescription;
import scomponents.SColor;
import scomponents.SLabel;
import scomponents.SLokum;
import scomponents.SOccupier;

@SuppressWarnings("serial")
public class BoardGUI extends JPanel {

	private SOccupier[][] occupiers;
	private SLabel glowing = new SLabel(SLabel.GLOW);

	public BoardGUI() {
		super();
		this.setLayout(null);
		this.setMinimumSize(new Dimension(600,600));
		this.setPreferredSize(new Dimension(600,600));
		this.setBackground(SColor.boardBackgroundColor);
		setAndDrawBoard(Board.getInstance().getRepresentationMatrix());
		add(glowing);
	}

	public void addSOccupier(SOccupier s, int x, int y) {
		super.add(s);
		s.setBounds(x-s.width()/2, y-s.height()/2, s.width(), s.height());
	}

	public void glow(boolean visibility) {
		System.out.println("here");
		glowing.setVisible(visibility);
		repaint();
		if (visibility)
			glowing.setLocation(getSLokumAt(GameEngine.getInstance().getFirstLokum().getPosition()).getLocation());
	}

	public void setAndDrawBoard(SquareOccupierDescription[][] representationMatrix) {
		occupiers = new SOccupier[representationMatrix.length][representationMatrix[0].length];
		for (int y = 0; y < representationMatrix.length; y++) {
			for (int x = 0; x < representationMatrix[0].length; x++) {
				SquareOccupierDescription desc = representationMatrix[y][x];
				if (desc instanceof LokumDescription) {
					insertSLokum(new SLokum(desc.getType(), ((LokumDescription) desc).getColor()), new Position(x,y));
				}
			}
		}
	}

	public void refreshBoard() {
		removeAll();
		SquareOccupierDescription[][] repMat = Board.getInstance().getRepresentationMatrix();
		for (int y = 0; y < repMat.length; y++) {
			for (int x = 0; x < repMat[0].length; x++) {
				SquareOccupierDescription desc = repMat[y][x];
				if (desc instanceof LokumDescription) {
					putSLokum(new SLokum(desc.getType(), ((LokumDescription) desc).getColor()), new Position(x,y));
				}
			}
		}
	}

	public void insertSLokum(SLokum sl, Position pos) {
		this.setSLokum(sl, pos);
		this.addSOccupier(sl, sl.width()+(pos.getX()*sl.width()), sl.height()+(pos.getY()*sl.height()));
	}

	public void putSLokum(SLokum sl, Position pos) {
		this.setSLokum(sl, pos);
		super.add(sl);
		sl.setBounds((sl.width()/2)+(pos.getX()*sl.width()), (sl.height()/2)+(pos.getY()*sl.height()), sl.width(), sl.height());
	}

	public void setSLokum(SLokum sl, Position pos) {
		occupiers[pos.getY()][pos.getX()] = sl;
	}

	public void removeSLokumAt(Position pos) {
		if (occupiers[pos.getY()][pos.getX()] != null) {
			this.remove(occupiers[pos.getY()][pos.getX()]);
			repaint();
			occupiers[pos.getY()][pos.getX()] = null;
		}
	}

	public SLokum getSLokumAt(Position pos) {
		SOccupier sl = occupiers[pos.getY()][pos.getX()];
		return (sl instanceof SLokum) ? (SLokum) sl : null;
	}

	public void printBoardGUI() {
		for (int y = 0; y < occupiers.length; y++) {
			for (int x = 0; x < occupiers[0].length; x++) {
				SLokum l = (SLokum) occupiers[y][x];
				System.out.print("["+((l == null) ? "*": ((l.getColor().length()>=1) ? l.getColor().charAt(0) : "  "))+"] ");

			}
			System.out.println();
		}
		System.out.println();
	}

	public void moveSLokums(Position p1, Position p2 , int tag) {

		GUIEngine.getInstance().addToAnimationQueue(null, p1, p2, 4 , tag);
	}

	public void moveSLokum(Position pre, Position post , int tag) {

		GUIEngine.getInstance().addToAnimationQueue(null, pre, post, 1 , tag);
	}

}
