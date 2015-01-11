package cas;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import engines.*;

/**
 * The Class SessionSaver.
 */
public class SessionSaver extends Configuration{

	/** The instance. */
	private static SessionSaver instance;

	/**
	 * Instantiates a new session saver.
	 */
	private SessionSaver() {
		super();

	}

	/**
	 * Gets the single instance of SessionSaver.
	 *
	 * @return single instance of SessionSaver
	 */
	public static SessionSaver getInstance() {
		if (instance == null)
			instance = new SessionSaver();
		return instance;
	}	

	/**
	 * Save session. 
	 * @requires Game should be initialized, and all fields of GameEngine and GUIEngine should be filled.
	 * @modifies 
	 * @ensures a new xml is created according to fields of GameEngine and GUIEngine
	 */
	public void saveSession() {
		Player Player = GameEngine.getInstance().getPlayer();
		int MaxLevelNumber = 0;
		if(GameEngine.getInstance().getLevel().getLevelNumber() < GUIEngine.getInstance().getMaxLevel()){
			MaxLevelNumber = GUIEngine.getInstance().getMaxLevel();
		}
		else{
			MaxLevelNumber = GameEngine.getInstance().getLevel().getLevelNumber() + 1;
		}
		double Score = GameEngine.getInstance().getScore();
		String sessionPath = Player.getName().toLowerCase();
		sessionPath = sessionPath.replaceAll("\\s+", "");
		File directory = new File("xml/sessionSaves/");
		File sessionXMLFile = new File("xml/sessionSaves/" + sessionPath
				+ ".xml");
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();

			// session element
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("session");
			doc.appendChild(rootElement);

			// player element
			Element ePlayer = doc.createElement("player");
			rootElement.appendChild(ePlayer);

			// id element
			Element eID = doc.createElement("id");
			eID.appendChild(doc.createTextNode(Integer.toString(Player.getId())));
			ePlayer.appendChild(eID);

			// name element
			Element eName = doc.createElement("name");
			eName.appendChild(doc.createTextNode(Player.getName()));
			ePlayer.appendChild(eName);

			// currentscore element
			Element eCurrentScore = doc.createElement("score");
			eCurrentScore.appendChild(doc.createTextNode(Double.toString(Score)));
			rootElement.appendChild(eCurrentScore);

			// level element
			Element eLevel = doc.createElement("maxlevel");
			eLevel.appendChild(doc.createTextNode(Integer.toString(MaxLevelNumber)));
			rootElement.appendChild(eLevel);

			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty(OutputKeys.METHOD, "xml");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
					"4");
			DOMSource source = new DOMSource(doc);
			if(!directory.exists()){
				directory.mkdirs();
			}
			StreamResult result = new StreamResult(sessionXMLFile);
			tf.transform(source, result);
		} catch (ParserConfigurationException
				| TransformerFactoryConfigurationError | TransformerException e) {
			e.printStackTrace();
		}
	}

}
