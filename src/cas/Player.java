package cas;

import java.util.HashMap;

/**
 * Player is a class that can hold the id, and name information.
 *
 */
public class Player {
	private String name;
	private static HashMap<String, Integer> map = new HashMap<String, Integer>();

	/**
	 * Constructor
	 */
	public Player(String name) {
		setName(name);
		setId((int) Math.floor((Math.random() * 1000)));
	}

	/**
	 * Returns the id number of the player
	 * 
	 * @return int id
	 */
	public int getId() {
		return map.get(getName());
	}

	/**
	 * Set the id field of the Player object
	 * 
	 * @modifies id field of the Player object
	 * @return
	 */
	protected void setId(int id) {
		if (!map.containsValue(id)) {
			map.put(name, id);
		} else {
			setId((int) Math.floor((Math.random() * 1000)));
		}
	}

	/**
	 * Returns the name of the player
	 * 
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name field of the Player object
	 * 
	 * @modifies name fields of the Player object
	 * @return
	 */
	protected void setName(String name) {
		this.name = name;
	}

}
