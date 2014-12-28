package test;
//Ezgi Karakas

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import handlers.SubscriptionKeeper;
import occupiers.SquareOccupier;
import occupiers.SquareOccupierFactory;

import org.junit.Before;
import org.junit.Test;

import cas.Board;
import cas.Level;
import cas.Player;
import cas.Position;

public class GameEngineBlackBoxTest {
	
	engines.GameEngine testEngine;
	Level testLevel;
	Board board;
	SquareOccupierFactory sof;

	@Before
	public void setUp() throws Exception {
		testEngine = engines.GameEngine.getInstance();
		testLevel = Level.getInstance();
		testLevel.loadLevel(1);
		testEngine.createNewGame(testLevel);
		board = Board.getInstance();
		sof = SquareOccupierFactory.getInstance();
		
		board.setLokum(sof.generateLokum("red", "RegularLokum"), new Position(0,0));
		board.setLokum(sof.generateLokum("red", "RegularLokum"), new Position(1,0));
		board.setLokum(sof.generateLokum("green", "RegularLokum"), new Position(2,0));
		board.setLokum(sof.generateLokum("red", "RegularLokum"), new Position(3,0));
		board.setLokum(sof.generateLokum("red", "RegularLokum"), new Position(4,0));
		board.setLokum(sof.generateLokum("white", "RegularLokum"), new Position(0,1));
		board.setLokum(sof.generateLokum("green", "RegularLokum"), new Position(1,1));
		board.setLokum(sof.generateLokum("red", "RegularLokum"), new Position(2,1));
		board.setLokum(sof.generateLokum("green", "RegularLokum"), new Position(3,1));
		board.setLokum(sof.generateLokum("white", "RegularLokum"), new Position(4,1));
		board.setLokum(sof.generateLokum("white", "RegularLokum"), new Position(0,2));
		board.setLokum(sof.generateLokum("red", "RegularLokum"), new Position(1,2));
		board.setLokum(sof.generateLokum("green", "RegularLokum"), new Position(2,2));
		board.setLokum(sof.generateLokum("red", "RegularLokum"), new Position(3,2));
		board.setLokum(sof.generateLokum("white", "RegularLokum"), new Position(4,2));
		board.setLokum(sof.generateLokum("red", "RegularLokum"), new Position(0,3));
		board.setLokum(sof.generateLokum("white", "RegularLokum"), new Position(1,3));
		board.setLokum(sof.generateLokum("green", "RegularLokum"), new Position(2,3));
		board.setLokum(sof.generateLokum("white", "RegularLokum"), new Position(3,3));
		board.setLokum(sof.generateLokum("red", "RegularLokum"), new Position(4,3));
		board.setLokum(sof.generateLokum("white", "RegularLokum"), new Position(0,4));
		board.setLokum(sof.generateLokum("green", "RegularLokum"), new Position(1,4));
		board.setLokum(sof.generateLokum("red", "RegularLokum"), new Position(2,4));
		board.setLokum(sof.generateLokum("white", "RegularLokum"), new Position(3,4));
		board.setLokum(sof.generateLokum("green", "RegularLokum"), new Position(4,4));
	}

	@Test
	public void testChooseLokum() {
		Position pos = new Position(3,4);
		testEngine.chooseLokum(pos);
		assertEquals(testEngine.getFirstLokum(),board.getLokumAt(pos));
	}

	@Test
	public void testSwapWith() throws Exception, SecurityException {
		testEngine.chooseLokum(new Position(3,4));
		Method m = testEngine.getClass().getDeclaredMethod("swapWith", Position.class);
		m.setAccessible(true);
		String result = (String) m.invoke(testEngine, new Position(0, 2));
		assertEquals(testEngine.getFirstLokum(),null);
		assertEquals(SubscriptionKeeper.getInstance().getComboCount(),0);
		m.setAccessible(false);
	}

	@Test
	public void testCompleteSwap() {
		testEngine.chooseLokum(new Position(2,0));
	    testEngine.setsPosition(new Position(3,0));
		int previousCounter = testEngine.getCompletedSwapCounter();
	    SquareOccupier[][] previousMatrix = board.getBoardMatrix();
	    testEngine.completeSwap();
	    assert(previousCounter == testEngine.getCompletedSwapCounter());
	    assert(previousMatrix == board.getBoardMatrix());
	}

	@Test
	public void testDeselectLokum() {
		testEngine.chooseLokum(new Position(2,0));
		testEngine.deselectLokum();
		assertEquals(testEngine.getFirstLokum(),null);
	}

	@Test
	public void testCreateLoadedGame() {
		Player player = new Player("ezgi");
		testEngine.setPlayer(player);
		testEngine.createLoadedGame("ass");
		assertEquals(testEngine.getScore(),180,0);
		assertEquals(testEngine.getMovesLeft(),9);
		assertEquals(testEngine.getPlayer().getName(),"ezgi");
		assertEquals(testEngine.getLevel().getLevelNumber(),1);	
	}

	@Test
	public void testUpdateScoreBy() {
		double score = testEngine.getScore();
		testEngine.updateScoreBy(50);
		assertEquals(testEngine.getScore(),(score+50),0);
	}

	@Test
	public void testUpdateMovesLeftBy() {
		int moves = testEngine.getMovesLeft();
		testEngine.updateScoreBy(2);
		assertEquals(testEngine.getMovesLeft(),(moves-2));
	}

}
