package engines;

import java.awt.event.*;
import javax.swing.Timer;
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
	private boolean thisWillBeSpecialSwap;
	private Timer timer;

	/**
	 * Constructor
	 */
	private GameEngine() {
		disableSpecialSwap();
		setLevel(null);
		setScore(-1);
		setMovesLeft(-1);
		setFirstLokum(null);
		setPlayer(null);
		disableInteraction();
		completedSwapCounter=0;
		setTimeLeft(-1);
		timer = new Timer(1000, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				decreaseRemainingTimeBy(1);
				int c = SubscriptionKeeper.getInstance().getTimeBonusCount();
				if(c != 0) {
					GUIEngine.getInstance().getPlayGUI().setTimeLeft(timeLeft-c*TimeLokum.defaultTime + " + " + c*TimeLokum.defaultTime);
					SubscriptionKeeper.getInstance().setTimeBonusCount(0);
				} else {
					GUIEngine.getInstance().getPlayGUI().setTimeLeft(timeLeft+"");
				}

				if (timeLeft == 0 )
					gameLost();
			}
		});
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
		setFirstLokum(Board.getInstance().getLokumAt(position));
		GUIEngine.getInstance().getPlayGUI().getBoardGUI().glow(true);
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
			AudioPlayers.getInstance().playFallingDoneEffect(true);
			deselectLokum();
			GUIEngine.getInstance().getPlayGUI().getBoardGUI().refreshBoard();
			enableInteraction();
		}
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
		if(level.getGoal() instanceof TimeScoreGoal)
			setTimeLeft(((TimeScoreGoal)level.getGoal()).getTimeGoal());
		else
			setTimeLeft(-1);

		setScore(0);
		setMovesLeft(level.getMaxMoves());
		setFirstLokum(null);
		disableSpecialSwap();
		setSpecialSwapsLeft(level.getSpecialSwapCounter());
		Board.getInstance().newBoard();
		enableInteraction();
		startTimer();
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
		disableSpecialSwap();
		GameLoader.getInstance().loadGame(path);		
		setLevel(GameLoader.getInstance().getLevel());
		setScore(GameLoader.getInstance().getScore());
		setMovesLeft(GameLoader.getInstance().getMovesLeft());		
		setSpecialSwapsLeft(GameLoader.getInstance().getSpecialSwapsLeft());
		setTimeLeft(GameLoader.getInstance().getTimeLeft());
		setFirstLokum(null);
		Board.getInstance().loadBoard(GameLoader.getInstance().getOccupierMatrix());
		enableInteraction();
		startTimer();
	}

	/**
	 * Starts the timer of the game
	 * 
	 * @param
	 * @requires timeLeft != -1
	 * @modifies
	 * @ensures timer is started
	 * @return
	 */
	public void startTimer(){
		if (getTimeLeft() != -1)
			timer.start();
	}

	/**
	 * Stops the timer of the game
	 * 
	 * @param
	 * @requires
	 * @modifies
	 * @ensures timer is stopped
	 * @return
	 */
	public void stopTimer(){
		if (timer.isRunning())
			timer.stop();
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
	 * @returns
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
		GUIEngine.getInstance().getPlayGUI().setScore(getScore()+"");
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
		GUIEngine.getInstance().getPlayGUI().setMovesLeft(getMovesLeft()+"");
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
		if (position != null && interactionEnabled) {

			if (getFirstLokum() == null)
				chooseLokum(position);
			else if (Board.getInstance().getLokumAt(position).equals(getFirstLokum()))
				deselectLokum();
			else {
				swapWith(position);
			}
		}
	}

	/**
	 * When Game is Lost
	 */
	private void gameLost() {
		stopTimer();
		disableInteraction();
		GUIEngine.getInstance().gameIsOverLosingSituation();		
	}

	/**
	 * When Game is Won
	 */
	private void gameWon() {
		stopTimer();
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

	private void swapWith(Position pSecond) {
		disableInteraction();
		boolean swappable;
		if (thisWillBeSpecialSwap){
			swappable = true;
			decreaseSpecialSwapLeft();
			GUIEngine.getInstance().specialSwapButtonClicked();
			GUIEngine.getInstance().displayNewSpecialSwapLeft();
		} else {
			swappable = Board.getInstance().testSwap(firstLokum.getPosition(), pSecond);
		}

		if (swappable) {
			Position pFirst = getFirstLokum().getPosition();
			Lokum secondLokum = Board.getInstance().getLokumAt(pSecond);
			Board.getInstance().setLokum(secondLokum, pFirst);
			Board.getInstance().setLokum(getFirstLokum(), pSecond);

			System.out.println("Swapping...");

			GUIEngine.getInstance().swapSLokums(pFirst, pSecond);
			SubscriptionKeeper.getInstance().swapped(getFirstLokum(), secondLokum);
		}
		else{
			AudioPlayers.getInstance().playIncorrectSelectionEffect(true);
		}
		deselectLokum();
		GUIEngine.getInstance().initiateAnimations();
	}

	public void enableSpecialSwap(){
		thisWillBeSpecialSwap = true;
	}

	public void disableSpecialSwap(){
		thisWillBeSpecialSwap = false;
	}
}
