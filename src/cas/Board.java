package cas;

import handlers.Match3SDetector;
import handlers.SubscriptionKeeper;

import java.util.ArrayList;

import engines.GUIEngine;
import engines.GameEngine;
import occupiers.*;
import scomponents.SLokum;
import scomponents.SLokum.whatWillBeTriggeredAfterThisLokum;

public class Board {
	private static Board instance = null;
	private SquareOccupier[][] boardMatrix;
	public static final int ROW_NUMBER = 9;
	public static final int COLUMN_NUMBER = 9;
	private int currentGenerationForRow;
	private int currentGenerationForColumn;
	private ArrayList<Lokum> findMatchCheckList;
	private SLokum currentLastGenerated;
	private int previousTotalCounter;

	/**
	 * constructor of the class
	 */
	private Board() {
		boardMatrix = new SquareOccupier[ROW_NUMBER][COLUMN_NUMBER];
		currentGenerationForRow = Board.ROW_NUMBER - 1;
		currentGenerationForColumn = 0;
		previousTotalCounter = 0;
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

	/**
	 * @ensures fill the empty spaces formed after an explosion occurs by
	 *          sliding the Lokums found above the exploded and emptied area.
	 * @modifies boardMatrix
	 */	
	public void fallLokumsToEmptySquares() {
		System.out.println(Board.getInstance().toString());

		int totalNullCounter = 0;
		for(int x = 0; x<Board.COLUMN_NUMBER; x++){
			for(int z = Board.ROW_NUMBER - 1; z >= 0; z--){
				if (instance.getLokumAt(new Position(x, z)) == null)
					totalNullCounter++;
			}
		}
		int counter = 0;
		for (int x = 0; x < Board.COLUMN_NUMBER; x++) {
			
			for (int y = Board.ROW_NUMBER - 1; y >= 0; y--) {
				if (instance.getLokumAt(new Position(x, y)) == null) {
					for (int k = y - 1; k >= 0; k--) {
						SquareOccupier occupier = instance.getLokumAt(new Position(x, k));
						if (occupier != null && occupier instanceof Lokum) {
							counter++;
							Lokum lokum = (Lokum) occupier;
						//	SLokum sl1 = GUIEngine.getInstance().getPlayGUI().getBoardGUI().getSLokumAt(new Position(x, k));
							//GUIEngine.getInstance().getPlayGUI().getBoardGUI().setSLokum(null, new Position(x, k));
							//GUIEngine.getInstance().getPlayGUI().getBoardGUI().setSLokum(sl1, new Position(x, y));
							instance.removeLokumAt(new Position(x, k));
							instance.setLokum(lokum, new Position(x, y));
							//sl1.setTriggerType(whatWillBeTriggeredAfterThisLokum.GENERATION);
							//sl1.setTotalTravelDistance(y-k);
							//sl1.moveToPosition(new Position(x, y));
							//	//System.out.println("findMatchCheckList increased in fall");
							findMatchCheckList.add(lokum);
							break;
						}
					}
				}
			}
		}
		if (counter != totalNullCounter) generateLokumsToEmptySquares();
	}

	/**
	 * @ensures The spaces formed by sliding the Lokums to the empty parts are
	 *          filled with new random Lokums
	 * @modifies boardMatrix
	 */
	public void generateLokumsToEmptySquares(){
		int totalNullCounter = 0;
		for(int x = 0; x<Board.COLUMN_NUMBER; x++){
			for(int z = Board.ROW_NUMBER - 1; z >= 0; z--){
				if (instance.getLokumAt(new Position(x, z)) == null)
					totalNullCounter++;
			}
		}

		boolean flag = false;
		for (int x = currentGenerationForColumn; x < Board.COLUMN_NUMBER; x++) {
			for (int z = currentGenerationForRow; z >= 0; z--) {
				if (instance.getLokumAt(new Position(x, z)) == null) {
					// currentGenerationForRow : z
					// currentGenerationForColumn : x
					Lokum lokum = SquareOccupierFactory.getInstance().generateRandomRegularLokum(new Position(x, z));					
					SLokum sl = new SLokum(lokum.getDescription().getType(),((LokumDescription) lokum.getDescription()).getColor());
				//	GUIEngine.getInstance().getPlayGUI().getBoardGUI().insertSLokum(sl, new Position(x, z));
					instance.setLokum(lokum, new Position(x, z));
					//sl.setBounds((sl.width()/2) + (x * sl.width()), (sl.height()/2) + (0 * sl.height()), sl.width(), sl.height());
					//sl.setTriggerType(whatWillBeTriggeredAfterThisLokum.FINDMATCHES);
					//	System.out.println("totalNullCounter " + totalNullCounter);
					if(totalNullCounter==1 || previousTotalCounter < totalNullCounter){
						previousTotalCounter = totalNullCounter;
						replaceLastGenerated(sl);
					}
					sl.moveToPosition(new Position(x, z));
					findMatchCheckList.add(lokum);
					//	//System.out.println("findMatchCheckList increased in generateLokumsToEmptySquares");
					flag = true;
					break;
				}
			}
			if (flag) break;
		}
	}
	/**
	 * @ensures there is no combination at board matrix
	 * @modifies boardMatrix
	 */
	public void findAllMatches(){
		while (!findMatchCheckList.isEmpty())
			SubscriptionKeeper.getInstance().findMatch(findMatchCheckList.remove(0));
		//System.out.println(toString());
		SubscriptionKeeper.getInstance().setComboCount(SubscriptionKeeper.getInstance().getComboCount()+1);
		while (!findMatchCheckList.isEmpty())
			SubscriptionKeeper.getInstance().findMatch(findMatchCheckList.remove(0));
		currentGenerationForRow = Board.ROW_NUMBER - 1;
		currentGenerationForColumn = 0;
		GameEngine.getInstance().checkGame();
		//System.out.println(toString());
	}

	/**
	 * @ensures boardMatrix is generated according to the level of the game
	 * @modifies boardMatrix
	 */
	public void newBoard() {
		reset();
		Level level = GameEngine.getInstance().getLevel();			

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
	 * @ensures the boardMatrix field of the class is set to the given
	 *          boardMatrix
	 * @modifies boardMatrix
	 * @param boardMatrix
	 *            the new game board
	 */
	private void setBoardMatrix(SquareOccupier[][] boardMatrix) {
		this.boardMatrix = boardMatrix;
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
		////System.out.println(position == null ? "p1: null" : "p1: val");
		////System.out.println(position2 == null ? "p2: null" : "p2: val");
		Lokum l1 = getLokumAt(position);
		Lokum l2 = getLokumAt(position2);
		////System.out.println("test subj 1: ("+position.getX()+","+position.getY()+")");
		////System.out.println("test subj 2: ("+position2.getX()+","+position2.getY()+")");
		////System.out.println(Board.getInstance().toString());
		if (l1 == null || l2 == null){
			//System.out.println("biri null");
			return false;
		}
		if (Math.abs(position.getX() - position2.getX()) > 1
				|| Math.abs(position.getY() - position2.getY()) > 1) {
			//System.out.println("uzaklar");
			return false;
		}
		if (l1 instanceof ColorBombLokum || l2 instanceof ColorBombLokum){
			//System.out.println("biri null");
			return true;
		}
		if (l1.isSpecialLokum() && l2.isSpecialLokum()){
			//System.out.println("ikisi de special");
			return true;
		}
		if (l1.getColor().equals(l2.getColor())){
			//System.out.println("renkler aynı");
			return false;
		}
		if (doesSimulationContainsAnyThreeSequence(position, position2)) {
			//System.out.println("match var");
			return true;
		}
		//System.out.println("bir şeyi atlıyoruz");
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
		/*
		return (analyzeThreeSequenceAroundTheSwappedLokums(p1, p2)
				|| analyzeThreeSequenceAroundTheSwappedLokums(p2, p1));
				*/
	}

	/**
	 * @param p1
	 *            the position of the first Lokum tested for being swapped
	 * @param p2
	 *            the position of the second Lokum tested for being swapped
	 * @return true if swapping of the Lokums at the given positions forms a
	 *         three sequence false if swapping of the Lokums at the given
	 *         positions doesn't form any three sequence
	 */
	private boolean analyzeThreeSequenceAroundTheSwappedLokums(Position p1,	Position p2) {
		return false;
		/*
		if (p1.getY() >= 1 && boardMatrix[p1.getY() - 1][p1.getX()] != null
				&& boardMatrix[p2.getY()][p2.getX()].isLokum()
				&& boardMatrix[p1.getY() - 1][p1.getX()].isLokum()) {
			if (((Lokum) boardMatrix[p2.getY()][p2.getX()]).getColor().equals(((Lokum) boardMatrix[p1
			                                                                                   .getY() - 1][p1.getX()]).getColor())) {
				if (p1.getY() >= 2
						&& boardMatrix[p1.getY() - 2][p1.getX()] != null
						&& boardMatrix[p1.getY() - 2][p1.getX()].isLokum()
						&& ((Lokum) boardMatrix[p2.getY()][p2.getX()])
						.getColor().equals(((Lokum) boardMatrix[p1.getY() - 2][p1
						                                                   .getX()]).getColor()))
					return true;

				if (p1.getY() <= ROW_NUMBER - 2
						&& boardMatrix[p1.getY() + 1][p1.getX()] != null
						&& boardMatrix[p1.getY() + 1][p1.getX()].isLokum()
						&& ((Lokum) boardMatrix[p2.getY()][p2.getX()])
						.getColor().equals(((Lokum) boardMatrix[p1.getY() + 1][p1
						                                                   .getX()]).getColor()))
					return true;
			}
		}

		if (p1.getY() <= ROW_NUMBER - 3
				&& boardMatrix[p1.getY() + 1][p1.getX()] != null
				&& boardMatrix[p2.getY()][p2.getX()].isLokum()
				&& boardMatrix[p1.getY() + 1][p1.getX()].isLokum()) {
			if (((Lokum) boardMatrix[p2.getY()][p2.getX()]).getColor().equals(((Lokum) boardMatrix[p1
			                                                                                   .getY() + 1][p1.getX()]).getColor())) {
				if (boardMatrix[p1.getY() + 2][p1.getX()] != null
						&& boardMatrix[p1.getY() + 2][p1.getX()].isLokum()
						&& ((Lokum) boardMatrix[p2.getY()][p2.getX()])
						.getColor().equals(((Lokum) boardMatrix[p1.getY() + 2][p1
						                                                   .getX()]).getColor()))
					return true;
			}
		}

		if (p1.getX() >= 1 && boardMatrix[p1.getY()][p1.getX() - 1] != null
				&& boardMatrix[p2.getY()][p2.getX()].isLokum()
				&& boardMatrix[p1.getY()][p1.getX() - 1].isLokum()) {
			if (((Lokum) boardMatrix[p2.getY()][p2.getX()]).getColor().equals(((Lokum) boardMatrix[p1
			                                                                                   .getY()][p1.getX() - 1]).getColor())) {

				if (p1.getX() >= 2
						&& boardMatrix[p1.getY()][p1.getX() - 2] != null
						&& boardMatrix[p1.getY()][p1.getX() - 2].isLokum()
						&& ((Lokum) boardMatrix[p2.getY()][p2.getX()])
						.getColor().equals(((Lokum) boardMatrix[p1.getY()][p1
						                                               .getX() - 2]).getColor()))

					return true;
				if (p1.getX() <= COLUMN_NUMBER - 2
						&& boardMatrix[p1.getY()][p1.getX() + 1] != null
						&& boardMatrix[p1.getY()][p1.getX() + 1].isLokum()
						&& ((Lokum) boardMatrix[p2.getY()][p2.getX()])
						.getColor().equals(((Lokum) boardMatrix[p1.getY()][p1
						                                               .getX() + 1]).getColor()))
					return true;
			}
		}

		if (p1.getX() <= COLUMN_NUMBER - 3
				&& boardMatrix[p1.getY()][p1.getX() + 1] != null
				&& boardMatrix[p2.getY()][p2.getX()].isLokum()
				&& boardMatrix[p1.getY()][p1.getX() + 1].isLokum()) {
			if (((Lokum) boardMatrix[p2.getY()][p2.getX()]).getColor().equals(((Lokum) boardMatrix[p1
			                                                                                   .getY()][p1.getX() + 1]).getColor())) {
				if (boardMatrix[p1.getY()][p1.getX() + 2] != null
						&& boardMatrix[p1.getY()][p1.getX() + 2].isLokum()
						&& ((Lokum) boardMatrix[p2.getY()][p2.getX()])
						.getColor().equals(((Lokum) boardMatrix[p1.getY()][p1
						                                               .getX() + 2]).getColor()))
					return true;
			}
		}
		return false;
		*/
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
			getLokumAt(p).setPosition(null);
			setLokum(null, p);
		}
	}

	/*
	 * public void makeBoardPlayable() { for (int y = 0; y < boardMatrix.length;
	 * y++) { for (int x = 0; x < boardMatrix[0].length; x++) {
	 * 
	 * } } }
	 */

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

	/**
	 * @ensures the last generated Lokum's representation is conserved
	 * @modifies currentLastGenerated Lokum
	 * @param sl the representation of the Lokum which is lastly generated
	 * 		  upon deletion of at least a three sequence
	 */
	private void replaceLastGenerated(SLokum sl){
		if (currentLastGenerated != null) 
			currentLastGenerated.setLastGenerationStatus(false);
		currentLastGenerated = sl;
		currentLastGenerated.setLastGenerationStatus(true);
	}
}
