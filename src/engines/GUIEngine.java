package engines;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import scomponents.SLokum;
import cas.AudioPlayers;
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
	private boolean specialSwapEnabled;
	private PlayerNameRequestGUI playerRequester;
	private LoadGameGUI loadGame;
	private Player player;
	private int maxLevel;
	private boolean musicStatus = true;
	private GameOverGUI gameOverWindow;

	private GUIEngine(){		
		setMaxLevel(1);
		specialSwapEnabled = false;

		queue = new ArrayList<Animation>();
		animTag = 0;
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

	public void exitGame() {
		System.exit(0);
	}

	public void newGame(int levelNo) {
		levelChooser.dispose();
		GameEngine.getInstance().createNewGame(
				Level.getInstance().loadLevel(levelNo));
		playScreen = new PlayGUI();
	}

	public void chooseLevel() {
		startMenu.dispose();
		if (SessionLoader.getInstance().isPlayerLoadable(player)) {
			SessionLoader.getInstance().loadSession(player);

			maxLevel = SessionLoader.getInstance().getMaxLevel()
					.getLevelNumber();
		}
		levelChooser = new LevelChooserGUI(maxLevel, Level.getTotalNumLevels());
	}

	public void specialSwapButtonClicked() {
		specialSwapEnabled = !specialSwapEnabled;
		if (specialSwapEnabled) {
			GameEngine.getInstance().enableSpecialSwap();
			// Change the visual indicator of special swap
			getPlayGUI().toggleSpecialSwapButton();
		} else {
			GameEngine.getInstance().disableSpecialSwap();
			// Change the visual indicator of special swap
			getPlayGUI().toggleSpecialSwapButton();
		}
		System.out.println(specialSwapEnabled);
	}

	public void displayNewSpecialSwapLeft() {
		getPlayGUI().setSpecialSwapLeft(
				GameEngine.getInstance().getSpecialSwapsLeft());
	}

	public void buttonClicked(Position position) {
		GameEngine.getInstance().lokumClicked(position);
	}

	public void swapSLokums(Position pos1, Position pos2) {
		playScreen.getBoardGUI().moveSLokums(pos1, pos2,
				GUIEngine.getInstance().getAnimTag());
	}

	public PlayGUI getPlayGUI() {
		return playScreen;
	}

	private void setPlayer(Player player) {
		GameEngine.getInstance().setPlayer(player);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void saveCurrentGame(String path) {
		GameEngine.getInstance().saveGame(path);
	}

	private void setMaxLevel(int maxlevel) {
		this.maxLevel = maxlevel;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public boolean getMusicStatus() {
		return musicStatus;
	}

	public void setMusicStatus(boolean status) {
		musicStatus = status;
	}

	// ////////////////////////////////////////
	// ANIMATION

	private ArrayList<Animation> queue;
	private Timer animTimer;
	private int animTag;

	public int getAnimTag() {
		return animTag;
	}

	public int getNextTag() {
		return animTag++;
	}

	public void resetAnimTag() {
		animTag = 0;
	}

	public void addToAnimationQueue(SLokum sl, Position pre, Position post,
			int act, int tag) {
		queue.add(new Animation(sl, pre, post, act, tag));
	}

	public void initiateAnimations() {
		System.out.println("Animations are initiated. " + queue.size()
				+ " animations and the bitch ain't one...");
		int pt = 0;
		for (int i = 0; i < queue.size(); i++) {
			if ((i == 0)
					|| (queue.get(i).act() != 2)
					&& (queue.get(i).tag() != queue.get(i - 1).tag())
					|| ((queue.get(i).act() == 2) && (queue.get(i - 1).act() != 2))) {
				pt++;
			}
		
			
			System.out.println("pt: " + pt);
			queue.get(i).animate(pt);
		}
		animTimer = new Timer(pt * Animation.step, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				playScreen.getBoardGUI().refreshBoard();
				System.out.println("Board refreshed, end of animations.");
				if(GameEngine.getInstance().checkGame()){
						GameEngine.getInstance().enableInteraction();
					}
				animTimer.stop();
				System.out.println("animTimer stopped");
				
			}
		});
		queue.clear();
		resetAnimTag();
			animTimer.start();
	}

	class Animation {

		private SLokum sl;
		private Position pre;
		private Position post;
		private int action;
		private int tag;
		Timer t;

		public Animation(SLokum sl, Position pre, Position post, int act,
				int tag) {
			this.sl = sl;
			this.pre = pre;
			this.post = post;
			this.action = act;
			this.tag = tag;
		}

		public int tag() {
			return tag;
		}

		public int act() {
			return action;
		}

		public void animate(int n) {
			t = new Timer(n * step, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					switch (action) {
					case SWAP:
						SLokum sl0 = playScreen.getBoardGUI().getSLokumAt(pre);
						SLokum sl1 = playScreen.getBoardGUI().getSLokumAt(post);
//						System.out.println("Swapping " + sl0.getColor()
//								+ " from " + pre + " to " + post);
//						System.out.println("Swapping " + sl1.getColor()
//								+ " from " + post + " to " + pre);
						playScreen.getBoardGUI().setSLokum(sl0, post);
						playScreen.getBoardGUI().setSLokum(sl1, pre);
						if (sl0 != null)
							sl0.setLocation((post.getX() * sl0.width()) + (sl0.width() / 2),(post.getY() * sl0.height()) + (sl0.height() / 2));
						if(sl1 != null)
							sl1.setLocation((pre.getX() * sl1.width()) + (sl1.width() / 2),(pre.getY() * sl1.height()) + (sl1.height() / 2));
						break;
					case MOVE:
						SLokum sl2 = playScreen.getBoardGUI().getSLokumAt(pre);
						// System.out.println("Moving "+sl2.getColor() +
						// " from "+pre+" to "+post);
						playScreen.getBoardGUI().setSLokum(null, pre);
						playScreen.getBoardGUI().setSLokum(sl2, post);
						if(sl2 != null)
						sl2.setLocation((post.getX() * sl2.width()) + (sl2.width() / 2), (post.getY() * sl2.height()) + (sl2.height() / 2));
						break;
					case REMOVE:
						System.out.println("lokum removed from (" + pre.getX()
								+ "," + pre.getY() + ")");
						SLokum slr = playScreen.getBoardGUI().getSLokumAt(pre);
						if(slr != null){
						if ("regular".equals(slr.getType())) {
							AudioPlayers.getInstance()
							.playExplosionEffect(true);
						} else if ("striped_horizontal".equals(slr.getType())
								|| "striped_vertical".equals(slr.getType())) {
							AudioPlayers.getInstance()
							.playStripedExplodeEffect(true);
						} else if ("wrapped".equals(slr.getType())) {
							AudioPlayers.getInstance()
							.playWrappedExplodeEffect(true);
						} else if ("colorbomb".equals(slr.getType())) {
							AudioPlayers.getInstance()
							.playColorBombExplodeEffect(true);
						}
						}
						playScreen.getBoardGUI().removeSLokumAt(pre);
						break;
					case EMERGE:
						playScreen.getBoardGUI().insertSLokum(sl, post);
						System.out.println("New lokum emerged at ("
								+ post.getX() + "," + post.getY() + ")");
						break;
					}
					t.stop();
					// playScreen.getBoardGUI().printBoardGUI();
				}

			});
			t.start();
			return;
		}

		public static final int step = 250;

		public static final int MOVE = 1;
		public static final int REMOVE = 2;
		public static final int EMERGE = 3;
		public static final int SWAP = 4;
	}
}
