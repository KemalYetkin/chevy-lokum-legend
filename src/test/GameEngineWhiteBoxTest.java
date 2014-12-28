package test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import occupiers.Lokum;
import occupiers.SquareOccupierFactory;
import occupiers.StripedLokum;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cas.Board;
import cas.GameLoader;
import cas.Level;
import cas.Player;
import cas.Position;

import engines.GUIEngine;
import engines.GameEngine;
import frames.BoardGUI;
import frames.PlayGUI;

public class GameEngineWhiteBoxTest {
	private static GameEngine engineObj;
	private static GUIEngine gui; // against null pointer exceptions
	private static SquareOccupierFactory factory;
	private static Board board;
	private static Level level;
	private static GameLoader loader;
	private Lokum lokum;
	private static Player tester;
/**
 * 
 * In this class, method design is like below:
 * method calls
 * expectations
 * assertions
 * 
 */
	
	@BeforeClass
	public static void setUpClass() {
	    //executed only once, before the first test
		engineObj = GameEngine.getInstance();
		gui = GUIEngine.getInstance();
		gui.start();
		factory = SquareOccupierFactory.getInstance();
		board = Board.getInstance();
		loader = GameLoader.getInstance();
		level = Level.getInstance();
		level.loadLevel(1);
		tester = new Player("LokumGG");
		gui.startGame(tester);
		gui.callLoadGame();	
	}
	
	@Before
	public void setUp() throws Exception, SecurityException {
		board = Board.getInstance();
		engineObj.createNewGame(level);
		tester = new Player("LokumGG");
		engineObj.setPlayer(tester);
		gui.loadGame("mySaves");
		gui.getPlayGUI().setVisible(false);	
		loader.loadGame("mySaves");
		engineObj.setPlayer(tester);
		engineObj.createLoadedGame("mySaves");	
		Method m = engineObj.getClass().getDeclaredMethod("setMovesLeft", int.class);
		m.setAccessible(true);
		String result = (String) m.invoke(engineObj, 3);
	}

	/**
	 * Testing getInstance() of Singleton
	 */
	@Test
	public void getInstanceTest() {
		System.out.println(board.toString());
		GameEngine otherEngine = GameEngine.getInstance();
		assertNotSame(null, engineObj);
		assertEquals(otherEngine, engineObj);
		assertTrue(engineObj.repOK());
	}

	@Test
	public void chooseLokumTest() {
		Position pos = new Position(0, 0);
		engineObj.chooseLokum(pos);
		Lokum lokum = board.getLokumAt(new Position(0,0));
		assertEquals(lokum, engineObj.getFirstLokum());
		assertTrue(engineObj.repOK());
	}

	@Test
	public void deselectLokumTest() {
		Position pos = new Position(0, 0);
		engineObj.chooseLokum(pos);
		engineObj.deselectLokum();
		assertEquals(null, engineObj.getFirstLokum());
		assertTrue(engineObj.repOK());
	}

	@Test
	public void resetTest() {
		level.loadLevel(2);
		engineObj.reset(level);
		assertEquals(null, engineObj.getFirstLokum());
		assertEquals(0.0, engineObj.getScore(), 0);
		assertEquals(level.getMaxMoves(), engineObj.getMovesLeft());
		assertEquals(level, engineObj.getLevel());
		assertTrue(engineObj.repOK());
	}

	@Test
	public void swapWithTest() throws Exception {
		Lokum lokum2 = factory.generateLokum("green", "RegularLokum");
		Lokum lokum3 = factory.generateLokum("red", "RegularLokum");
		Lokum lokum4 = factory.generateLokum("red", "RegularLokum");
		board.setLokum(lokum2, new Position(1, 0));
		board.setLokum(lokum3, new Position(2, 0));
		board.setLokum(lokum4, new Position(3, 0));
		engineObj.chooseLokum(new Position(0, 0));
		//reflection
		Method m = engineObj.getClass().getDeclaredMethod("swapWith", Position.class);
		m.setAccessible(true);
		String result = (String) m.invoke(engineObj, new Position(1, 0));

		System.out.println(engineObj.getFirstLokum());
		assertEquals(null, engineObj.getFirstLokum());
		assertTrue(engineObj.repOK());
		m.setAccessible(false);
	}

	@Test
	public void createnewGameTest() {
		level.loadLevel(2);
		engineObj.createNewGame(level);

		assertEquals(null, engineObj.getFirstLokum());
		assertEquals(0.0, engineObj.getScore(), 0);
		assertEquals(level.getMaxMoves(), engineObj.getMovesLeft());
		assertEquals(level, engineObj.getLevel());
		assertTrue(engineObj.repOK());
	}

	@Test
	public void createLoadedGameTest() throws Exception {
		Player sabri = new Player("cey");
		engineObj.setPlayer(sabri);
		System.out.println(engineObj.getPlayer().getName());
		loader.loadGame("asafasd");
		engineObj.createLoadedGame("asafasd");
		assertEquals(loader.getPlayer(), engineObj.getPlayer());
		assertEquals(loader.getLevel(), engineObj.getLevel());
		assertNotSame(null, engineObj.getPlayer());
		assertNotSame(null, engineObj.getLevel());
		assertEquals(loader.getMovesLeft(), engineObj.getMovesLeft());
		assertEquals(loader.getScore(), engineObj.getScore(), 0);
		assertEquals(loader.getSpecialSwapsLeft(),
				engineObj.getSpecialSwapsLeft());
		assertEquals(loader.getTimeLeft(), engineObj.getTimeLeft());
		assertTrue(engineObj.repOK());
	}

	@Test
	public void updateScoreTest() {
		engineObj.updateScoreBy(100.0);
		assertEquals(100, engineObj.getScore(), 0);
	}

	@Test
	public void updateMovesLeftTest() {
		engineObj.updateMovesLeftBy(2);
		assertEquals(level.getMaxMoves() - 2, engineObj.getMovesLeft()); //lokumGGden movesLeft alamayoruz!!
	}

	@Test
	public void checkGameWinTest() {
		engineObj.updateScoreBy(10000000.0);
		assertFalse(engineObj.checkGame());
	}

	@Test
	public void checkGameLoseTest() {
		engineObj.updateMovesLeftBy(level.getMaxMoves());
		assertFalse(engineObj.checkGame());
	}

	@Test
	public void specialSwapLeftTest() {
		assertEquals(level.getSpecialSwapCounter(),
		engineObj.getSpecialSwapsLeft());
	}

	@Test
	public void increaseRemainingTimeTest() {
		engineObj.increaseRemainingTimeBy(5000);
		assertEquals(loader.getTimeLeft() + 5000, engineObj.getTimeLeft());
	}

	@Test
	public void decreaseRemainingTimeTest() {
		engineObj.decreaseRemainingTimeBy(5000);
		assertEquals(loader.getTimeLeft() - 5000, engineObj.getTimeLeft());
	}

	@Test
	public void lokumClickedNullTest() {
		engineObj.lokumClicked(null);
		assertEquals(null, engineObj.getFirstLokum());

		engineObj.lokumClicked(new Position(0, 0));
		engineObj.lokumClicked(null);
		assertEquals(null, engineObj.getFirstLokum());
	}

	@Test
	public void lokumClickedForDeselectionTest() {
		engineObj.lokumClicked(new Position(0, 0));
		engineObj.lokumClicked(new Position(0, 0));
		assertEquals(null, engineObj.getFirstLokum());
	}

	@Test
	public void regularSwapTest() {
		engineObj.lokumClicked(new Position(5,1));
		engineObj.lokumClicked(new Position(5,0));
		Lokum unexpected = factory.generateLokum("brown", "RegularLokum");
		assertFalse(board.getLokumAt(new Position(3,0)).equals(unexpected) && board.getLokumAt(new Position(4,0)).equals(unexpected) && board.getLokumAt(new Position(5,0)).equals(unexpected));
		assertTrue(engineObj.getScore() > 59);
		assertTrue(engineObj.repOK());
	}

	@Test
	public void stripedHorizontalGeneratedTest() {
		engineObj.lokumClicked(new Position(7,7));
		engineObj.lokumClicked(new Position(7,8));	
		StripedLokum expected = (StripedLokum) factory.generateLokum("green", "StripedLokum");
		expected.setDirection("horizontal");
		assertEquals(expected, board.getLokumAt(new Position(7,8)));
		assertTrue(engineObj.repOK());
	}

	@Test
	public void stripedVerticalGeneratedTest() {
		engineObj.lokumClicked(new Position(6,7));
		engineObj.lokumClicked(new Position(7,7));	
		
		StripedLokum expected = (StripedLokum) factory.generateLokum("green", "StripedLokum");
		expected.setDirection("vertical");
		
		assertEquals(expected, board.getLokumAt(new Position(7,8)));
		assertTrue(engineObj.repOK());
	}

	@Test
	public void wrappedGeneratedTest() {
		engineObj.lokumClicked(new Position(0,0));
		engineObj.lokumClicked(new Position(1,1));
		
		Lokum expected = factory.generateLokum("white", "WrappedLokum");
		
		assertEquals(expected, board.getLokumAt(new Position(0,2)));
		assertTrue(engineObj.repOK());
	}

	@Test
	public void colorBombGeneratedTest() {
		engineObj.lokumClicked(new Position(2,8));
		engineObj.lokumClicked(new Position(2,7));
		
		Lokum expected = factory.generateLokum("", "ColorBombLokum");
		
		assertEquals(expected, board.getLokumAt(new Position(2,8)));
		assertTrue(engineObj.repOK());
	}

	@Test
	public void stripedHorizontalExplodeTest() {
		engineObj.lokumClicked(new Position(0,4));
		engineObj.lokumClicked(new Position(1,4));
		
		assertTrue(engineObj.getScore() > 179);
		assertTrue(engineObj.repOK());
	}

	@Test
	public void stripedVerticalExplodeTest() {
		Lokum helper = factory.generateLokum("white", "RegularLokum");
		board.setLokum(helper, new Position(3,3));
		Lokum unexpected = board.getLokumAt(new Position(4,2));
		
		engineObj.lokumClicked(new Position(4,2));
		engineObj.lokumClicked(new Position(3,2));
		
		assertFalse(board.getLokumAt(new Position(3,8)).equals(unexpected));
		assertTrue(engineObj.getScore() > 179);
		assertTrue(engineObj.repOK());
	}

	@Test
	public void wrappedExplodeTest() {
		Lokum helper = factory.generateLokum("green", "RegularLokum");
		board.setLokum(helper, new Position(5,4));
		Lokum unexpected = board.getLokumAt(new Position(5,6));
		
		engineObj.lokumClicked(new Position(4,5));
		engineObj.lokumClicked(new Position(5,5));
		
		assertFalse(board.getLokumAt(new Position(5,8)).equals(unexpected));
		assertTrue(engineObj.getScore() > 179);
		assertTrue(engineObj.repOK());
	}

	@Test
	public void colorBombStripedExplodeTest() {
		engineObj.lokumClicked(new Position(7,0));
		engineObj.lokumClicked(new Position(7,1));
		
		assertTrue(engineObj.getScore() > 540);
		assertTrue(engineObj.repOK());
	}

	@Test
	public void wrappedWrappedExplodeTest() { //Bedi
		engineObj.lokumClicked(new Position(2,6));
		engineObj.lokumClicked(new Position(1,6));
		
		Lokum unexpected = factory.generateLokum("brown", "WrappedLokum");
		
		assertFalse(board.getLokumAt(new Position(1,6)).equals(unexpected) && board.getLokumAt(new Position(2,6)).equals(unexpected));
		assertTrue(engineObj.getScore() > 1000);
		assertTrue(engineObj.repOK());
	}

	@Test
	public void wrappedExplodeInRangeOfWrappedTest() { //Atil
		Lokum helper = factory.generateLokum("green", "RegularLokum");
		board.setLokum(helper, new Position(5,4));
		Lokum otherWrapped = factory.generateLokum("white", "WrappedLokum");
		board.setLokum(otherWrapped, new Position(6,6));
		
		assertTrue(engineObj.getScore() > 500);
		assertTrue(engineObj.repOK());
	}

	@Test
	public void stripedStripedExplodeTest() { //Bedi
		engineObj.lokumClicked(new Position(4,2));
		engineObj.lokumClicked(new Position(5,2));
		
		Lokum unexpected = factory.generateLokum("green", "StripedLokum");
		
		assertNotSame(unexpected, board.getLokumAt(new Position(5,2)));
		assertTrue(engineObj.getScore() > 500);
		assertTrue(engineObj.repOK());
	}

	@Test
	public void colorBombWrappedExplodeTest() { //Atil
		engineObj.lokumClicked(new Position(8,0));
		engineObj.lokumClicked(new Position(8,1));
		
		Lokum unexpected = factory.generateLokum("", "ColorBombLokum");
		
		assertNotSame(unexpected, board.getLokumAt(new Position(8,0)));
		assertTrue(engineObj.getScore() > 1000);
		assertTrue(engineObj.repOK());
	}

	@Test
	public void colorBombColorBombExplodeTest() { // Atil
		engineObj.lokumClicked(new Position(8,0));
		engineObj.lokumClicked(new Position(7,0));
		
		Lokum unexpected = factory.generateLokum("", "ColorBombLokum");
		
		assertTrue(engineObj.getScore() > 2000);
		assertNotSame(unexpected, board.getLokumAt(new Position(7,0)));
		assertNotSame(unexpected, board.getLokumAt(new Position(8,0)));
		assertTrue(engineObj.repOK());
	}

	@Test
	public void wrappedStripedExplodeTest() { // Bedi
		engineObj.lokumClicked(new Position(6,6));
		engineObj.lokumClicked(new Position(5,6));
		
		Lokum unexpected1 = factory.generateLokum("brown", "WrappedLokum");
		Lokum unexpected2 = factory.generateLokum("white", "StripedLokum");
		
		assertTrue(engineObj.getScore() > 1100);
		assertFalse(board.getLokumAt(new Position(5,6)).equals(unexpected1) && board.getLokumAt(new Position(6,6)).equals(unexpected2));
		assertTrue(engineObj.repOK());
	}

	@Test
	public void timeLokumExplodeTest() {
		Lokum helper = factory.generateLokum("brown", "TimeLokum");
		board.setLokum(helper, new Position(5,1));
		long timeLeft = engineObj.getTimeLeft();
		
		engineObj.lokumClicked(new Position(5,1));
		engineObj.lokumClicked(new Position(5,0));
		
		long expected = timeLeft+5000;
		assertEquals(expected, engineObj.getTimeLeft());
		assertTrue(engineObj.repOK());
	}
}
