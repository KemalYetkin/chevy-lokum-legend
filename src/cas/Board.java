package cas;

import handlers.Match3SDetector;
import handlers.SubscriptionKeeper;
import java.util.ArrayList;
import cas.Position;
import engines.*;
import frames.BoardGUI;
import occupiers.*;
import scomponents.SLokum;

public class Board {
	private static Board instance = null;
	private SquareOccupier[][] boardMatrix;
	public static final int ROW_NUMBER = 9;
	public static final int COLUMN_NUMBER = 9;
	private ArrayList<Lokum> findMatchCheckList;

	/**
	 * constructor of the class
	 */
	private Board() {
		boardMatrix = new SquareOccupier[ROW_NUMBER][COLUMN_NUMBER];
		findMatchCheckList = new ArrayList<Lokum>();
	}

	/**
	 * @ensures if an instance has already been created before the call of this
	 *          method, returns that instance; if not, then create a new
	 *          instance of the class and return it.
	 * @modifies instance
	 * @return the instance of the class
	 */
	public static Board getInstance() {
		if (instance == null) {
			instance = new Board();
		}
		return instance;
	}

	public void fillDown(int tag) {
		BoardGUI gui = GUIEngine.getInstance().getPlayGUI().getBoardGUI();
		while(hasEmptySpaces()) {
			for(int x = 0; x < COLUMN_NUMBER; x++) {
				int k = 0;
				for(int y = ROW_NUMBER-1; y >= 0; y--){
					Position cur = new Position(x,y);
					SquareOccupier s = getOccupierAt(cur);
					if (s == null) {
						k = y-1;
						while (k >= 0 && getLokumAt(new Position(x,k)) == null){
							k--;
						}
						if (k < 0){
							Position top = new Position(x,0);
							System.out.println(this);
							System.out.println("occupier at "+x+",0:" + getOccupierAt(top));
							if (getOccupierAt(top) == null) {
								Lokum r = SquareOccupierFactory.getInstance().generateRandomRegularLokum(top);
								SLokum sl = new SLokum(r.getDescription().getType(),((LokumDescription) r.getDescription()).getColor());
								setLokum(r, top);
								GUIEngine.getInstance().addToAnimationQueue(sl, null, top,3, tag);
								findMatchCheckList.add(r);
							}
						} else {
							Position post = new Position(x,k+1);
							Position pre = new Position(x,k);
							gui.moveSLokum(pre, post , tag);
							Lokum l = getLokumAt(pre);
							removeLokumAt(pre);
							setLokum(l, post);
							findMatchCheckList.add(l);

							System.out.println(this);
						}
						break;
					}
				}
			}
		}
	}

	public SquareOccupier getOccupierAt(Position pos){
		return boardMatrix[pos.getY()][pos.getX()];
	}
	public boolean hasEmptySpaces(){
		for(int x = 0; x < COLUMN_NUMBER; x++) {
			for(int y = ROW_NUMBER-1; y >= 0; y--){
				if (boardMatrix[y][x] == null)
					return true;
			}
		}
		return false;
	}

	public void findAllMatches(){
		while (!findMatchCheckList.isEmpty())
			SubscriptionKeeper.getInstance().findMatch(findMatchCheckList.remove(0));
		
		fillDown(GUIEngine.getInstance().getNextTag());
		if (!findMatchCheckList.isEmpty())
			findAllMatches();

		SubscriptionKeeper.getInstance().setComboCount(SubscriptionKeeper.getInstance().getComboCount()+1);

		fillDown(GUIEngine.getInstance().getNextTag());
		if (!findMatchCheckList.isEmpty())
			findAllMatches();

		SubscriptionKeeper.getInstance().setComboCount(SubscriptionKeeper.getInstance().getComboCount()+1);
	}

	/**
	 * @ensures boardMatrix is generated according to the level of the game
	 * @modifies boardMatrix
	 */
	public void newBoard() {
		reset();
		for (int x = 0; x < boardMatrix.length; x++) {
			for (int y = 0; y < boardMatrix[0].length; y++) {
				if (boardMatrix[x][y] == null) {
					boardMatrix[x][y] = SquareOccupierFactory.getInstance()
							.generateRandomRegularLokum();
					((Lokum) boardMatrix[x][y]).setPosition(new Position(y, x));
					while (analyzeThreeSequenceAroundAnInitialLokum(((Lokum) boardMatrix[x][y])
							.getPosition())) {
						boardMatrix[x][y] = SquareOccupierFactory.getInstance()
								.generateRandomRegularLokum();
						((Lokum) boardMatrix[x][y]).setPosition(new Position(y, x));
					}
				}
			}
		}
	}

	/**
	 * @ensures the boardMatrix field of the class is set to the given
	 *          loadedMatrix
	 * @modifies boardMatrix
	 * @param loadedMatrix
	 *            the game board containing all saved Lokums and obstacles
	 */
	public void loadBoard(SquareOccupier[][] loadedMatrix) {
		boardMatrix = loadedMatrix;
	}

	/**
	 * @ensures the boardMatrix field is set to empty matrix.
	 * @modifies boardMatrix
	 */
	public void reset() {
		for (int x = 0; x < boardMatrix.length; x++) {
			for (int y = 0; y < boardMatrix[0].length; y++) {
				if (boardMatrix[x][y] != null) {
					boardMatrix[x][y] = null;
				}
			}
		}
	}

	/**
	 * @return boardMatrix
	 */
	public SquareOccupier[][] getBoardMatrix() {
		return boardMatrix;
	}

	/**
	 * @requires position != null & position2 != null & position and position2
	 *           must be positive integers and lower than or equal to the
	 *           maximum; the squareOccupiers at these positions must not be null
	 *           but be Lokum; they must be adjacent (either up-down, left-right 
	 *           or crosswise); the resultant matrix after the swapping must 
	 *           contain at least 3s.
	 * @ensures the Lokums at the given positions are swappable or not, then
	 *          returns appropriate boolean
	 * @param position
	 *            the position of the first Lokum to be tested for swapping
	 * @param position2
	 *            the position of the second Lokum to be tested for swapping
	 * @return true if they are swappable; false vice versa
	 */
	public boolean testSwap(Position position, Position position2) {
		Lokum l1 = getLokumAt(position);
		Lokum l2 = getLokumAt(position2);
		if (l1 == null || l2 == null){
			return false;
		}
		if (Math.abs(position.getX() - position2.getX()) > 1
				|| Math.abs(position.getY() - position2.getY()) > 1) {
			return false;
		}
		if (l1 instanceof ColorBombLokum || l2 instanceof ColorBombLokum){
			return true;
		}
		if (l1.isSpecialLokum() && l2.isSpecialLokum()){
			return true;
		}
		if (l1.getColor().equals(l2.getColor())){
			return false;
		}
		if (doesSimulationContainsAnyThreeSequence(position, position2)) {
			return true;
		}
		return false;
	}

	/**
	 * @requires p must not be null p.getX() and p.getY() must be lower than the
	 *           maximum values and bigger than 0 there must be a Lokum at the
	 *           given position, not an obstacle
	 * @ensures The Lokum at the given position is returned
	 * @param p
	 *            The position of the desired Lokum
	 * @return The Lokum found in the given position
	 */
	public Lokum getLokumAt(Position p) {
		if (p == null)
			return null;

		if (!Position.isValid(p))
			return null;

		if (boardMatrix[p.getY()][p.getX()] instanceof Lokum)
			return (Lokum) boardMatrix[p.getY()][p.getX()];
		else
			return null;
	}

	/**
	 * @param lokum
	 * @param p
	 */
	public void setLokum(Lokum lokum, Position p) {
		if (p == null)
			return;
		if (p.getY() >= ROW_NUMBER || p.getY() < 0 || p.getX() >= COLUMN_NUMBER
				|| p.getX() < 0)
			return;
		else {
			if (lokum != null)
				lokum.setPosition(p);
			boardMatrix[p.getY()][p.getX()] = lokum;
		}
	}

	/**
	 * @return boardMatrix as String
	 */
	public String toString() {
		String matrix = "";
		for (int x = 0; x < boardMatrix.length; x++) {
			for (int y = 0; y < boardMatrix[0].length; y++) {
				SquareOccupier s = boardMatrix[x][y];
				if (s == null) matrix += "[NULL] ";
				else matrix += "["+s.getDescription().getType().substring(0, 2).toUpperCase()+":"+
						(s instanceof Lokum ? (s instanceof ColorBombLokum ? "*" : ((Lokum) s).getColor().substring(0, 1).toLowerCase()) : "*")
						+"] ";
			}
			matrix += "\n";
		}
		return matrix;
	}

	/**
	 * @param p1
	 *            the position of the first Lokum tested for being swapped
	 * @param p2
	 *            the position of the second Lokum tested for being swapped
	 * @return true if swapping of the Lokums at the given positions forms a
	 *         three sequence; false vice versa
	 */
	private boolean doesSimulationContainsAnyThreeSequence(Position p1,Position p2) {
		Lokum l1 = getLokumAt(p1);
		Lokum l2 = getLokumAt(p2);
		setLokum(l1, p2);
		setLokum(l2, p1);
		boolean result = (((Match3SDetector.horizontalMatch(getLokumAt(p1),boardMatrix).size() > 1) ||
				(Match3SDetector.verticalMatch(getLokumAt(p1),boardMatrix).size() > 1)) ||
				((Match3SDetector.horizontalMatch(getLokumAt(p2),boardMatrix).size() > 1) ||
						(Match3SDetector.verticalMatch(getLokumAt(p2),boardMatrix).size() > 1)));
		setLokum(l1, p1);
		setLokum(l2, p2);
		return result;
	}

	/**
	 * @requires position given is a valid position
	 * @ensures analyze the board so that it contains at least one three sequence
	 * 			around the given position
	 * @param p the position that will be analyzed
	 * @return true if there is at least one three sequence around the position
	 * 		   false vice versa
	 */
	private boolean analyzeThreeSequenceAroundAnInitialLokum(Position p) {
		return doesSimulationContainsAnyThreeSequence(p, p);
	}

	/**
	 * @requires the given position is in the valid range
	 * @modifies boardMatrix
	 * @ensures the Lokum is deleted from the board
	 * @param p
	 *            the position of the Lokum that is going to be removed from the
	 *            game board
	 */
	public void removeLokumAt(Position p) {
		if (p.getY() < ROW_NUMBER && p.getY() >= 0 && p.getX() < COLUMN_NUMBER
				&& p.getX() >= 0) {
			if(getLokumAt(p) != null){
			getLokumAt(p).setPosition(null);
			setLokum(null, p);
			}
		}
	}

	/**
	 * @return the representation of the matrix by forming a new 2d matrix
	 *         containing each Lokum's representation
	 */
	public SquareOccupierDescription[][] getRepresentationMatrix() {
		SquareOccupierDescription matrix[][] = new SquareOccupierDescription[ROW_NUMBER][COLUMN_NUMBER];
		for (int y = 0; y < boardMatrix.length; y++) {
			for (int x = 0; x < boardMatrix[0].length; x++) {
				if (boardMatrix[y][x] != null)
					matrix[y][x] = getLokumAt(new Position(x, y))
					.getDescription();
				else
					matrix[y][x] = null;
			}
		}
		return matrix;
	}

	/**
	 * @ensures the matrix is not null and each position is filled with a Lokum
	 * @return true if the matrix have a correct form; false vice versa
	 */
	public boolean repOK() {
		if (instance == null) {
			return false;
		} else if (boardMatrix == null) {
			return false;
		}

		for(int i = 0; i < COLUMN_NUMBER; i++){
			for(int j = 0; j < ROW_NUMBER; j++){
				if(getLokumAt(new Position(i,j)) == null)
					return false;
			}
		}
		return true;
	}
}
