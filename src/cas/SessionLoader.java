package cas;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Class SessionLoader.
 */
public class SessionLoader extends Configuration {

	/** The max level. */
	private Level maxLevel;

	/** The score. */
	private double score;

	/** The player. */
	private Player player;

	/** The session xsd file. */
	private File sessionXSDFile;

	/** The instance. */
	private static SessionLoader instance;

	/**
	 * Instantiates a new session loader.
	 */
	private SessionLoader() {
		super();
		sessionXSDFile = new File("xml/sessionSaves/session.xsd");
	}

	/**
	 * Gets the single instance of SessionLoader.	 
	 * @requires 
	 * @modifies SessionLoader.instance if it has not been initialized yet
	 * @ensures SessiionLoader.instance is instantiated if it has not been yet
	 * @return single instance of SessionLoader
	 */
	public static SessionLoader getInstance() {
		if (instance == null)
			instance = new SessionLoader();
		return instance;
	}

	/**
	 * Load session.
	 *
	 * @param Player the player
	 * @requires A Schema in directory "xml/sessionSaves/session.xsd" should be initialized.
	 * @modifies All fields of this class.
	 * @ensures Fields of this class are filled from xml.
	 */
	public void loadSession(Player Player) {
		String sessionPath = Player.getName().toLowerCase();

		sessionPath = sessionPath.replaceAll("\\s+", "");

		File sessionXMLFile = new File("xml/sessionSaves/" + sessionPath
				+ ".xml");
		if (!validate(sessionXSDFile, sessionXMLFile)) {
			return;
		}
		try {
			Document document;
			document = XMLParser.getDomElement(sessionXMLFile);

			setPlayer(loadPlayer(document));
			setScore(loadScore(document));
			setMaxLevel(loadMaxLevel(document));

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Checks if is player loadable.
	 *
	 * @param Player the player 
	 * @requires A directory "xml/sessionSaves" should be initialized.
	 * @modifies 
	 * @ensures there is a player that saved a session before	
	 * @return true, if is player loadable
	 */
	public boolean isPlayerLoadable(Player Player) {
		String playerPath = Player.getName().toLowerCase();
		File playerDir = new File("xml/sessionSaves/" + playerPath + ".xml");
		return playerDir.exists();
	}

	/**
	 * Load player.
	 *
	 * @param document the document
	 * @requires document should be parsed from a validated XML file.
	 * @modifies
	 * @ensures 
	 * @return the player
	 */
	private Player loadPlayer(Document document) {
		NodeList nodeList = document.getElementsByTagName("player");
		Element ePlayer = (Element) nodeList.item(0);
		Player player = new Player(XMLParser.getValue(ePlayer, "name"));
		player.setId(Integer.parseInt(XMLParser.getValue(ePlayer, "id")));
		return player;
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
		NodeList nodeList = document.getElementsByTagName("score");
		Element eScore = (Element) nodeList.item(0);
		double score = Double.parseDouble(XMLParser.getElementValue(eScore));
		return score;
	}

	/**
	 * Load max level.
	 *
	 * @param document the document
	 * @requires document should be parsed from a validated XML file.
	 * @modifies
	 * @ensures 
	 * @return the level
	 */
	private Level loadMaxLevel(Document document) {

		NodeList nodeList = document.getElementsByTagName("maxlevel");
		Element eLevel = (Element) nodeList.item(0);
		Level level = Level.getInstance().loadLevel(
				Integer.parseInt(XMLParser.getElementValue(eLevel)));
		return level;
	}

	/**
	 * Gets the max level.
	 *
	 * @return the max level
	 */
	public Level getMaxLevel() {
		return maxLevel;
	}

	/**
	 * Sets the max level.
	 *
	 * @param MaxLevel the new max level
	 */
	private void setMaxLevel(Level MaxLevel) {
		this.maxLevel = MaxLevel;
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
	 * @param Score the new score
	 */
	private void setScore(double Score) {
		this.score = Score;
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
	 * @param Player the new player
	 */
	private void setPlayer(Player Player) {
		this.player = Player;
	}

}
