package occupiers;

public class SquareOccupierDescription {
	// the type of the square occupier
	private String type;
	
	/**
	 * Constructor
	 * @param type the type of the Lokum itself like regular, time, wrapped etc.
	 */
	public SquareOccupierDescription(String type) {
		setType(type);
	}
	
	/**
	 * @return the type of the square occupier
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @param type new type of the description
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}
