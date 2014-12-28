package cas;

import java.io.File;

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

import engines.GameEngine;
import occupiers.Lokum;
import occupiers.SquareOccupier;
import occupiers.StripedLokum;

// TODO: Auto-generated Javadoc
/**
 * The Class GameSaver.
 */
public class GameSaver extends Configuration {
	
	/** The instance. */
	private static GameSaver instance;

	/**
	 * Instantiates a new game saver.
	 */
	private GameSaver() {
		super();		
	}

	/**
	 * Gets the single instance of GameSaver.
	 *
	 * @return single instance of GameSaver
	 */
	public static GameSaver getInstance() {
		if (instance == null)
			instance = new GameSaver();
		return instance;
	}
	
	/**
	 * Save current game.
	 *
	 * @param gamePath the game path 	 
	 * @requires Game should be initialized, and all fields of GameEngine should be filled.
	 * @modifies 
	 * @ensures a new xml is created according to fields of GameEngine
	 */
	public void saveCurrentGame(String gamePath) {
		Player sPlayer = GameEngine.getInstance().getPlayer();
		SquareOccupier[][] sMatrix = Board.getInstance().getBoardMatrix();
		double sScore = GameEngine.getInstance().getScore();
		int sMovesLeft = GameEngine.getInstance().getMovesLeft();
		int sLevelNumber = GameEngine.getInstance().getLevel().getLevelNumber();
		gamePath = gamePath.toLowerCase();
		gamePath = gamePath.replaceAll("\\s+", "");
		String playerPath = sPlayer.getName().toLowerCase();
		playerPath = playerPath.replaceAll("\\s+", "");
		File directory = new File("xml/gameSaves/" + playerPath);
		File gameXMLFile = new File("xml/gameSaves/" + playerPath + "/"
				+ gamePath + ".xml");
		try {			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();

			// game element
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("game");
			doc.appendChild(rootElement);

			// player element
			Element ePlayer = doc.createElement("player");
			rootElement.appendChild(ePlayer);

			// id element
			Element eID = doc.createElement("id");
			eID.appendChild(doc.createTextNode(Integer.toString(sPlayer.getId())));
			ePlayer.appendChild(eID);

			// name element
			Element eName = doc.createElement("name");
			eName.appendChild(doc.createTextNode(sPlayer.getName()));
			ePlayer.appendChild(eName);

			// board element
			Element eBoard = doc.createElement("board");
			rootElement.appendChild(eBoard);

			// lokums element
			Element eLokums = doc.createElement("lokums");
			eBoard.appendChild(eLokums);

			for (int i = 0; i < sMatrix.length; i++) {
				for (int j = 0; j < sMatrix[0].length; j++) {
					SquareOccupier current = sMatrix[i][j];
					Element eColor = doc.createElement("color");
					Element ePosition = doc.createElement("position");
					Element eXCoord = doc.createElement("xcoord");
					Element eYCoord = doc.createElement("ycoord");
					Element eType = doc.createElement("type");
					Element eDirection = doc.createElement("direction");
					// lokum element
					if (current != null && current instanceof Lokum) {
						Element eLokum = doc.createElement("lokum");

						// color element
						eColor.appendChild(doc.createTextNode(((Lokum) current)
								.getColor()));
						eLokum.appendChild(eColor);

						// position element
						eXCoord.appendChild(doc.createTextNode(Integer
								.toString(current.getPosition().getX())));
						eYCoord.appendChild(doc.createTextNode(Integer
								.toString(current.getPosition().getY())));
						ePosition.appendChild(eXCoord);
						ePosition.appendChild(eYCoord);
						eLokum.appendChild(ePosition);

						// type element
						String type = ((Lokum) current)
								.getClass().getSimpleName();
						eType.appendChild(doc.createTextNode(type));
						
						//direction element
						if(type.equals("StripedLokum")){
						String direction = ((StripedLokum) current).getDirection();
						eDirection.appendChild(doc.createTextNode(direction));
						eLokum.appendChild(eDirection);
						}
						eLokum.appendChild(eType);

						eLokums.appendChild(eLokum);
					}
				}
			}

			// currentscore element
			Element eCurrentScore = doc.createElement("currentscore");
			eCurrentScore.appendChild(doc.createTextNode(Double
					.toString(sScore)));
			rootElement.appendChild(eCurrentScore);

			// movesleft element
			Element eMovesLeft = doc.createElement("movesleft");
			eMovesLeft.appendChild(doc.createTextNode(Integer
					.toString(sMovesLeft)));
			rootElement.appendChild(eMovesLeft);

			//level element
			Element eLevel = doc.createElement("level");
			eLevel.appendChild(doc.createTextNode(Integer.toString(sLevelNumber)));
			rootElement.appendChild(eLevel);
			
			//time element
			if(GameEngine.getInstance().getLevel().getGoal() instanceof TimeScoreGoal){
			Element eTime = doc.createElement("timeleft");
			eTime.appendChild(doc.createTextNode(Long.toString(GameEngine.getInstance().getTimeLeft())));
			rootElement.appendChild(eTime);
			}
			
			//specialSwap element
			Element eSpecialSwap = doc.createElement("specialswapsleft");
			eSpecialSwap.appendChild(doc.createTextNode(Integer.toString(GameEngine.getInstance().getSpecialSwapsLeft())));
			rootElement.appendChild(eSpecialSwap);

			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty(OutputKeys.METHOD, "xml");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
					"4");
			DOMSource source = new DOMSource(doc);
			if(!directory.exists()){
				directory.mkdirs();
			}
			StreamResult result = new StreamResult(gameXMLFile);			
			tf.transform(source, result);

		} catch (ParserConfigurationException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
