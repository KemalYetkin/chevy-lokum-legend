package occupiers;

import java.util.ArrayList;
import cas.Position;
import cas.TimeScoreGoal;
import engines.GameEngine;

public class SquareOccupierFactory {
	// possible color list
	@SuppressWarnings("serial")
	public static ArrayList<String> possibleColors = new ArrayList<String>() {{add("red"); add ("green"); add("white"); add("brown");}};
	// the singleton instance of the class
	private static SquareOccupierFactory instance = null;

	/**
	 * Constructor
	 */
	private SquareOccupierFactory() {}

	/**
	 * Singleton getInstance method
	 * @return the instance of the factory
	 * @ensures the instance of the factory is generated
	 * @modifies instance
	 */
	public static SquareOccupierFactory getInstance(){
		if (instance == null)
			instance = new SquareOccupierFactory();
		return instance;
	}

	/**
	 * @return a randomly created regular Lokum according to the level type
	 */
	public Lokum generateRandomRegularLokum(){
		int randomNumber = (int) Math.floor(Math.random() * possibleColors.size());
		Lokum newLokum = null;
		if (GameEngine.getInstance().getLevel().getGoal() instanceof TimeScoreGoal &&  (int) Math.floor(Math.random() * 20) == 0)
			newLokum = new TimeLokum();
		else
			newLokum = new RegularLokum();
		newLokum.setColor(possibleColors.get(randomNumber));
		return newLokum;
	}

	/**
	 * @param pos the position of the newly created regular Lokum
	 * @return a randomly created regular Lokum with specified position according to the level type
	 */
	public Lokum generateRandomRegularLokum(Position pos){
		Lokum newLokum = generateRandomRegularLokum();
		newLokum.setPosition(pos);
		return newLokum;
	}

	/**
	 * @param color the color of the newly created Lokum
	 * @param type the type of the newly created Lokum
	 * @return a newly created Lokum whose color and type
	 * 		   are the specified ones
	 * @ensures a new Lokum is generated; if an exception occurs, it is catched properly
	 */
	@SuppressWarnings("rawtypes")
	public Lokum generateLokum(String color, String type){
		type = "occupiers." + type;

		Class classOfLokum;
		Lokum newLokum = null;
		try {
			classOfLokum = Class.forName(type);
			newLokum = (Lokum) classOfLokum.newInstance();
			newLokum.setColor(color);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return newLokum;
	}
}
