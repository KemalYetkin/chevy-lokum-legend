package cas;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Class Level.
 */
public class Level {

	/** The level number. */
	private int levelNumber;

	/** The goal. */
	private Goal goal;

	/** The max moves. */
	private int maxMoves;

	/** The special swap counter. */
	private int specialSwapCounter;

	/** The xml file. */
	private static File xmlFile = new File("xml/levels.xml");

	/** The instance. */
	private static Level instance;

	/**
	 * Instantiates a new level.
	 */
	private Level() {			
	}

	/**
	 * Gets the single instance of Level.
	 *
	 * @return single instance of Level
	 */
	public static Level getInstance() {
		if (instance == null)
			instance = new Level();
		return instance;
	}

	/**
	 * Load level.
	 *
	 * @param levelNo the level no
	 * @requires a proper xml file in correct path.
	 * @modifies all fields of this class
	 * @ensures fields are filled according to xml file.
	 * @return the level
	 */
	public Level loadLevel(int levelNo){
		setLevelNumber(levelNo);			
		Document document;		
		try {
			document = XMLParser.getDomElement(xmlFile);			

			//find the according level
			NodeList nodeList = document.getElementsByTagName("level");
			Element eLevel = null;
			for(int i = 0; i < nodeList.getLength();i++){
				eLevel = (Element) nodeList.item(i);
				int currentLevelNo = Integer.parseInt(XMLParser.getValue(eLevel, "levelno"));
				if(currentLevelNo == levelNo){
					break;
				}				
			}			

			//goal parse			
			Element eGoal = (Element) eLevel.getElementsByTagName("goal").item(0);
			String className = XMLParser.getValue(eGoal, "classname");
			int goal = Integer.parseInt(XMLParser.getValue(eGoal, "scoretarget"));
			if(className.equals(TimeScoreGoal.class.getSimpleName())){
				long time = Long.parseLong(XMLParser.getValue(eGoal, "timetarget"));				
				setGoal(new TimeScoreGoal(goal,time));
			}
			else{
				setGoal(new ScoreGoal(goal));
			}		

			//maxmoves parse			
			setMaxMoves(Integer.parseInt(XMLParser.getValue(eLevel, "maxmoves")));

			//specialSwapCounter parse
			setSpecialSwapCounter(levelNo);

			return this;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;


	}	

	/**
	 * Gets the total num levels.
	 * @requires a proper xml file in correct path.
	 * @modifies 
	 * @ensures 
	 * @return the total num levels
	 */
	public static int getTotalNumLevels() {		
		Document document;		
		try {
			document = XMLParser.getDomElement(xmlFile);

			NodeList nodeList = document.getElementsByTagName("levelno");
			return nodeList.getLength();

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return 1;

	}

	/**
	 * Gets the special swap counter.
	 *
	 * @return the special swap counter
	 */
	public int getSpecialSwapCounter() {
		return specialSwapCounter;
	}

	/**
	 * Sets the special swap counter.
	 *
	 * @param specialSwapCounter the new special swap counter
	 */
	private void setSpecialSwapCounter(int specialSwapCounter) {
		this.specialSwapCounter = specialSwapCounter;
	}

	/**
	 * Gets the level number.
	 *
	 * @return the level number
	 */
	public int getLevelNumber() {
		return levelNumber;
	}

	/**
	 * Sets the level number.
	 *
	 * @param level the new level number
	 */
	private void setLevelNumber(int level) {
		this.levelNumber = level;
	}

	/**
	 * Gets the goal.
	 *
	 * @return the goal
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * Sets the goal.
	 *
	 * @param goal the new goal
	 */
	private void setGoal(Goal goal) {
		this.goal = goal;
	}

	/**
	 * Gets the max moves.
	 *
	 * @return the max moves
	 */
	public int getMaxMoves() {
		return maxMoves;
	}

	/**
	 * Sets the max moves.
	 *
	 * @param maxMoves the new max moves
	 */
	private void setMaxMoves(int maxMoves) {
		this.maxMoves = maxMoves;
	}

}
