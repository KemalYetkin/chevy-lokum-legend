package occupiers;

public class LokumDescription extends SquareOccupierDescription {
	// the color of the Lokum
	private String color;
		
	/**
	 * @modifies color
	 * @param type the type of the Lokum representation
	 * @param color the color of the Lokum representation
	 */
	public LokumDescription(String type, String color) {
		super(type);
		setColor(color);
	}
	
	/**
	 * @return the color of the representation
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color new color of the representation
	 */
	public void setColor(String color) {
		this.color = color;
	}

}
