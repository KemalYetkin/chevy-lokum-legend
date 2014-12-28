package engines;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import scomponents.SLokum;
import scomponents.SLokum.whatWillBeTriggeredAfterThisLokum;
import cas.GameLoader;
import cas.Level;
import cas.Player;
import cas.Position;
import cas.SessionLoader;
import frames.*;

public class GUIEngine {
	
	private static GUIEngine instance = null;

	private MenuGUI mainMenu;
	private StartGameGUI startMenu;
	private PlayGUI playScreen;
	private LevelChooserGUI levelChooser;

	private PlayerNameRequestGUI playerRequester;
	private LoadGameGUI loadGame;
	
	private Player player;
	private int maxLevel;
	

	private GameOverGUI gameOverWindow;


	private GUIEngine(){		
		maxLevel = 1;
	}

	public static GUIEngine getInstance(){
		if(instance == null){
			instance = new GUIEngine();
		}
		return instance;
	}

	public void start(){
		mainMenu = new MenuGUI();
	}

	public void startGame(Player player) {
		setPlayer(player);
		if(playerRequester != null)
			playerRequester.dispose();
		startMenu = new StartGameGUI();
	}

	
	public void askPlayer(){
		boolean alreadyAsked = player != null && player.getName() != null;
		mainMenu.dispose();
		
		if (alreadyAsked)
			startMenu = new StartGameGUI();
		else
			playerRequester = new PlayerNameRequestGUI();
			
	}
	

	public void gameIsOverLosingSituation(){
		playScreen.setEnabled(false);
		if (gameOverWindow == null)
			gameOverWindow = new GameOverGUI("lose");
	}

	public void gameIsOverWinningSituation(){
		playScreen.setEnabled(false);
		if (gameOverWindow == null)
			gameOverWindow = new GameOverGUI("win");
	}

	public void loadNextLevel(){
		playScreen.dispose();
		gameOverWindow.dispose();
		gameOverWindow = null;
		GameEngine.getInstance().createNewGame(Level.getInstance().loadLevel(Level.getInstance().getLevelNumber()+1));
		playScreen = new PlayGUI();
	}

	public void returnMainMenuFromGameOverScreen(){
		playScreen.dispose();
		gameOverWindow.dispose();
		gameOverWindow = null;
		mainMenu = new MenuGUI();
	}


	public void exitGame(){
		System.exit(0);
	}

	public void newGame(int levelNo){
		levelChooser.dispose();
		GameEngine.getInstance().createNewGame(Level.getInstance().loadLevel(levelNo));		
		playScreen = new PlayGUI();
	}

	
	public void loadGame(String path){
		loadGame.dispose();
		GameEngine.getInstance().createLoadedGame(path);
		playScreen = new PlayGUI();
	}
	
	public void callLoadGame(){
		startMenu.dispose();
		if(GameLoader.getInstance().isPlayerLoadable(player)){
			loadGame = new LoadGameGUI(GameLoader.getInstance().loadableXMLs(player));
		}
		else{			
			loadGame = new LoadGameGUI();
		}
	}

	public void returnMenu(){
		startMenu.dispose();
		mainMenu = new MenuGUI();
	}
	
	public void returnStartGUIFromLevelChooser() {
		// TODO Auto-generated method stub
		levelChooser.dispose();
		startMenu = new StartGameGUI();
	}

	public void returnMenuFromPlayerRequester(){
		playerRequester.dispose();
		mainMenu = new MenuGUI();
	}
	
	public void returnStartGUIFromLoadGUI(){
		loadGame.dispose();
		startMenu = new StartGameGUI();
	}

	public void chooseLevel() {
		startMenu.dispose();		
		if(SessionLoader.getInstance().isPlayerLoadable(player)){
			SessionLoader.getInstance().loadSession(player);
			maxLevel = SessionLoader.getInstance().getMaxLevel().getLevelNumber();			
		}		
		levelChooser = new LevelChooserGUI(maxLevel, Level.getTotalNumLevels());
	}

	public void buttonClicked(Position position){
		GameEngine.getInstance().lokumClicked(position);
	}
	
	public void swapSLokums(Position pos1, Position pos2) {
		SLokum sl1 = playScreen.getBoardGUI().getSLokumAt(pos1);
		SLokum sl2 = playScreen.getBoardGUI().getSLokumAt(pos2);
		sl1.setTriggerType(whatWillBeTriggeredAfterThisLokum.FALL);
		sl2.setTriggerType(whatWillBeTriggeredAfterThisLokum.FALL);
		sl1.moveToPosition(pos2);
		sl2.moveToPosition(pos1);
		playScreen.getBoardGUI().setSLokum(sl1, pos2);
		playScreen.getBoardGUI().setSLokum(sl2, pos1);
		//playScreen.getBoardGUI().printBoardGUI();
	}

	public PlayGUI getPlayGUI() {
		return playScreen;
	}
	
	private void setPlayer(Player player){
		GameEngine.getInstance().setPlayer(player);
		this.player = player;		
	}
	public Player getPlayer(){
		return player;
	}
	
	public void saveCurrentGame(String path){
		GameEngine.getInstance().saveGame(path);
	}
	
	private void setMaxLevel(int maxlevel){
		maxLevel = maxlevel;		
	}
	public int getMaxLevel(){
		return maxLevel;
	}
	
}
