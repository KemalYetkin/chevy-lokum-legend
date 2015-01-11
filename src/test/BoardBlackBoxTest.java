package test;

import static org.junit.Assert.*;
import occupiers.Lokum;
import occupiers.RegularLokum;
import occupiers.SquareOccupier;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import cas.AudioPlayers;
import cas.GameLoader;
import cas.Level;
import cas.Player;
import cas.Position;
import engines.GUIEngine;
import engines.GameEngine;

public class BoardBlackBoxTest {
	cas.Board testBoard;
	engines.GameEngine testEngine;
	Level testLevel;
	private static GameEngine engineObj;
	private static GUIEngine gui;
	private static Level level;
	private static GameLoader loader;
	private static Player tester;
	
	

	@BeforeClass
	public static void setUpClass() {
	    //executed only once, before the first test
		AudioPlayers noSoundPlease = AudioPlayers.getInstance();
		noSoundPlease.disableOrEnableAllBackMusic(false);
		noSoundPlease.disableOrEnableEffects(false);
		engineObj = GameEngine.getInstance();
		gui = GUIEngine.getInstance();
		gui.start();
		loader = GameLoader.getInstance();
		level = Level.getInstance();
		level.loadLevel(1);
		tester = new Player("dsds");
		gui.startGame(tester);
		gui.callLoadGame();	
	}
	
	@Before
	public void setUp() throws Exception {
		testBoard = cas.Board.getInstance();

		for (int i = 0; i < cas.Board.ROW_NUMBER; i++) {
			for (int j = 0; j < cas.Board.COLUMN_NUMBER; j++) {
				testBoard.setLokum(new RegularLokum(), new Position(i,j));
				if ((i + j) % 2 == 1)  
					testBoard.getLokumAt(new Position(i,j)).setColor("red");
				else 
					testBoard.getLokumAt(new Position(i,j)).setColor("green");
			}
		}
		
		engineObj.createNewGame(level);
		tester = new Player("LokumGG");
		engineObj.setPlayer(tester);
		gui.loadGame("mySaves");
		gui.getPlayGUI().setVisible(false);	
		loader.loadGame("mySaves");
		engineObj.setPlayer(tester);
		engineObj.createLoadedGame("mySaves");	

		testEngine = engines.GameEngine.getInstance();
		testLevel = Level.getInstance();
	}

	@Test
	public void testFallLokumsToEmptySquares() {
		Position pos = new Position(3,4);
		testBoard.setLokum(null,pos);
		Lokum lokum = testBoard.getLokumAt(new Position(3,3));
		System.out.println(testBoard.getLokumAt(new Position(3,3)).getColor());
		testBoard.fillDown(0);
		assertEquals(lokum,testBoard.getLokumAt(pos));
		assert(null != testBoard.getLokumAt(new Position(3,0)));

	}

	@Test
	public void testFindAllMatches() {
		testLevel.loadLevel(1);
		testEngine.createNewGame(testLevel);
		testBoard.getLokumAt(new Position(2,0)).setColor("green");
		testBoard.findAllMatches();
		assert(testBoard.getLokumAt(new Position(2,0)).getColor()!="green");
		assert(testBoard.getLokumAt(new Position(1,0)).getColor()!="green");
		assert(testBoard.getLokumAt(new Position(3,0)).getColor()!="green");
	}

	@Test
	public void testLoadBoard() {
		SquareOccupier[][] matrix = new SquareOccupier[cas.Board.ROW_NUMBER][cas.Board.COLUMN_NUMBER];
		for (int i = 0; i < cas.Board.ROW_NUMBER; i++) {
			for (int j = 0; j < cas.Board.COLUMN_NUMBER; j++) {
				Lokum lok = new RegularLokum();
				lok.setPosition(new Position(i,j));
				lok.setColor("red");
				matrix[j][i]=lok;
			}
		}
		testBoard.loadBoard(matrix);
		for (int i=0; i<cas.Board.ROW_NUMBER; i++){
			for (int j=0; j<cas.Board.COLUMN_NUMBER; j++){
				assertEquals(testBoard.getBoardMatrix()[i][j],matrix[i][j]);
			}
		}
	}

	@Test
	public void testReset() {
		testBoard.reset();
		for (int i=0; i<testBoard.getBoardMatrix().length; i++){
			for (int j=0; j<testBoard.getBoardMatrix().length; j++){
				assertEquals(testBoard.getBoardMatrix()[i][j],null);
			}	
		}
	}

	@Test
	public void testTestSwap() {
		Position pos3 = new Position(3,0);
		Position pos4 = new Position(2,0);
		boolean bool1 = testBoard.testSwap(pos3, pos4);
		assertEquals(false,bool1);
	}

	@Test
	public void testRemoveLokumAt() {
		Position pos = new Position(3,4);
		Lokum lok = testBoard.getLokumAt(pos);
		testBoard.removeLokumAt(pos);
		assertEquals(lok.getPosition(),null);
		assertEquals(testBoard.getBoardMatrix()[4][3],null);
	}

}
