package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cas.Level;
import cas.Player;
import cas.SessionLoader;
import cas.SessionSaver;
import engines.GameEngine;

public class SessionSaverLoaderTest {
	private SessionSaver saver;
	private SessionLoader loader;
	private GameEngine engine;
	private Level level;
	private Player player;

	@Before
	public void setUp() throws Exception {
		saver = SessionSaver.getInstance();
		loader = SessionLoader.getInstance();
		engine = GameEngine.getInstance();
		level = Level.getInstance();
		level.loadLevel(1);
		engine.createNewGame(level);
		player = new Player("testsession");
		engine.setPlayer(player);
	}

	@Test
	public void loaderGetInstanceTest() {
		SessionLoader otherLoader = SessionLoader.getInstance();
		
		assertNotSame(null, loader);
		assertEquals(otherLoader, loader);
	}

	@Test
	public void saverGetInstanceTest() {
		SessionSaver otherSaver = SessionSaver.getInstance();
		
		assertNotSame(null, saver);
		assertEquals(otherSaver, saver);
	}

	@Test
	public void sessionSaveAndLoadTest() {
		saver.saveSession();
		loader.loadSession(player);
		
		assertFalse(level == null);
		assertFalse(player == null);
		assertEquals(level, loader.getMaxLevel());
		assertEquals(player.getId(), loader.getPlayer().getId());
		assertEquals(engine.getScore(), loader.getScore(), 0);
	}

}
