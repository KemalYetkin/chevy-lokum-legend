package cas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import engines.GameEngine;
import occupiers.*;


// TODO: Auto-generated Javadoc
/**
 * The Class GameLoader.
 */
public class GameLoader extends Configuration {
	
	/** The player. */
	private Player player;
	
	/** The occupier matrix. */
	private SquareOccupier[][] occupierMatrix;
	
	/** The score. */
	private double score;
	
	/** The moves left. */
	private int movesLeft;
	
	/** The level. */
	private Level level;
	
	/** The time left. */
	private long timeLeft;
	
	/** The special swap left. */
	private int specialSwapsLeft;
	
	/** The game xsd file. */
	private File gameXSDFile;
	
	/** The instance. */
	private static GameLoader instance;

	/**
	 * Instantiates a new game loader.
	 */
	private GameLoader() {
		super();
		occupierMatrix = new SquareOccupier[Board.ROW_NUMBER][Board.COLUMN_NUMBER];
		gameXSDFile = new File("xml/gameSaves/game.xsd");		
	}

	/**	 
	 * @requires 
	 * @modifies GameLoader.instance if it has not been initialized yet
	 * @ensures GameLoader.instance is instantiated if it has not been yet
	 * @return GameLoader instance	 
	 */
	public static GameLoader getInstance() {
		if (instance == null)
			instance = new GameLoader();
		return instance;
	}

	/**
	 * Load game.
	 *
	 * @param gamePath the game path
	 * @requires The game should be initialized, a player should be set to GameEngine.
	 * 			 A Schema in directory "xml/gameSaves/game.xsd" should be initialized.
	 * @modifies All fields of this class.
	 * @ensures Fields of this class are filled from xml.
	 * @return 
	 */
	public void loadGame(String gamePath) {	
		setPlayer(GameEngine.getInstance().getPlayer());
		gamePath = gamePath.toLowerCase();
		gamePath = gamePath.replaceAll("\\s+", "");
		String playerPath = getPlayer().getName().toLowerCase();
		playerPath = playerPath.replaceAll("\\s+", "");
		File gameXMLFile = new File("xml/gameSaves/" + playerPath + "/"
				+ gamePath + ".xml");
		if (!validate(gameXSDFile, gameXMLFile)) {
			return;
		}
		try {
			Document document;
			document = XMLParser.getDomElement(gameXMLFile);			
			setOccupierMatrix(loadBoard(document));
			setScore(loadScore(document));
			setMovesLeft(loadMovesLeft(document));
			setLevel(loadLevel(document));
			if(getLevel().getGoal() instanceof TimeScoreGoal){
				setTimeLeft(loadTimeLeft(document));
			}
			setSpecialSwapsLeft(loadSpecialSwapsLeft(document));

		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Checks if is player loadable.
	 *
	 * @param Player the player 
	 * @requires A directory "xml/gameSaves" should be initialized.
	 * @modifies 
	 * @ensures there is a player that saved a game before	 
	 * @return true, if is player loadable
	 */
	public boolean isPlayerLoadable(Player Player) {
		String playerPath = Player.getName().toLowerCase();
		File playerDir = new File("xml/gameSaves/" + playerPath);
		if(playerDir.list() != null){
		return playerDir.list().length > 0;
		}
		else{
			return false;
		}
	}

	/**
	 * Loadable xmls.
	 *
	 * @param Player the player
	 * @requires A directory "xml/gameSaves/" should be initiliazed.
	 * @modifies
	 * @ensures 
	 * @return the hash map of players and corresponding games.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap<String, ArrayList> loadableXMLs(Player Player) {
		HashMap<String, ArrayList> resultList = new HashMap<String, ArrayList>();
		if (isPlayerLoadable(Player)) {
			Date date;
			ArrayList list = new ArrayList();
			int levelNo = 0;
			String gameName = "";
			String playerPath = Player.getName().toLowerCase();
			playerPath = playerPath.replaceAll("\\s+", "");
			Document document;
			File dir = new File("xml/gameSaves/" + playerPath);
			File[] directory = dir.listFiles();
			if (directory != null) {
				for (File child : directory) {
					date = new Date(child.lastModified());
					gameName = child.getName();
					try {
						document = XMLParser.getDomElement(child);
						levelNo = loadLevel(document).getLevelNumber();
					} catch (ParserConfigurationException | SAXException
							| IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					list.add(levelNo);
					list.add(date);
					resultList.put(gameName, list);
				}
			}

		}
		return resultList;
	}

	/**
	 * Load board.
	 *
	 * @param document the document
	 * @requires document should be parsed from a validated XML file.
	 * @modifies
	 * @ensures 
	 * @return the SquareOccupier[][]
	 */
	private SquareOccupier[][] loadBoard(Document document) {

		NodeList nodeList = document.getElementsByTagName("board");
		Node node = nodeList.item(0).getFirstChild();
		SquareOccupier[][] matrix = new SquareOccupier[Board.ROW_NUMBER][Board.COLUMN_NUMBER];
		while (node != null) {
			if (node.getNodeName() == "lokums") {
				NodeList childList = node.getChildNodes();
				for (int i = 0; i < childList.getLength(); i++) {
					Node child = childList.item(i);
					if (child.getNodeType() == Node.ELEMENT_NODE) {
						Element eLokum = (Element) child;
						int xcoord = Integer.parseInt(XMLParser.getValue(
								eLokum, "xcoord"));
						int ycoord = Integer.parseInt(XMLParser.getValue(
								eLokum, "ycoord"));
						;
						Position position = new Position(xcoord, ycoord);
						String color = XMLParser.getValue(eLokum, "color");
						String type = XMLParser.getValue(eLokum, "type");
						String direction = XMLParser.getValue(eLokum, "direction");
						Lokum lokum = SquareOccupierFactory.getInstance()
								.generateLokum(color, type);
						if(direction != ""){
							((StripedLokum) lokum).setDirection(direction);
						}
						lokum.setPosition(position);
						putToOccupierMatrix(matrix, lokum);
					}
				}
			}
			node = node.getNextSibling();
		}
		return matrix;
	}

	/**
	 * Load score.
	 *
	 * @param document the document
	 * @requires document should be parsed from a validated XML file.
	 * @modifies 
	 * @ensures 
	 * @return the double
	 */
	private double loadScore(Document document) {
		NodeList nodeList = document.getElementsByTagName("currentscore");
		Element eScore = (Element) nodeList.item(0);
		double score = Double.parseDouble(XMLParser.getElementValue(eScore));
		return score;
	}

	/**
	 * Load moves left.
	 *
	 * @param document the document 
	 * @requires document should be parsed from a validated XML file.
	 * @modifies
	 * @ensures 
	 * @return the int
	 */
	private int loadMovesLeft(Document document) {
		NodeList nodeList = document.getElementsByTagName("movesleft");
		Element eMoves = (Element) nodeList.item(0);
		int movesLeft = Integer.parseInt(XMLParser.getElementValue(eMoves));
		return movesLeft;
	}

	/**
	 * Load level.
	 *
	 * @param document the document
	 * @requires document should be parsed from a validated XML file.
	 * @modifies
	 * @ensures 
	 * @return the level
	 */
	private Level loadLevel(Document document) {
		NodeList nodeList = document.getElementsByTagName("level");
		Element eLevel = (Element) nodeList.item(0);
		Level.getInstance().loadLevel(
				Integer.parseInt(XMLParser.getElementValue(eLevel)));
		return Level.getInstance();
	}
	
	/**
	 * Load time left.
	 *
	 * @param document the document
	 * @requires document should be parsed from a validated XML file.
	 * @modifies 
	 * @ensures 
	 * @return the long
	 */
	private long loadTimeLeft(Document document){
		NodeList nodeList = document.getElementsByTagName("timeleft");
		Element eTimeLeft = (Element) nodeList.item(0);
		long timeLeft = Long.parseLong(XMLParser.getElementValue(eTimeLeft));
		return timeLeft;
	}
	
	/**
	 * Load special swap left.
	 *
	 * @param document the document
	 * @requires document should be parsed from a validated XML file.
	 * @modifies
	 * @ensures 
	 * @return the int
	 */
	private int loadSpecialSwapsLeft(Document document){
		NodeList nodeList = document.getElementsByTagName("specialswapsleft");
		Element especialSwapsLeft = (Element) nodeList.item(0);
		int specialSwapsLeft = Integer.parseInt(XMLParser.getElementValue(especialSwapsLeft));
		return specialSwapsLeft;
	}

	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the player.
	 *
	 * @param player the new player
	 */
	private void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Gets the occupier matrix.
	 *
	 * @return the occupier matrix
	 */
	public SquareOccupier[][] getOccupierMatrix() {
		return occupierMatrix;
	}

	/**
	 * Sets the board.
	 *
	 * @param matrix the new board
	 */
	private void setOccupierMatrix(SquareOccupier[][] matrix) {
		// TODO Auto-generated method stub
		occupierMatrix = matrix;
	}

	/**
	 * Gets the score.
	 *
	 * @return the score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * Sets the score.
	 *
	 * @param score the new score
	 */
	private void setScore(double score) {
		this.score = score;
	}

	/**
	 * Gets the moves left.
	 *
	 * @return the moves left
	 */
	public int getMovesLeft() {
		return movesLeft;
	}

	/**
	 * Sets the moves left.
	 *
	 * @param movesLeft the new moves left
	 */
	private void setMovesLeft(int movesLeft) {
		this.movesLeft = movesLeft;
	}

	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Sets the level.
	 *
	 * @param level the new level
	 */
	private void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * Gets the time left.
	 *
	 * @return the time left
	 */
	public long getTimeLeft() {
		return timeLeft;
	}

	/**
	 * Sets the time left.
	 *
	 * @param timeLeft the new time left
	 */
	private void setTimeLeft(long timeLeft) {
		this.timeLeft = timeLeft;
	}

	/**
	 * Gets the special swap left.
	 *
	 * @return the special swap left
	 */
	public int getSpecialSwapsLeft() {
		return specialSwapsLeft;
	}

	/**
	 * Sets the special swap left.
	 *
	 * @param specialSwapsLeft the new special swap left
	 */
	private void setSpecialSwapsLeft(int specialSwapsLeft) {
		this.specialSwapsLeft = specialSwapsLeft;
	}

	/**
	 * Put to occupier matrix.
	 *
	 * @param matrix the matrix
	 * @param occupier the occupier
	 */
	private void putToOccupierMatrix(SquareOccupier[][] matrix,
			SquareOccupier occupier) {
		int x = occupier.getPosition().getX();
		int y = occupier.getPosition().getY();
		matrix[y][x] = occupier;
	}

}
