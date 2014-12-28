package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cas.GameLoader;
import cas.GameSaver;
import cas.Level;
import cas.Player;
import engines.GameEngine;

public class GameSaverLoaderTest {
	GameSaver saver;
	GameLoader loader;
	GameEngine engine;
	Level level;
	Player player;

	@Before
	public void setUp() throws Exception {
		saver = GameSaver.getInstance();
		loader = GameLoader.getInstance();
		engine = GameEngine.getInstance();
		level = Level.getInstance();
		level.loadLevel(1);
		player = new Player("ali");		
		engine.createNewGame(level);		
		engine.setPlayer(player);
	}

	@Test
	public void saverGetInstanceTest() {
		GameSaver otherSaver = GameSaver.getInstance();
		
		assertFalse(saver.equals(null));
		assertEquals(saver, otherSaver);
	}
	
	@Test
	public void loaderGetInstanceTest() {
		GameLoader otherLoader = GameLoader.getInstance();
		
		assertFalse(loader.equals(null));
		assertEquals(loader, otherLoader);
	}
	
	@Test
	public void saveAndLoadTest() { // by CeyCey
		saver.saveCurrentGame("oyun");
		loader.loadGame("oyun");
		engine.createLoadedGame("oyun");
		
		assertFalse(loader.equals(null));
		assertFalse(loader.getLevel().equals(null));
		assertFalse(loader.getPlayer().equals(null));
		assertEquals(loader.getPlayer(), engine.getPlayer());
		assertEquals(loader.getMovesLeft(), engine.getMovesLeft());
		assertEquals(loader.getSpecialSwapsLeft(), engine.getSpecialSwapsLeft());
		assertEquals(loader.getScore(), engine.getScore(), 0);
		assertEquals(loader.getTimeLeft(), engine.getTimeLeft());
	}
	
	@Test
	public void saveByLokumGGAndLoadTest() {
		player = new Player("LokumGG");
		engine.setPlayer(player);
		loader.loadGame("mySaves");
		engine.createLoadedGame("mySaves");
		
		assertFalse(loader.equals(null));
		assertFalse(loader.getLevel().equals(null));
		assertFalse(loader.getPlayer().equals(null));
		assertEquals(loader.getPlayer(), engine.getPlayer());
		assertEquals(loader.getMovesLeft(), engine.getMovesLeft());
		assertEquals(loader.getSpecialSwapsLeft(), engine.getSpecialSwapsLeft());
		assertEquals(loader.getScore(), engine.getScore(), 0);
		assertEquals(loader.getTimeLeft(), engine.getTimeLeft());
	}

}
