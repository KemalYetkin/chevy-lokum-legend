package engines;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import handlers.SubscriptionKeeper;
import cas.*;
import occupiers.*;


/**
 * GameEngine is a singleton class that controls logical operations of the game session
 * @author atilberk
 *
 */
public class GameEngine {
	/**
	 * Static Fields
	 */
	private static GameEngine instance;

	private Level level;
	private double score;
	private int movesLeft;
	private Lokum firstLokum;
	private boolean interactionEnabled;
	private Player player;
	private Position sPosition;
	private int completedSwapCounter;
	private double totalScore;
	private long timeLeft;
	private int specialSwapLeft;

	/**
	 * Constructor
	 */
	private GameEngine() {
		setLevel(null);
		setScore(-1);
		setMovesLeft(-1);
		setFirstLokum(null);
		setPlayer(null);
		disableInteraction();
		completedSwapCounter=0;
	}

	/**
	 * Returns the singleton instance of GameEngine
	 * 
	 * @requires 
	 * @modifies GameEngine.instance if it has not been initialized yet
	 * @ensures GameEngine.instance is instantiated if it has not been yet
	 * @return GameEngine instance
	 */
	public static GameEngine getInstance() {
		if (instance == null)
			instance = new GameEngine();
		return instance;
	}

	/**
	 * Resets the engine according to the given level
	 * 
	 * @param Level
	 * @requires 
	 * @modifies All fields of GameEngine instance
	 * @ensures GameEngine instance is reset according to level
	 * @return
	 */
	public void reset(Level level) {
		createNewGame(level);
	}

	/**
	 * Sets the first lokum
	 * 	 
	 * @param Position of the first lokum
	 * @requires instance.firstLokum == null
	 * 			 position != null
	 * 			 getLokumAt(position) != null
	 * @modifies instance.firstLokum
	 * @ensures instance.firstLokum is set to the Lokum object at the position
	 * @return
	 */
	public void chooseLokum(Position position) {
		//System.out.println(Board.getInstance().toString());
		//System.out.println("first lokum: "+position.getX()+","+position.getY()+","+Board.getInstance().getLokumAt(position).getColor());
		setFirstLokum(Board.getInstance().getLokumAt(position));

		GUIEngine.getInstance().getPlayGUI().getBoardGUI().glow(true);
	}

	/**
	 * Swaps the chosen first lokum with the lokum at the given position
	 * 	 
	 * @param Position of the second lokum
	 * @requires instance.firstLokum != null
	 * 			 sPosition != null
	 * 			 sPosition != instance.fistLokum.getPosition()
	 * @modifies instance.firstLokum
	 * 			 comboCount = 0
	 * @ensures instance.firstLokum is set to null, two lokums are swapped if possible
	 * @return
	 */
	private void swapWith(Position sPosition) {
		disableInteraction();
		this.sPosition = sPosition;
		SubscriptionKeeper.getInstance().setComboCount(0);
		boolean swappable = Board.getInstance().testSwap(firstLokum.getPosition(), sPosition);
		System.out.println("swappable: " + swappable);
		if (swappable) {
			//System.out.println("swappable");
			Position fPosition = firstLokum.getPosition();
			//System.out.println(Board.getInstance().toString());

			GUIEngine.getInstance().swapSLokums(fPosition, sPosition);
		} else {
			//System.out.println("not swappable");
			completedSwapCounter = 0;
			deselectLokum();
			enableInteraction();
		}
		//System.out.println(Board.getInstance().toString());
	}
	
	public void saveGame(String gamePath){
		GameSaver.getInstance().saveCurrentGame(gamePath);
	}
	
	public void saveSession(){
		SessionSaver.getInstance().saveSession();
	}

	/** 	 
	 * @requires firstLokum != null
	 * 			 sPosition != null
	 * @modifies completedSwapCounter
	 * 			 Board.getInstance().getBoardMatrix()
	 * @ensures there is no possible combination
	 * @return
	 */
	
	public void completeSwap(){
		completedSwapCounter++;
		if (completedSwapCounter == 2){
			completedSwapCounter = 0;
			Position fPosition = firstLokum.getPosition();
			Lokum secondLokum = Board.getInstance().getLokumAt(sPosition);
			Board.getInstance().setLokum(secondLokum, fPosition);
			Board.getInstance().setLokum(firstLokum, sPosition);
			SubscriptionKeeper.getInstance().swapped(firstLokum, secondLokum);

			checkGame();
			deselectLokum();
			GUIEngine.getInstance().getPlayGUI().getBoardGUI().refreshBoard();
			enableInteraction();
		}
		//if (completedSwapCounter == 0){}
		//GUIEngine.getInstance().getPlayGUI().getBoardGUI().setAndDrawBoard(Board.getInstance().getRepresentationMatrix());
		
	}

	/**
	 * Deselects the chosen lokum
	 * 	 
	 * @requires instance.firstLokum != null
	 * @modifies instance.firstLokum
	 * @ensures instance.firstLokum is set to null
	 * @return
	 */
	public void deselectLokum() {
		//System.out.println("first lokum: null");

		GUIEngine.getInstance().getPlayGUI().getBoardGUI().glow(false);

		setFirstLokum(null);
	}


	/**
	 * Enables the interaction with GUI
	 * 	 
	 * @param 
	 * @requires instance.interactionEnabled is initialised
	 * @modifies instance.interactionEnabled
	 * @ensures instance.interactionEnabled is set to false
	 * @return 
	 */
	public void enableInteraction() {
		this.interactionEnabled = true;
	}

	/**
	 * Disables the interaction with GUI
	 * 	 
	 * @param 
	 * @requires instance.interactionEnabled is initialised
	 * @modifies instance.interactionEnabled
	 * @ensures instance.interactionEnabled is set to true
	 * @return 
	 */
	public void disableInteraction() {
		this.interactionEnabled = false;
	}

	/**
	 * Creates a new game of given level
	 * 	 
	 * @param level
	 * @requires level is a legal level
	 * @modifies  All fields of GameEngine instance
	 * @ensures  All fields of GameEngine instance is set according to the given level
	 * @return
	 */
	public void createNewGame(Level level) {
		setLevel(level);
		setScore(0);
		setMovesLeft(level.getMaxMoves());
		setFirstLokum(null);
		Board.getInstance().newBoard();
		enableInteraction();
	}

	/**
	 * Loads the previously saved game
	 * 	 
	 * @param
	 * @requires
	 * @modifies  All fields of GameEngine instance
	 * @ensures  All fields of GameEngine instance is set according to the previous game session
	 * 				if there is not a saved game, creates a new game from the maximum level possible
	 * @return	 
	 */

	public void createLoadedGame(String path){			
		GameLoader.getInstance().loadGame(path);		
		setLevel(GameLoader.getInstance().getLevel());
		setScore(GameLoader.getInstance().getScore());
		setMovesLeft(GameLoader.getInstance().getMovesLeft());		
		setSpecialSwapsLeft(GameLoader.getInstance().getSpecialSwapsLeft());
		setTimeLeft(GameLoader.getInstance().getTimeLeft());
		setFirstLokum(null);
		Board.getInstance().loadBoard(GameLoader.getInstance().getOccupierMatrix());
		enableInteraction();
	}

	/**
	 * Returns the current level of the game session
	 * 	 
	 * @param 
	 * @requires instance.level != null
	 * @modifies 
	 * @ensures
	 * @return
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Sets the level of the game session
	 * 	 
	 * @param Level
	 * @requires 
	 * @modifies instance.level
	 * @ensures instance.level is set to the given level
	 * @return
	 */
	private void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * Returns the current score of the game session
	 * 	 
	 * @param 
	 * @requires instance.score != null
	 * @modifies 
	 * @ensures
	 * @return
	 */
	public double getScore() {
		return score;
	}

	/**
	 * Sets the score of the game session
	 * 	 
	 * @param Score
	 * @requires 
	 * @modifies instance.score
	 * @ensures instance.score is set to the given score
	 * @return
	 */
	private void setScore(double score) {
		this.score = score;
	}

	/**
	 * Returns the current number of moves left of the game session
	 * 	 
	 * @param 
	 * @requires instance.movesLeft != null
	 * @modifies 
	 * @ensures
	 * @return
	 */
	public int getMovesLeft() {
		return movesLeft;
	}

	/**
	 * Sets the current number of moves left of the game session
	 * 	 
	 * @param Moves Left
	 * @requires 
	 * @modifies instance.movesLeft
	 * @ensures instance.movesLeft is set to the given number of moves left
	 * @return
	 */
	private void setMovesLeft(int movesLeft) {
		this.movesLeft = movesLeft;
	}

	/**
	 * Returns the currently selected first lokum of the game session
	 * 
	 * @param 
	 * @requires instance.firstLokum != null
	 * @modifies 
	 * @ensures 
	 * @return
	 */
	public Lokum getFirstLokum() {
		return firstLokum;
	}

	/**
	 * Sets the currently selected first lokum of the game session
	 * 	 
	 * @param First Lokum
	 * @requires 
	 * @modifies instance.firstLokum
	 * @ensures instance.firstLokum is set to the given lokum
	 * @return
	 */
	private void setFirstLokum(Lokum firstLokum) {
		this.firstLokum = firstLokum;
	}

	/**
	 * Increments the score by the given amount (give negative amount to decrement)
	 * 	 
	 * @param Amount
	 * @return 
	 * @requires instance.score != null
	 * @modifies instance.score
	 * @ensures instance.score = old(instance.score) + amount
	 * 			old: old state of the instance
	 * @return
	 */
	public void updateScoreBy(double amount) {
		setScore(getScore() + amount);
	//	GUIEngine.getInstance().getPlayGUI().setScore(getScore()+"");
	}

	/**
	 * Decrements the score by the given amount (give negative amount to increment)
	 * 	 
	 * @param Amount
	 * @return 
	 * @requires instance.movesLeft != null
	 * @modifies instance.movesLeft
	 * @ensures instance.movesLeft = old(instance.movesLeft) + amount
	 * 			old: old state of the instance
	 * @return
	 */
	public void updateMovesLeftBy(int amount) {
		setMovesLeft(getMovesLeft() - amount);
	//	GUIEngine.getInstance().getPlayGUI().setMovesLeft(getMovesLeft()+"");
	}

	/**
	 * Checks if the goal for the level is satisfied or game is over unsuccessfully
	 * 	 
	 * @param
	 * @requires instance.level != null, 
	 * @modifies
	 * @ensures 
	 * @return 
	 */
	public boolean checkGame() {
		if (level.getGoal().goalReached()) {
			gameWon();
			return false;
		} else if (getMovesLeft() <= 0) {
			gameLost();
			return false;
		}
		return true;
	}

	/**
	 * Handles the clicks on BoardGUI
	 * 	 
	 * @param Position of SquareOccupier
	 * @requires
	 * @modifies
	 * @ensures
	 * @return
	 */
	public void lokumClicked(Position position) {
		//System.out.println("interaction: " + interactionEnabled);
		if (position != null && interactionEnabled) {

			if (getFirstLokum() == null)
				chooseLokum(position);
			else if (Board.getInstance().getLokumAt(position).equals(getFirstLokum()))
				deselectLokum();
			else {
				//GUIEngine.getInstance().getPlayGUI().getBoardGUI().printBoardGUI();
				//System.out.println("Here is the board:\n"+Board.getInstance().toString());

				swapWith(position);
			}
		}
	}

	/**
	 * When Game is Lost
	 */
	private void gameLost() {
		disableInteraction();
		GUIEngine.getInstance().gameIsOverLosingSituation();		
	}

	/**
	 * When Game is Won
	 */
	private void gameWon() {
		disableInteraction();
		increaseTotalScore(getScore());	
		saveSession();				
		GUIEngine.getInstance().gameIsOverWinningSituation();
	}

	public boolean repOK() {
		if (score<-1) {
			return false;
		} else if (movesLeft<-1) {
			return false;
		} else if (instance==null){
			return false;
		} else if ((score==-1 && movesLeft!=-1) || (score!=-1 && movesLeft==-1)) {
			return false;
		} else if (level.getMaxMoves()<movesLeft) {
			return false;
		}
		return true;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public double getTotalScore() {
		return totalScore;
	}

	private void increaseTotalScore(double Score) {
		this.totalScore += Score;
	}

	public Position getsPosition() {
		return sPosition;
	}
	
	public void setsPosition(Position pos) {
		this.sPosition = pos;
	}
	
	public int getCompletedSwapCounter() {
		return completedSwapCounter;
	}
	
	public void increaseRemainingTimeBy(long increment){
		timeLeft += increment;
	}

	public void decreaseRemainingTimeBy(long decrement){
		timeLeft -= decrement;
	}
	
	public long getTimeLeft(){
		return timeLeft;
	}
	
	public int getSpecialSwapsLeft(){
		return specialSwapLeft;
	}
	
	private void setTimeLeft(long time){
		timeLeft = time;
	}
	
	private void setSpecialSwapsLeft(int specialSwap){
		specialSwapLeft = specialSwap;
	}
	
	public void decreaseSpecialSwapLeft(){
		--specialSwapLeft;
	}
	
}
