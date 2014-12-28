package occupiers;

public class StripedLokum extends SpecialLokum {
	
	private String direction;
	
	/**
	 * Constructor
	 * Creates a specialLokum whose type is striped
	 * Set a random direction initially
	 * @modifies direction
	 */
	public StripedLokum() {
		super("striped");
		double directionProb = Math.random();
		if (directionProb<=0.5){
			direction = "horizontal";
		} else {
			direction = "vertical";
		}
		setDirection(direction);
		getDescription().setType(getDescription().getType() + "_" + direction);
	}

	/**
	 * @return the direction of the striped Lokum
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction new direction of the stripped Lokum
	 * @modifies direction
	 */
	public void setDirection(String direction) {
		this.direction = direction;
		this.getDescription().setType("striped_"+direction);
	}
}
